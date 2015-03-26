package execution.gamestate;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GameStateManager {
	private ArrayList<GameState> gameStates;
	private int currentState;
	
	public static final int MENUSTATE = 0;
	
	public GameStateManager() {
		gameStates = new ArrayList<GameState>();
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
	}
	
	public void setState(int state) {
		currentState = state;
		gameStates.get(currentState).init();
	}
	
	public void update() {
		gameStates.get(currentState).update();
	}
	
	public void draw(java.awt.Graphics2D g) {
		gameStates.get(currentState).draw(g);
	}
	
	public void mouseClicked(MouseEvent arg0) {
		gameStates.get(currentState).mouseClicked(arg0);
	}

	public void mouseEntered(MouseEvent arg0) {
		gameStates.get(currentState).mouseEntered(arg0);
	}

	public void mouseExited(MouseEvent arg0) {
		gameStates.get(currentState).mouseExited(arg0);
	}

	public void mousePressed(MouseEvent arg0) {
		gameStates.get(currentState).mousePressed(arg0);
	}

	public void mouseReleased(MouseEvent arg0) {
		gameStates.get(currentState).mouseReleased(arg0);
	}
	
	public void keyPressed(KeyEvent k) {
		gameStates.get(currentState).keyPressed(k);
	}
	
	public void keyReleased(KeyEvent k) {
		gameStates.get(currentState).keyReleased(k);
	}
	
	public void keyTyped(KeyEvent k) {
		gameStates.get(currentState).keyTyped(k);
	}
}
