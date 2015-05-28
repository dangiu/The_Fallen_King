package execution;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import networking.ConnectionHandler;
import core.KillEntityCondition;
import core.PlayerInfo;
import core.Team;
import core.World;
import core.WorldInfo;
import core.entities.Castle;


/**
 * Server di esecuzione della simulazione del mondo.
 * Gestisce le connessioni con i client e l'esecuzione delle simulazioni.
 * 
 * @author Willi Menapace
 *
 */
class Server {
	
	private static final int TICKS = 50;

	//Lo stato del mondo su cui vengono effettuate le simulazioni
	private World world;
	
	//Gestisce la connessione di rete con i client
	private ConnectionHandler connectionHandler;
	//Gli stream ai client per gli output dello stato
	private Map<Team, DataOutputStream> stateTransferStreams;
	
	//Ricevitori asincroni di comandi dei client
	private Map<Team, CommandReceiver> commandReceivers;
	
	//Timer per l'esecuzione del lavoro ciclico
	Timer executionTimer;
	
	private boolean serverStopped = false;
	
	/**
	 * Configura un server impostato per un match standard
	 */
	public Server() {
		executionTimer = new Timer();
		
		stateTransferStreams = new HashMap<>();
		
		commandReceivers = new HashMap<>();
		connectionHandler = new ConnectionHandler();
		
		WorldInfo worldInfo = new WorldInfo(50);
		
		Map<Team, PlayerInfo> playerInfo = new HashMap<>();
		
		playerInfo.put(Team.BLUE, new PlayerInfo(500));
		playerInfo.put(Team.RED, new PlayerInfo(500));
		world = new World(worldInfo, playerInfo);
		
		
		//Inserisce i castelli
		Castle blueCastle = new Castle(Team.BLUE, 0 - Castle.width/2);
		Castle redCastle = new Castle(Team.RED, worldInfo.getWorldWidth() - blueCastle.getBox().getWidth()/2);
		
		world.addEntity(blueCastle);
		world.addEntity(redCastle);
		
		//Inserisce le condizioni di vittoria
		KillEntityCondition killBlue = new KillEntityCondition(blueCastle);
		KillEntityCondition killRed = new KillEntityCondition(redCastle);
		
		world.addVictoryCondition(Team.BLUE, killRed);
		world.addVictoryCondition(Team.RED, killBlue);
	
	}
	
	/**
	 * Instaura una connessione con i giocatori.
	 * Inizializza i ricettori di comandi senza avviarli.
	 * Inizializza gli stream di output dello stato.
	 * Termina l'applicazione in caso di fallimento
	 */
	private void acceptPlayers() {
		
		//Accetta le connessioni dei giocatori
		try {
			connectionHandler.waitAllClients();
		} catch(IOException e) {
			System.out.println("Impossible to accept client connections due to network error:\n" + e.getMessage());
			System.exit(1);
		}
		
		//Ottiene gli stream di output dei client e li inserisce nella mappa corrispondente
		//Ottiene gli stream di inupt dei client e avvia i thread di ricezione comandi
		for(Team team : Team.values()) {
			DataInputStream commandInputStream = null;
			DataOutputStream stateOutputStream = null;
			try {
				stateOutputStream = new DataOutputStream(new BufferedOutputStream(connectionHandler.getStateOutput(team)));
				commandInputStream = new DataInputStream(new BufferedInputStream(connectionHandler.getCommandInput(team)));				
			} catch(IOException e) {
				System.out.println("Impossible to establish a communication channel:\n" + e.getMessage());
				System.exit(1);
			}

			stateTransferStreams.put(team, stateOutputStream);
			
			CommandReceiver commandReceiver = new CommandReceiver(commandInputStream);
			commandReceivers.put(team, commandReceiver);
		}
		
	}
	
	/**
	 * Inizia la simulazione sul server avviando effettivamente la partita
	 * Il server non puo' essere riavviato dopo essere stato chiuso
	 */
	public void startExecution() {
		
		if(serverStopped) {
			throw new IllegalStateException("Cannot restart a closed server");
		}
		
		//Stabilisce le connessioni con i client
		acceptPlayers();
		
		//Inizia la ricezione dei comandi
		for(Team team : Team.values()) {
			commandReceivers.get(team).start();
		}
		
		//Esegue il lavoro ciclico del server al rateo fissato
		executionTimer.scheduleAtFixedRate(new ServerExecutionTask(), 0, 1000 / TICKS);
	}
	
	/**
	 * Termina definitivamente il server deallocando le risorse utilizzate
	 */
	public void closeServer() {
		
		serverStopped = true;
		
		//Termina l'esecuzione del lavoro ciclico
		executionTimer.cancel();
		
		//Termina la ricezione asincrona di comandi
		for(Team team : Team.values()) {
			commandReceivers.get(team).dispose();
		}
		
		//Disconnette tutti i client connessi e dealloca le risorse di rete
		connectionHandler.closeAllConnections();
		
		//I client sono disconnessi, dunque gli stream non sono piu' utilizzabili
		stateTransferStreams.clear();
		commandReceivers.clear();
		
	}
	
	/**
	 * Si occupa dell'esecuzione del lavoro ciclico necessario per l'elaborazione dei comandi,
	 * la simulazione del mondo e l'invio dei risultati dell'elaborazione ai client
	 * 
	 * @author Willi Menapace
	 *
	 */
	private class ServerExecutionTask extends TimerTask {
		
		@Override
		public void run() {
			
			//Elabora i comandi ricevuti dai client
			for(Team team : Team.values()) {
				CommandReceiver receiver = commandReceivers.get(team);
				while(receiver.isCommandAvailable()) {
					receiver.getNextCommand().execute(world);
				}
			}
			
			//Simula il nuovo stato del mondo
			world.simulate(1000 / TICKS);
			
			//Invia il nuovo stato del mondo ai client
			for(Team team : Team.values()) {
				DataOutputStream stateOutputStream = stateTransferStreams.get(team);
				
				try {
					//NOTA: e' possibile parallelizzare l'invio per performance migliori
					//NOTA: il processo di serializzazione svolge lavoro duplicato
					World.SerializationHelper.serialize(world, stateOutputStream);
					stateOutputStream.flush(); //Forza lo svuotamento del buffer di invio
					
				} catch(IOException e) {
					System.out.println("Couldn't transfer new world state to client.\n" + e.getMessage());
				}
			}
		}
	}
	
}
