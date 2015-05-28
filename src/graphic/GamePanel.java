package graphic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JPanel;

import execution.gamestate.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener{
	
	// options
	private static Properties options;
	public static int width;
	public static int height;
	
	// game thread
	private Thread gameThread;
	private boolean running;
	private static int FPS = 60;
	private static int targetTime = 1000/FPS;
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
	public GamePanel() {
		super();
		try {
			options = new Properties();
			loadProperties();
		}catch(FileNotFoundException e) {
			System.exit(0);
		}
		width = Integer.parseInt(options.getProperty("screen.width"));
		height = Integer.parseInt(options.getProperty("screen.height"));
		Dimension preferredSize = new Dimension(width, height);
		this.setPreferredSize(preferredSize);
		this.setFocusable(true);
		this.requestFocusInWindow();		
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
			System.out.println("Error: file TFK.properties not found!");
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			System.out.println("Error thile reading the TFK.properties file!");
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				System.out.println("Error while closing the input stream!");
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
			System.out.println("Error: file TFK.properties not found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Errore while writing the TFK.properties file!");
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				System.out.println("Error while closing the output stream!");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Metodo che ritorna le opzioni
	 * @return oggetto contenente le opzioni
	 */
	public static Properties getProperties() {
		return options;
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
		System.out.println("Options updated!");
	}
	
	public void addNotify() {
		super.addNotify();
		if(gameThread == null) {
			gameThread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			gameThread.start();
		}
	}
	
	private void init() {
		image = new BufferedImage(width, height , BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		gsm = new GameStateManager();
	}
	
	@Override
	public void run() {
		init();
		
		long start;
		long elapsed;
		long wait;
		
		//game loop
		while(running) {
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 1;
			
			try {
				Thread.sleep(wait);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void update() {
		gsm.update();
	}
	
	private void draw() {
		gsm.draw(g);
	}
	
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, width, height, null);
		g2.dispose();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		gsm.mouseClicked(arg0);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		gsm.mouseEntered(arg0);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		gsm.mouseExited(arg0);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		gsm.mousePressed(arg0);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		gsm.mouseReleased(arg0);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		gsm.keyPressed(arg0);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		gsm.keyReleased(arg0);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		gsm.keyTyped(arg0);
	}

}
