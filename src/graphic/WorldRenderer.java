package graphic;

import java.awt.Graphics;
import java.util.Iterator;

import core.World;
import core.entities.Entity;

/**
 * Classe che contiene tutte le informazioni per eseguire il rendering di uno stato del mondo
 * 
 * @author Daniele Giuliani
 */
public class WorldRenderer {
	
	private World world;
	
	public WorldRenderer(World pWorld) {
		world = pWorld;
	}
	
	public void render(Graphics g, long timeOffset) {
		Iterator<Entity> entities = world.getEntityIterator();
		entities.getClass();
	}
	
	public void switchWorld(World pWorld) {
		world = pWorld;
	}
}
