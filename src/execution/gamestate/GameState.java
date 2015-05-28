package execution.gamestate;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Classe astratta che rappresenta uno stato del gioco generico
 * @author Th3W4r70cK
 *
 */
public abstract class GameState {
	protected GameStateManager gsm;
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void mouseClicked(MouseEvent arg0);
	public abstract void mouseEntered(MouseEvent arg0);
	public abstract void mouseExited(MouseEvent arg0);
	public abstract void mousePressed(MouseEvent arg0);
	public abstract void mouseReleased(MouseEvent arg0);
	public abstract void keyPressed(KeyEvent arg0);
	public abstract void keyReleased(KeyEvent arg0);
	public abstract void keyTyped(KeyEvent arg0);
	
}
