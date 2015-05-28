package execution;

import java.io.DataInputStream;
import java.io.IOException;

import core.World;

/**
 * Classe che rimane in ascolto su un canale per ricever
 * nuovi stati del mondo dal server inoltre fornisce dei
 * metodi per verificare se è arrivato un nuovo mondo
 * 
 * @author Daniele Giuliani
 *
 */
public class WorldReceiver {
	//descrive se il mondo è cambiato
	private boolean worldChange;
	//contiene il mondo corrente inviato dal server
	private World currentWorld;
	//thread che continua a ricevere i mondi
	private WorldListener listener;
	
	/**
	 * Passiamo solamente lo stream di input da dove ricevere i mondi
	 * @param pIn
	 */
	public WorldReceiver(DataInputStream pIn) {
		if(pIn == null) {
			throw new NullPointerException("Command input stream must be != null");
		}
		listener = new WorldListener(pIn, this);
		worldChange = false;
	}
	
	/**
	 * Metodo per impostare il nuovo mondo, è sincronizzato per garantire l'accesso alla
	 * struttura in modo sicuro
	 * @param pWorld
	 */
	public synchronized void setWorld(World pWorld) {
		this.currentWorld = pWorld;
		worldChange = true;
	}
	
	/**
	 * Metodo per ottenere il nuovo mondo, è sincronizzato per garantire l'accesso alla
	 * struttura in modo sicuro
	 * @param pWorld
	 */
	public synchronized World getWorld() {
		if(worldChange == false || currentWorld == null) {
			throw new IllegalStateException("There must be a world to read");
		}
		worldChange = false;
		return this.currentWorld;
		
	}
	
	/**
	 * Metodo per verificare se è stato inserito un nuovo mondo
	 * @return
	 */
	public synchronized boolean hasWorldChange() {
		return worldChange;
	}
	
	/**
	 * Inizia la ricezione dei comandi
	 * Non puo' essere richiamato dopo aver terminato l'ascolto del canale
	 */
	public void start() {
		listener.start();
	}
	
	/**
	 * Termina irreversibilmente l'ascolto del canale da parte del listener
	 */
	public void dispose() {
		listener.requestStop();
	}
		
	/**
	 * Thread che resta in ascolto su un canale di nuovi mondi
	 * Puo' essere avviato una sola volta
	 * @author Daniele Giuliani
	 *
	 */
	private static class WorldListener extends Thread {
		
		//Il tempo dopo il quale se richiamato un metodo per terminare il thread
		//il thread viene chiuso forzatamente
		private static final int IO_TERMINATION_TIMEOUT = 500;
		
		//true se il thread deve terminarsi
		private boolean stopRequested = false;
		
		private DataInputStream in;
		private WorldReceiver worldReceiver;
		
		/**
		 * Riceve il mondo da un dato canale e li inserisce nella struttura comune
		 * @param in Il canale da cui ricevere il mondo
		 * @param commands La struttura in cui inserire i comandi ricevuti
		 */
		WorldListener(DataInputStream pIn, WorldReceiver pWorldReceiver) {
			this.in = pIn;
			this.worldReceiver = pWorldReceiver;
			
		}
		
		/**
		 * Termina il thread di ricezione comandi
		 */
		synchronized void requestStop() {
			stopRequested = true;
			
			//Il thread da terminare potrebbe essere impegnato in operazioni di IO bloccanti
			//quindi viene terminato forzatamente da un thread esterno se ancora vivo
			final Thread threadToTerminate = this;
			Thread terminatorThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(IO_TERMINATION_TIMEOUT);
						if(threadToTerminate.isAlive())
							threadToTerminate.interrupt();
					} catch(Exception e) {
						System.out.println("Something bad happened while termination thread was sleeping:\n" + e.getMessage());
					}
				}
			});
			terminatorThread.start();
		}
		
		/**
		 * Verifica se il thread deve terminarsi
		 * @return
		 */
		synchronized boolean isStopRequested() {
			return stopRequested;
		}
		
		@Override
		public void run() {
			if(isStopRequested()) {
				throw new IllegalStateException("Cannot restart an already disposed channel listener");
			}
			
			while(!isStopRequested()) {
				try {
					World recivedWorld = World.SerializationHelper.deserialize(in);
					worldReceiver.setWorld(recivedWorld);
				} catch(IOException e) {
					System.out.println("Something wrong happened with the command input stream:\n" + e.getMessage() + "\nCorrect execution is no longer guaranteed: Shutting down VM");
					System.exit(1);
				}
			}
		}
	}
}
