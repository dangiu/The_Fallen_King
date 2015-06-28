package execution;

public class StartServer {

	public static void main(String[] args) {
		Server serverTest = new Server();
		System.out.println("Server avviato!");
		serverTest.startExecution();
		System.out.println("Partita iniziata!");
	}

}
