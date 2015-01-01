package core.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import core.PlayerInfo;
import core.Team;
import core.World;
import core.WorldInfo;
import core.entities.Archer;
import core.entities.DamageableEntity;
import core.entities.MovementDirection;
import core.entities.Soldier;

/**
 * Effettua test sul movimento di entita' e sull'attacco
 * 
 * @author Willi Menapace
 *
 */
public class AttackTest {

	private static final int WORLD_WIDTH = 100;
	
	private WorldInfo worldInfo;
	
	//Il mondo in cui si effettua la simulazione
	private World world;
	
	/**
	 * Inizializza il mondo per prepararlo alla simulazione
	 */
	private void init() {
		worldInfo = new WorldInfo(WORLD_WIDTH);
		Map<Team, PlayerInfo> playerInfo = new HashMap<>();
		
		playerInfo.put(Team.BLUE, new PlayerInfo(100));
		playerInfo.put(Team.RED, new PlayerInfo(100));
		
		world = new World(worldInfo, playerInfo);
	}
	
	/**
	 * Simula lo scontro di due unita' in un periodo di tempo lungo
	 * Il test verifica che almeno una di esse venga uccisa nello scontro
	 */
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
