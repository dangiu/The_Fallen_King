package graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import core.World;
import core.entities.Castle;
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
	private TextureManager textureMap;
	private SizeConverter sizeConverter;
	private int windowPixelWidth;
	private int windowPixelHeight;
	
	//altezza del mondo in pixel
	private int worldHeight;
	
	//castelli che vanno disegnati per ultimi separatamente
	private Entity castle1;
	private Entity castle2;
	
	//loading
	private Font loadingFont;
	private Color loadingColor;
	
	public WorldRenderer(int pWindowPixelWidth, int pWindowPixelHeight) {
		windowPixelWidth = pWindowPixelWidth;
		windowPixelHeight = pWindowPixelHeight;		
		textureMap = new TextureManager();
		worldHeight = windowPixelHeight*2/3;
		loadingFont = new Font("Century Gothic", Font.PLAIN, 30);
		loadingColor = Color.BLACK;
	}
	
	/**
	 * Metodo che renderizza un singolo frame del gioco
	 * @param g l'oggetto utilizzato per disegnare
	 * @param timeOffset il tempo di gioco
	 */
	public void render(Graphics g, long timeOffset) {
		if(this.world == null) {
			//se il mondo non esiste coloro tutto il pannello di grigio
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, windowPixelWidth, windowPixelHeight);
			String message = "Waiting for your opponent to connect";
			g.setColor(loadingColor);
			g.setFont(loadingFont);
			FontMetrics fm = g.getFontMetrics();
			Rectangle2D bounds = fm.getStringBounds(message, g);
			g.drawString(message, (int)(windowPixelWidth/2-bounds.getWidth()/2), (int)(windowPixelHeight/2-bounds.getHeight()/2));
		}else {
			g.setColor(new Color(22, 102, 177));
			g.fillRect(0, 0, windowPixelWidth, windowPixelHeight);
			Iterator<Entity> entities = world.getEntityIterator();
			
			//disegno il cielo
			g.drawImage(TextureManager.sky.getTexture(0), 0, 0, windowPixelWidth, worldHeight, null);
			//scorriamo la lista di entità
			while(entities.hasNext()) {
				Entity currentEntity = entities.next();
				//memorizzo i castelli
				if(currentEntity instanceof Castle) {
					if(castle1 == null) {
						castle1 = currentEntity;
					}else {
						castle2 = currentEntity;
					}
				}else{
					
					//ottengo la texture giusta in base ai tick del server
					BufferedImage currentTexture = textureMap.getByClass(currentEntity.getClass(),	world.getTimeOffset());
					
					//aggiusto la texture specchiandola se il personaggio si deve muovere verso sinistra
					if(currentEntity instanceof MobileEntity) {
						MobileEntity currentMobileEntity = (MobileEntity) currentEntity;
						if(currentMobileEntity.getMovementDirection() == MovementDirection.LEFT) {
							currentTexture = TextureManager.flipTextureHorizzontaly(currentTexture);
						}
					}
					
					g.drawImage(
							currentTexture,
							sizeConverter.toPixels(currentEntity.getBox().getX()),
							worldHeight-sizeConverter.toPixels(currentEntity.getBox().getHeight() + currentEntity.getBox().getY()),
							sizeConverter.toPixels(currentEntity.getBox().getWidth()),
							sizeConverter.toPixels(currentEntity.getBox().getHeight()),
							null
					);
				}
				
			}
			//disegnare i castelli e il resto
			BufferedImage castleTexture = textureMap.getByClass(castle1.getClass(), world.getTimeOffset());
			if(castle1 != null) {
				g.drawImage(
						castleTexture,
						sizeConverter.toPixels(castle1.getBox().getX()),
						worldHeight-sizeConverter.toPixels(castle1.getBox().getHeight()),
						sizeConverter.toPixels(castle1.getBox().getWidth()),
						sizeConverter.toPixels(castle1.getBox().getHeight()),
						null
				);
			}
			if(castle2 != null) {
				g.drawImage(
						castleTexture,
						sizeConverter.toPixels(castle2.getBox().getX()),
						worldHeight-sizeConverter.toPixels(castle2.getBox().getHeight()),
						sizeConverter.toPixels(castle2.getBox().getWidth()),
						sizeConverter.toPixels(castle2.getBox().getHeight()),
						null
				);
			}
			//cancello i castelli precedenti
			castle1 = null;
			castle2 = null;
			
			//disegno il terreno
			g.drawImage(TextureManager.terrain.getTexture(0), 0, worldHeight, windowPixelWidth, windowPixelHeight, null);
		}
	}
	
	public void switchWorld(World pWorld) {
		if(world == null) {
			sizeConverter = new SizeConverter(pWorld.getWorldInfo().getWorldWidth(), windowPixelWidth);
		}
		world = pWorld;
	}
	
	public World getWorld() {
		return world;
	}
}
