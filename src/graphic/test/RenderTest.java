package graphic.test;

import graphic.Textbox;
import graphic.WorldRenderer;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.junit.Test;

import core.PlayerInfo;
import core.Team;
import core.World;
import core.WorldInfo;
import core.entities.Archer;
import core.entities.Champion;
import core.entities.MovementDirection;
import core.entities.Soldier;

public class RenderTest extends Canvas{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isRunning = false;
	private Thread thread;
	private static int WIDTH = 800;
	private static int HEIGHT = WIDTH / 16 * 9;
	private static double SCALE = 1;
	
	public RenderTest() {
		Dimension windowSize = new Dimension((int)(WIDTH * SCALE), (int)(HEIGHT * SCALE));
		setPreferredSize(windowSize);
	}
	
	public void start() {
		isRunning = true;
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				final int WORLD_WIDTH = 20;
				WorldInfo worldInfo = new WorldInfo(WORLD_WIDTH);
				Map<Team, PlayerInfo> playerInfo = new HashMap<>();
				playerInfo.put(Team.BLUE, new PlayerInfo(100));
				playerInfo.put(Team.RED, new PlayerInfo(100));
				
				World world = new World(worldInfo, playerInfo);
				
				world.addEntity(new Soldier(Team.BLUE, WORLD_WIDTH-2, MovementDirection.LEFT));
				world.addEntity(new Archer(Team.BLUE, WORLD_WIDTH-1, MovementDirection.LEFT));
				world.addEntity(new Soldier(Team.BLUE, WORLD_WIDTH-3, MovementDirection.LEFT));
				world.addEntity(new Soldier(Team.BLUE, WORLD_WIDTH-3, MovementDirection.LEFT));
				world.addEntity(new Soldier(Team.RED, 2, MovementDirection.RIGHT));
				world.addEntity(new Archer(Team.RED, 1, MovementDirection.RIGHT));
				world.addEntity(new Archer(Team.RED, 1, MovementDirection.RIGHT));
				world.addEntity(new Champion(Team.RED, -1, MovementDirection.RIGHT));
				world.addEntity(new Soldier(Team.BLUE, WORLD_WIDTH-2, MovementDirection.LEFT));
				world.addEntity(new Archer(Team.BLUE, WORLD_WIDTH-8, MovementDirection.LEFT));
				world.addEntity(new Soldier(Team.BLUE, WORLD_WIDTH-3, MovementDirection.LEFT));
				world.addEntity(new Archer(Team.RED, 4, MovementDirection.RIGHT));
				world.addEntity(new Archer(Team.RED, 1, MovementDirection.RIGHT));
				world.addEntity(new Champion(Team.BLUE, WORLD_WIDTH, MovementDirection.LEFT));
				
				
				
				while(isRunning) {
					long init = System.nanoTime();
					world.simulate(3);
					render(world);
					long end = System.nanoTime();
					System.out.println((end-init)/1000000);
				}
			}
			
		}, "Game");
		thread.start();
		
	}
	
	public void stop() {
		try {
			thread.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void render(World world) {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		
		WorldRenderer renderer = new WorldRenderer(WIDTH, HEIGHT);
		renderer.render(g, 0);
		
		Textbox test = new Textbox(100, 100, 150, 50);
		Textbox test2 = new Textbox(100, 160, 200, 70);
		test.draw(g);
		test2.draw(g);
		
		g.dispose();
		bs.show();
	}
	
	@Test
	public void test() {
		RenderTest test1 = new RenderTest();
		JFrame frame = new JFrame();
		
		frame.add(test1);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		
		test1.start();
		try {
			Thread.sleep(10000000);//per il debug
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
