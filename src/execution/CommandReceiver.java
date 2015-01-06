package execution;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.Command;
import core.CommandSerializationHelper;


/**
 * Classe in grado di rimanere in ascolto di comandi asincronamente
 * su un canale fornito e di esporre i risultati concorrentemente
 * 
 * @author Willi Menapace
 */
class CommandReceiver {
	
	//Coda contenente i comandi deserializzati in attesa che vengano consumati
	private Queue<Command> commands = new ConcurrentLinkedQueue<>();
	
	//Il thread che si occupa di ascoltare il canale e riempire la coda dei comandi
	private ChannelListener listener;
	
	/**
	 * Crea un ricevitore di comandi in ascolto su un dato canale
	 * @param in Il canale da ascoltare. Deve essere != null
	 */
	public CommandReceiver(DataInputStream in) {
		if(in == null) {
			throw new NullPointerException("Command input stream must be != null");
		}
		
		listener = new ChannelListener(in, commands);
		listener.start();
	}
	
	/**
	 * Termina l'ascolto del canale da parte del listener
	 */
	public void dispose() {
		listener.requestStop();
	}
	
	/**
	 * 
	 * @return true se e' disponibile un nuovo comando
	 */
	public boolean isCommandAvailable() {
		return commands.peek() == null;
	}
	
	/**
	 * 
	 * @return Il primo comando ricevuto non ancora processato
	 */
	public Command getNextCommand() {
		return commands.poll();
	}
	
	/**
	 * Thread che resta in ascolto su un canale di nuovi comandi
	 * Puo' essere avviato una sola volta
	 * @author Willi Menapace
	 *
	 */
	private static class ChannelListener extends Thread {
		
		//Il tempo dopo il quale se richiamato un metodo per terminare il thread
		//il thread viene chiuso forzatamente
		private static final int IO_TERMINATION_TIMEOUT = 500;
		
		//true se il thread deve terminarsi
		private boolean stopRequested = false;
		
		private DataInputStream in;
		private Queue<Command> commands;
		
		/**
		 * Riceve comandi da un dato canale e li inserisce nella struttura fornita
		 * @param in Il canale da cui ricevere i comandi
		 * @param commands La struttura in cui inserire i comandi ricevuti
		 */
		ChannelListener(DataInputStream in, Queue<Command> commands) {
			this.in = in;
			this.commands = commands;
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
					Command command = CommandSerializationHelper.deserializeCommand(in);
					commands.add(command);
				} catch(IOException e) {
					System.out.println("Something wrong happened with the command input stream:\n" + e.getMessage() + "\nCorrect execution is no longer guaranteed: Shutting down VM");
					System.exit(1);
				}
			}
		}
		
		
	}
	
}
