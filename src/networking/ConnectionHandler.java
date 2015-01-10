package networking;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import core.Team;
import core.TeamSerializationHelper;

/**
 * Stabilisce una connessione con i client ritornando gli stream reletivi
 * 
 * @author Willi Menapace
 *
 */
public class ConnectionHandler {

	private int STATE_SERVER_PORT = 4069;
	private int COMMAND_SERVER_PORT = 4070;
	
	private boolean connectionsEstablished = false;
	
	//Socket per la trasmissione dello stato del mondo
	private Map<Team, Socket> stateTransferSockets = new HashMap<>();
	
	//Socket per la ricezione dei comandi
	private Map<Team, Socket> commandReceptionSockets = new HashMap<>();
	
	public ConnectionHandler() {
		
	}
	
	/**
	 * Stabilisce le connessioni con tutti i client di tutti i possibili team.
	 * Non devono essere attive altre connessioni dall'istanza corrente alla chiamata del metodo
	 * @throws IOException In caso non sia possibile stabilire la connessione con i client
	 * 
	 */
	public void waitAllClients() throws IOException {
		
		if(connectionsEstablished) {
			throw new IllegalStateException("Cannot establish new connections: connections already established must be closed first");
		}
		
		ServerSocket stateServer = new ServerSocket(STATE_SERVER_PORT);
		ServerSocket commandServer = new ServerSocket(COMMAND_SERVER_PORT);
		
		try {
			for(Team team : Team.values()) {
				
				//Al client e' assegnato il team
				Socket stateSocket = stateServer.accept();
				stateTransferSockets.put(team, stateSocket);
				Socket commandSocket = commandServer.accept();
				commandReceptionSockets.put(team, commandSocket);
				//Indica al client connesso che fa parte del team blu
				DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(stateSocket.getOutputStream()));
				TeamSerializationHelper.serializeTeam(team, outStream);
				outStream.flush(); //Si forza l'invio dei dati
				outStream.close(); //Libera le risorse
				
			}
		} catch(IOException e) {
			//Se l'inizializzazione si blocca a meta' resetta tutte le connessioni
			//per poter riprovare in seguito e segnala l'errore
			closeAllConnections();
			throw e;
		}
		
		connectionsEstablished = true;
		
		//Libera le risorse
		stateServer.close();
		commandServer.close();
	}
	
	/**
	 * Chiude tutte le connessioni instaurate attraverso l'istanza corrente
	 * Deve essere richiamato prima della distruzione dell'oggetto per evitare mancanze di risorse
	 */
	public void closeAllConnections() {
		for(Socket socket : stateTransferSockets.values()) {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Error while closing state transmission socket:\n" + e.getMessage());
			}
		}
		stateTransferSockets.clear();
		for(Socket socket : commandReceptionSockets.values()) {
			try {
			socket.close();
			} catch (IOException e) {
				System.out.println("Error while closing state transmission socket:\n" + e.getMessage());
			}
		}
		commandReceptionSockets.clear();
		
		connectionsEstablished = false;
	}
	
	/**
	 * Ritorna lo stream di output di stato associato al team. Non deve essere chiuso 
	 * @param team Il team a cui si vuole inviare lo stato
	 * @return Lo stream su cui inviare lo stato
	 * @throws IOException In caso sia impossibile stabilire la comunicazione
	 */
	public OutputStream getStateOutput(Team team) throws IOException {
		if(!connectionsEstablished) {
			throw new IllegalStateException("You must establish a connection before getting a stream");
		}
		
		OutputStream stream = stateTransferSockets.get(team).getOutputStream();

		return stream;
	}
	
	/**
	 * Ritorna lo stream di input di comandi associato al team
	 * @param team Il team di cui si vogliono ricevere i comandi
	 * @return Lo stream su cui si ricevono i comandi
	 * @throws IOException  In caso sia impossibile stabilire la connessione
	 */
	public InputStream getCommandInput(Team team) throws IOException {
		if(!connectionsEstablished) {
			throw new IllegalStateException("You must establish a connection before getting a stream");
		}
		
		return commandReceptionSockets.get(team).getInputStream();
	}
	
}
