package execution.gamestate;

import graphic.Background;
import graphic.Button;
import graphic.GamePanel;
import graphic.TextureManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MenuState extends GameState{
	
	private BufferedImage bg;
	
	//test moving cloud in background
	private Background cloud1;
	private Background cloud2;
	
	private Font titleFont;
	private Color titleColor;
	private String title;
	
	private Font buttonFont;
	private Button play;
	private Button help;
	private Button exit;

	public MenuState(GameStateManager gsm, ArrayList<GameState> gameStates) {
		this.gsm = gsm; 
		
		bg = TextureManager.menuBackground.getTexture(0);
		
		titleFont = new Font("Century Gothic", Font.PLAIN, 48);
		titleColor = Color.RED;
		title = "The Fallen King";
		
		buttonFont = new Font("Century Gothic", Font.PLAIN, 18);
		play = new Button(GamePanel.width/2-100, 240, 200, 80, Color.LIGHT_GRAY, "PLAY", Color.BLACK, buttonFont);
		help = new Button(GamePanel.width/2-100, 340, 200, 80, Color.LIGHT_GRAY, "HELP", Color.BLACK, buttonFont);
		exit = new Button(GamePanel.width/2-100, 440, 200, 80, Color.LIGHT_GRAY, "EXIT", Color.BLACK, buttonFont);
		cloud1 = new Background(TextureManager.cloud.getTexture(0), 800, 100, (int) (TextureManager.cloud.getTexture(0).getWidth()*1.8), (int) (TextureManager.cloud.getTexture(0).getHeight()*1.8), 0.01, 0);
		cloud2 = new Background(TextureManager.flipTextureHorizzontaly(TextureManager.cloud.getTexture(0)), -150, 50, (int) (TextureManager.cloud.getTexture(0).getWidth()*2), (int) (TextureManager.cloud.getTexture(0).getHeight()*2), -0.02, 0);
	}

	@Override
	public void init() {}

	@Override
	public void update() {
		cloud1.update();
		cloud2.update();
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(bg, 0, 0, GamePanel.width, GamePanel.height, null);
		cloud1.draw(g);
		cloud2.draw(g);
		
		g.setFont(titleFont);
		g.setColor(titleColor);
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(title, g);
		g.drawString(title,(int) (GamePanel.width/2-rect.getWidth()/2), 100);
		
		g.setFont(buttonFont);
		
		play.draw(g);
		help.draw(g);
		exit.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(play.mouseClicked(e)) {
			gsm.setState(1);
		}
		if(help.mouseClicked(e)) {
			
		}
		if(exit.mouseClicked(e)) {
			System.exit(0);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		play.mousePressed(e);
		help.mousePressed(e);
		exit.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		play.mouseReleased(e);
		help.mouseReleased(e);
		exit.mouseReleased(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

}
