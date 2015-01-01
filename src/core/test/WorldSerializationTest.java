package core.test;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import core.PlayerInfo;
import core.Team;
import core.World;
import core.WorldInfo;
import core.entities.Archer;
import core.entities.Entity;
import core.entities.MovementDirection;
import core.entities.Soldier;

/**
 * Effettua test sulle capacita' di serializzazione e deserializzazione dello stato del mondo
 * 
 * @author Willi Menapace
 *
 */
public class WorldSerializationTest {

	private static final String FILE_PATH = "serialized_world.bin";
	
	private static final int WORLD_WIDTH = 100;
	
	private WorldInfo worldInfo;
	
	//Lo stato del mondo da serializzare
	private World world;
	
	/**
	 * Crea uno stato del mondo con informazioni di base
	 */
	private void init() {
		worldInfo = new WorldInfo(WORLD_WIDTH);
		Map<Team, PlayerInfo> playerInfo = new HashMap<>();
		
		playerInfo.put(Team.BLUE, new PlayerInfo(100));
		playerInfo.put(Team.RED, new PlayerInfo(200));
		
		world = new World(worldInfo, playerInfo);
	}
	
	/**
	 * Simula la serializzazione e la deserialzizazione di uno stato del mondo con una sola unita'
	 * Il test verifica che le informazioni riassuntive necessarie siano corrette e che le unita'
	 * deserializzate siano dello stesso tipo di quelle serializzate
	 */
	@Test
	public void testCorrectness() {
		init();
		
		Archer entity = new Archer(Team.BLUE, 0, MovementDirection.RIGHT);
		entity.setHp(entity.getMaxHp() / 2);
		world.addEntity(entity);
		
		DataOutputStream out = null;
		
		try {
			out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(FILE_PATH)));
			
			World.SerializationHelper.serialize(world, out);
			
			out.close();
		}  catch(Exception e) {
			e.printStackTrace();
			fail("Stream exception");
		} finally {
			try {
				if(out != null)
					out.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		DataInputStream in = null;
		
		try {
			
			in = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_PATH)));
			
			World deserializedWorld = World.SerializationHelper.deserialize(in);
			
			Assert.assertTrue(world.getWorldInfo().getWorldWidth() == deserializedWorld.getWorldInfo().getWorldWidth());
			
			for(Team team : Team.values()) {
				Assert.assertTrue(deserializedWorld.getPlayerInfo(team).getMoney() == world.getPlayerInfo(team).getMoney());
			}
			
			Archer deserializedEntity = (Archer) deserializedWorld.getEntityIterator().next();
			Assert.assertTrue(deserializedEntity.getClass().equals(entity.getClass()));
			Assert.assertTrue(entity.getHp() == deserializedEntity.getHp());
			Assert.assertTrue(entity.getMovementDirection() == deserializedEntity.getMovementDirection());
			Assert.assertTrue(entity.getTeam() == deserializedEntity.getTeam());
			
		} catch(Exception e) {
			e.printStackTrace();
			fail("Stream exception");
		} finally {
			try {
				if(in != null)
					in.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void testPerformance() {
		final int ENTITIES_TO_ADD = 100;
		init();
		
		for(int i = 0; i < ENTITIES_TO_ADD; ++i) {
			Entity entityToAdd = new Soldier(Team.BLUE, 50, MovementDirection.LEFT);
			world.addEntity(entityToAdd);
		}
		
		DataOutputStream out = null;
		
		try {
			out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(FILE_PATH)));

			long startTime = System.nanoTime();
			World.SerializationHelper.serialize(world, out);
			long endTime = System.nanoTime();
			System.out.println("Ms taken to serialize world with " + ENTITIES_TO_ADD + " entities: " + (endTime - startTime) / 1000000);
			
			out.close();
		}  catch(Exception e) {
			e.printStackTrace();
			fail("Stream exception");
		} finally {
			try {
				if(out != null)
					out.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		DataInputStream in = null;
		
		try {
			
			in = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_PATH)));
			
			long startTime = System.nanoTime();
			
			@SuppressWarnings("unused")
			World deserializedWorld = World.SerializationHelper.deserialize(in);
			long endTime = System.nanoTime();
			
			System.out.println("Ms taken to deserialize world with " + ENTITIES_TO_ADD + " entities: " + (endTime - startTime) / 1000000);
			
		} catch(Exception e) {
			e.printStackTrace();
			fail("Stream exception");
		} finally {
			try {
				if(in != null)
					in.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
