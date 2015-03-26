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
 * 
 */
public class WorldRenderer {
	
	private World world;
	private TextureProvider textureMap;
	private SizeConverter sizeConverter;
	private int windowPixelWidth;
	private int windowPixelHeight;
	
	public WorldRenderer(World pWorld, int pWindowPixelWidth, int pWindowPixelHeight) {
		windowPixelWidth = pWindowPixelWidth;
		windowPixelHeight = pWindowPixelHeight;
		world = pWorld;
		sizeConverter = new SizeConverter(world.getWorldInfo().getWorldWidth(), windowPixelWidth);
		textureMap = new TextureProvider();
	}
	
	/**
	 * Metodo che renderizza un singolo frame del gioco
	 * @param g l'oggetto utilizzato per disegnare
	 * @param timeOffset il tempo di gioco
	 */
	public void render(Graphics g, long timeOffset) {
		g.clearRect(0, 0, windowPixelWidth + 10, windowPixelHeight + 10);//pulire l'intera finestra +10 serve perche' altrimenti non copre alcuni pixel della finestra
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
