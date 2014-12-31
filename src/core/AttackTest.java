package core;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class AttackTest {

	private static final int WORLD_WIDTH = 100;
	
	private WorldInfo worldInfo;
	
	private World world;
	
	private void init() {
		worldInfo = new WorldInfo(WORLD_WIDTH);
		Map<Team, PlayerInfo> playerInfo = new HashMap<>();
		
		playerInfo.put(Team.BLUE, new PlayerInfo(100, 5));
		playerInfo.put(Team.RED, new PlayerInfo(100, 5));
		
		world = new World(worldInfo, playerInfo);
	}
	
	@Test
	public void basicAttack() {
		init();
		DamageableEntity blu = new Soldier(Team.BLUE, 0, MovementDirection.RIGHT);
		DamageableEntity red = new Archer(Team.RED, 10, MovementDirection.LEFT);
		
		world.addEntity(blu);
		world.addEntity(red);
		
		for(int i = 0; i < 500; ++i) {
			world.simulate(200);
			System.out.println("BLU: " + blu.getBox().getCenterX() + " " + blu.getHp() + " : RED " + red.getBox().getCenterX() + " " + red.getHp());
		}
		
		System.out.println(blu.getHp());
		Assert.assertTrue(blu.isDead() || red.isDead());
		
		
		
	}

}
