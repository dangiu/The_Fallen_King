package graphic.test;

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
import core.entities.DamageableEntity;
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
	private static int SCALE = 1;
	
	public RenderTest() {
		Dimension windowSize = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(windowSize);
	}
	
	public void start() {
		isRunning = true;
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				final int WORLD_WIDTH = 50;
				WorldInfo worldInfo = new WorldInfo(WORLD_WIDTH);
				Map<Team, PlayerInfo> playerInfo = new HashMap<>();
				playerInfo.put(Team.BLUE, new PlayerInfo(100));
				playerInfo.put(Team.RED, new PlayerInfo(100));
				
				World world = new World(worldInfo, playerInfo);
				
				DamageableEntity blu = new Soldier(Team.BLUE, 48, MovementDirection.LEFT);
				DamageableEntity blu1 = new Archer(Team.BLUE, 49, MovementDirection.LEFT);
				DamageableEntity blu2 = new Soldier(Team.BLUE, 47, MovementDirection.LEFT);
				DamageableEntity blu3 = new Soldier(Team.BLUE, 47, MovementDirection.LEFT);
				DamageableEntity red = new Soldier(Team.RED, 2, MovementDirection.RIGHT);
				DamageableEntity red1 = new Archer(Team.RED, 1, MovementDirection.RIGHT);
				DamageableEntity red2 = new Archer(Team.RED, 1, MovementDirection.RIGHT);
				
				world.addEntity(blu);
				world.addEntity(red);
				world.addEntity(blu1);
				world.addEntity(red2);
				world.addEntity(blu2);
				world.addEntity(blu3);
				world.addEntity(red1);
				while(isRunning) {
					world.simulate(10);
					long init = System.nanoTime();
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
		
		WorldRenderer renderer = new WorldRenderer(world, WIDTH);
		long init = System.nanoTime();
		renderer.render(g, 0);
		long end = System.nanoTime();
		System.out.println((end-init)/1000000);
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
