package graphic;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import core.World;
import core.entities.Entity;
import core.entities.MobileEntity;
import core.entities.MovementDirection;

/**
 * Classe che contiene tutte le informazioni per eseguire il rendering di uno stato del mondo
 * 
 * @author Daniele Giuliani
 */
public class WorldRenderer {
	
	private World world;
	private TextureProvider textureMap;
	private SizeConverter sizeConverter;
	
	public WorldRenderer(World pWorld, int windowPixelWidth) {
		world = pWorld;
		sizeConverter = new SizeConverter(world.getWorldInfo().getWorldWidth(), windowPixelWidth);
		textureMap = new TextureProvider();
	}
	
	public void render(Graphics g, long timeOffset) {
		g.clearRect(0, 0, 820, 800 / 16 * 9);
		Iterator<Entity> entities = world.getEntityIterator();
		while(entities.hasNext()) {
			Entity currentEntity = entities.next();
			BufferedImage currentTexture = textureMap.getByClass(currentEntity.getClass(),	timeOffset);
			
			if(currentEntity instanceof MobileEntity) {
				MobileEntity currentMobileEntity = (MobileEntity) currentEntity;
				if(currentMobileEntity.getMovementDirection() == MovementDirection.RIGHT) {
					currentTexture = textureMap.flipTextureHorizzontaly(currentTexture);
				}
			}
			
			g.drawImage(
					currentTexture,
					sizeConverter.toPixels(currentEntity.getBox().getX()),
					sizeConverter.toPixels(currentEntity.getBox().getY()),
					sizeConverter.toPixels(currentEntity.getBox().getWidth()),
					sizeConverter.toPixels(currentEntity.getBox().getHeight()),
					null);
		}
	}
	
	public void switchWorld(World pWorld) {
		world = pWorld;
	}
}
