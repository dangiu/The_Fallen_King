package execution;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Properties;

import networking.ClientConnectionHandler;

/**
 * Client che esegue il gioco.
 * Gestisce la connessione con il server e l'impostazione del gioco.
 * 
 * @author Daniele Giuliani
 *
 */
public class Client {
	private InetAddress serverIP;
	private ClientConnectionHandler connectionHandler;
	private static Properties options;
	
	static{
		try {
			options = new Properties();
			loadProperties();
		} catch (FileNotFoundException e) {
			System.out.println("Errore: file TFK.properties non trovato!");
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	
	/**
	 * Metodo che carica le opzioni del programma dal file TFK.properties
	 * @throws FileNotFoundException file TFK.properties non trovato
	 */
	public static void loadProperties() throws FileNotFoundException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream("TFK.properties");
			options.load(inputStream);
		} catch (FileNotFoundException e) {
			System.out.println("Errore: file TFK.properties non trovato!");
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			System.out.println("Errore: lettura file TFK.properties non riuscita!");
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				System.out.println("Errore: chiusura stream di lettura non riuscita");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Metodo che salva le opzioni del programma nel file TFK.properties
	 */
	public static void saveProperties() {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream("TFK.properties");
			options.store(outputStream, "TFK OPTIONS -- DO NOT EDIT MANUALLY");
		} catch (FileNotFoundException e) {
			System.out.println("Errore: file TFK.properties non trovato!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Errore: scrittura file TFK.properties non riuscita!");
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				System.out.println("Errore: chiusura stream di scrittura non riuscita");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Metodo che ricarica le proprieta del programma nel file TFK.properties
	 */
	public static void updateProperties() {
		try {
			loadProperties();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Opzioni aggiornate");
	}
	
	public Client(InetAddress pServerIP) {
		this.serverIP = pServerIP;
	}
	
	
}
