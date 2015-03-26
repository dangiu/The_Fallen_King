package execution;

import graphic.GamePanel;

import javax.swing.JFrame;

/**
 * Client che esegue il gioco.
 * 
 * @author Daniele Giuliani
 *
 */
public class Client {
	public static void main(String args[]) {
		JFrame window = new JFrame("The Fallen King");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}
}
