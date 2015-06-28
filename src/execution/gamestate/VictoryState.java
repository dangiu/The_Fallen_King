package execution.gamestate;

import graphic.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class VictoryState extends GameState {
	
	//GameState Array
	private ArrayList<GameState> gameStates;
	//victory
	private int victory;

	public VictoryState(GameStateManager gameStateManager, ArrayList<GameState> gameStates) {
		this.gameStates = gameStates;
		this.gsm = gameStateManager;
	}

	@Override
	public void init() {
		PlayState ps = (PlayState) gameStates.get(GameStateManager.PLAYSTATE);
		victory = ps.getVictory();
	}

	@Override
	public void update() {}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, GamePanel.width, GamePanel.height);
		if(victory == 1) {
			//blue won
			String text = "BLUE WON";
			g.setColor(Color.BLUE);
			Font teamFont = new Font("Century Gothic", Font.PLAIN, 48);
			g.setFont(teamFont);
			GlyphVector gv = teamFont.createGlyphVector(g.getFontRenderContext(), text);
			Rectangle2D textBounds = gv.getPixelBounds(null, 0, 0);
			g.drawString(text, (int) (GamePanel.width/2-textBounds.getWidth()/2), (int) (GamePanel.height/2-textBounds.getHeight()/2));
		}else if(victory == 2) {
			//red won
			String text = "RED WON";
			g.setColor(Color.RED);
			Font teamFont = new Font("Century Gothic", Font.PLAIN, 48);
			g.setFont(teamFont);
			GlyphVector gv = teamFont.createGlyphVector(g.getFontRenderContext(), text);
			Rectangle2D textBounds = gv.getPixelBounds(null, 0, 0);
			g.drawString(text, (int) (GamePanel.width/2-textBounds.getWidth()/2), (int) (GamePanel.height/2-textBounds.getHeight()/2));
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
