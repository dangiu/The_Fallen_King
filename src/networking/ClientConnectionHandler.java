package networking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import core.Team;
import core.TeamSerializationHelper;

/**
 * Stabilisce e gestisce la connessione con il server
 * 
 * @author Daniele Giuliani
 *
 */
public class ClientConnectionHandler {
	
	private boolean connectionEstablished = false;
	private Socket stateSocket;
	private Socket commandSocket;
	
	public ClientConnectionHandler() {
		
	}
	
	/**
	 * Metodo che si occupa di stabilire la connessione del client al server
	 * @param serverIP l'IP del server a cui ci si deve connettere
	 * @return il team a cui si appartiene che viene comunicato dal server a connessione effettuata
	 * @throws IOException
	 */
	public Team connectToServer(InetAddress serverIP) throws IOException {
		
		if(connectionEstablished) {
			throw new IllegalStateException("Cannot establish new connections: connections already established must be closed first");
		}
		
		stateSocket = new Socket(serverIP, ConnectionHandler.STATE_SERVER_PORT);
		commandSocket = new Socket(serverIP, ConnectionHandler.COMMAND_SERVER_PORT);
		
		DataInputStream inputStream = new DataInputStream(new BufferedInputStream(stateSocket.getInputStream()));
		
		connectionEstablished = true;
		
		return TeamSerializationHelper.deserializeTeam(inputStream);
	}
	
	/**
	 * Metodo che chiude la connessione con il server
	 */
	public void closeConnection() {
		try {
			stateSocket.close();
		} catch (IOException e) {
			System.out.println("Error while closing state transmission socket:\n" + e.getMessage());
		}
		try {
			commandSocket.close();
		} catch (IOException e) {
			System.out.println("Error while closing command transmission socket:\n" + e.getMessage());
		}
		connectionEstablished = false;
	}
	
	/**
	 * Ritorna lo stream di input con cui si riceve lo stato del mondo dal server
	 * @return Lo stream su cui si riceve lo stato del mondo
	 * @throws IOException In caso sia impossibile stabilire la connessione
	 */
	public DataInputStream getStateInput() throws IOException {
		if(!connectionEstablished) {
			throw new IllegalStateException("You must establish a connection before getting a stream");
		}
		return new DataInputStream(new BufferedInputStream(stateSocket.getInputStream()));
	}
	
	/**
	 * Ritorna lo stream di output per comunicare i comandi al server 
	 * @return Lo stream su cui inviare i comandi
	 * @throws IOException In caso sia impossibile stabilire la comunicazione
	 */
	public DataOutputStream getCommandOutput() throws IOException {
		if(!connectionEstablished) {
			throw new IllegalStateException("You must establish a connection before getting a stream");
		}
		return new DataOutputStream(new BufferedOutputStream(commandSocket.getOutputStream()));
	}
	
}
