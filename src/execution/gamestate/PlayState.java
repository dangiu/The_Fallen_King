package execution.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import networking.ClientConnectionHandler;
import core.BuyAndSpawnEntityCommand;
import core.CommandSerializationHelper;
import core.Team;
import core.entities.Archer;
import core.entities.Champion;
import core.entities.MovementDirection;
import core.entities.Piker;
import core.entities.Soldier;
import execution.WorldReceiver;
import graphic.Button;
import graphic.GamePanel;
import graphic.WorldRenderer;

public class PlayState extends GameState {
	//GameState Array
	private ArrayList<GameState> gameStates;
	
	//ServerIP
	private InetAddress serverIP;
	
	//ClientConnectionHandler
	private ClientConnectionHandler cch;
	
	//Team
	private Team team;
	//Movement direction
	private MovementDirection md;
	//SpawnPoint
	private int spp;
	
	//money
	private String money;
	private Font moneyFont;
	private Color moneyColor;
	
	//WorldReceiver
	private WorldReceiver worldReceiver;
	
	//WorldRenderer
	private WorldRenderer worldRenderer;
	
	//initializer
	private boolean init;
	
	//button
	private Button spawnButton[];

	
	public PlayState(GameStateManager gsm, ArrayList<GameState> gameStates) {
		this.gameStates = gameStates;
		init = false;
		moneyFont = new Font("Arial", Font.PLAIN, 28);
		moneyColor = Color.WHITE;
		spawnButton = new Button[4];
		spawnButton[0] = new Button(
				20,
				GamePanel.height-80,
				120,
				60,
				Color.LIGHT_GRAY,
				"ARCHER\n" + Archer.baseMoneyOnBuy + " $",
				Color.BLACK,
				new Font("Arial", Font.PLAIN, 15)
		);
		spawnButton[1] = new Button(
				20+120+20,
				GamePanel.height-80,
				120,
				60,
				Color.LIGHT_GRAY,
				"PIKER\n" + Piker.baseMoneyOnBuy + " $",
				Color.BLACK,
				new Font("Arial", Font.PLAIN, 15)
		);
		spawnButton[2] = new Button(
				20+120+20+120+20,
				GamePanel.height-80,
				120,
				60,
				Color.LIGHT_GRAY,
				"SOLDIER\n" + Soldier.baseMoneyOnBuy + " $",
				Color.BLACK,
				new Font("Arial", Font.PLAIN, 15)
		);
		spawnButton[3] = new Button(
				20+120+20+120+20+120+20,
				GamePanel.height-80,
				120,
				60,
				Color.LIGHT_GRAY,
				"CHAMPION\n" + Champion.baseMoneyOnBuy + " $",
				Color.BLACK,
				new Font("Arial", Font.PLAIN, 15)
		);
	}
	
	public boolean hasInitialized() {
		return init;
	}
	
	@Override
	public void init() {
		ConnectionState cs = (ConnectionState) gameStates.get(GameStateManager.CONNECTIONSTATE);
		try {
			serverIP = InetAddress.getByName(cs.getServerIP());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		cch = new ClientConnectionHandler();
		
		try {
			team = cch.connectToServer(serverIP);
		} catch (IOException e) {
			e.printStackTrace();
			//imposta schermata errore eccetera eccetera
		}
		
		try {
			worldReceiver = new WorldReceiver(cch.getStateInput());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		worldRenderer = new WorldRenderer(GamePanel.width, GamePanel.height);
		
		worldReceiver.start();
		
		init = true;
	}

	@Override
	public void update() {
		if(hasInitialized()) {
			//Switch to the new world
			if(worldReceiver.hasWorldChange()) {
				worldRenderer.switchWorld(worldReceiver.getWorld());
			}
			if(worldRenderer.getWorld() != null){
				money = Integer.toString((int)worldRenderer.getWorld().getPlayerInfo(team).getMoney()) + " $";

				if(team == Team.BLUE) {
					md = MovementDirection.RIGHT;
					spp = 0;
				}else {
					md = MovementDirection.LEFT;
					spp = worldRenderer.getWorld().getWorldInfo().getWorldWidth();
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if(worldRenderer != null) {
			worldRenderer.render(g, 0);
	
			//renderizzo i soldi
			if(money != null) {
				GlyphVector mgv = moneyFont.createGlyphVector(g.getFontRenderContext(), money);
				Rectangle2D mtextBounds = mgv.getPixelBounds(null, 0, 0);
				g.setFont(moneyFont);
				g.setColor(moneyColor);
				g.drawString(money, 10, (int)mtextBounds.getHeight());
				
				//inizio a renderizzare i bottoni qunado il gioco si è avviato
				for (Button currentButton : spawnButton) {
					currentButton.draw(g);
				}
				//renderizzo la scritta del team
				if(worldRenderer.getWorld().getTimeOffset()<2000) {
					String text;
					if(team == Team.BLUE) {
						g.setColor(Color.BLUE);
						text = "You are BLUE";
					}else {
						g.setColor(Color.RED);
						text = "You are RED";
					}
					Font teamFont = new Font("Century Gothic", Font.PLAIN, 48);
					g.setFont(teamFont);
					GlyphVector gv = teamFont.createGlyphVector(g.getFontRenderContext(), text);
					Rectangle2D textBounds = gv.getPixelBounds(null, 0, 0);
					g.drawString(text, (int) (GamePanel.width/2-textBounds.getWidth()/2), (int) (20 + textBounds.getHeight()));
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(spawnButton[0].mouseClicked(arg0)) {
			BuyAndSpawnEntityCommand archer = new BuyAndSpawnEntityCommand(new Archer(team, spp, md));
			try {
				CommandSerializationHelper.serializeCommand(archer, cch.getCommandOutput());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(spawnButton[1].mouseClicked(arg0)) {
			BuyAndSpawnEntityCommand piker = new BuyAndSpawnEntityCommand(new Piker(team, spp, md));
			try {
				CommandSerializationHelper.serializeCommand(piker, cch.getCommandOutput());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(spawnButton[2].mouseClicked(arg0)) {
			BuyAndSpawnEntityCommand soldier = new BuyAndSpawnEntityCommand(new Soldier(team, spp, md));
			try {
				CommandSerializationHelper.serializeCommand(soldier, cch.getCommandOutput());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(spawnButton[3].mouseClicked(arg0)) {
			BuyAndSpawnEntityCommand champion = new BuyAndSpawnEntityCommand(new Champion(team, spp, md));
			try {
				CommandSerializationHelper.serializeCommand(champion, cch.getCommandOutput());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {
		for (Button currentButton : spawnButton) {
			currentButton.mousePressed(arg0);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		for (Button currentButton : spawnButton) {
			currentButton.mouseReleased(arg0);;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}
