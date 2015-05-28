package execution.gamestate;

import graphic.Button;
import graphic.GamePanel;
import graphic.Textbox;
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

public class ConnectionState extends GameState {
	
	//bg
	private BufferedImage bg;
	
	//title
	private String title;
	private Font titleFont;
	private Color titleColor;
	
	//label
	private String label;
	private Font labelFont;
	private Color labelColor;
	
	//textbox
	private Textbox serverIP;
	
	//button
	private Button connect;
	private Button back;
	
	public ConnectionState(GameStateManager gsm, ArrayList<GameState> gameStates) {
		this.gsm = gsm;
		
		bg = TextureManager.menuBackground.getTexture(0);
		
		title = "Connect to server:";
		titleFont = new Font("Century Gothic", Font.PLAIN, 48);
		titleColor = Color.BLUE;
		
		label = "Server IP:";
		labelFont = new Font("Century Gothic", Font.PLAIN, 20);
		labelColor = Color.BLACK;
		
		int textboxWidth = 200;
		int textboxHeight = 30;
		serverIP = new Textbox(GamePanel.width/2-textboxWidth/2, (int) (4.0/10*GamePanel.height), textboxWidth, textboxHeight);
		connect = new Button(GamePanel.width/2-100, (int) (5.0/10*GamePanel.height), 200, 80, Color.LIGHT_GRAY, "CONNECT", Color.BLACK, labelFont);
		back = new Button(GamePanel.width/2-100, (int) (7.0/10*GamePanel.height), 200, 80, Color.LIGHT_GRAY, "BACK", Color.BLACK, labelFont);
	}

	@Override
	public void init() {}

	@Override
	public void update() {}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(bg, 0, 0, GamePanel.width, GamePanel.height, null);
		
		g.setFont(titleFont);
		g.setColor(titleColor);
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(title, g);
		g.drawString(title, (int) (GamePanel.width/2-rect.getWidth()/2), 100);
		
		g.setFont(labelFont);
		g.setColor(labelColor);
		FontMetrics fm2 = g.getFontMetrics();
		Rectangle2D rect2 = fm2.getStringBounds(label, g);
		g.drawString(label, (int) (GamePanel.width/2-rect2.getWidth()/2), (int) (3.0/10*GamePanel.height));
		
		serverIP.draw(g);
		connect.draw(g);
		back.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		serverIP.mouseClicked(e);
		if(connect.mouseClicked(e)) {
			gsm.setState(GameStateManager.PLAYSTATE);
		}
		if(back.mouseClicked(e)) {
			gsm.setState(0);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		connect.mousePressed(e);
		back.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		connect.mouseReleased(e);
		back.mouseReleased(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {
		serverIP.keyTyped(e);
	}
	
	public String getServerIP() {
		return serverIP.getText();
	}

}
