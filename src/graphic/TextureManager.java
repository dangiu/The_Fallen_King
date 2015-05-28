package graphic;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import core.entities.Archer;
import core.entities.BasicArrow;
import core.entities.Castle;
import core.entities.Champion;
import core.entities.Piker;
import core.entities.Soldier;

/**
 * Classe che contiene tutte le texture che vengono caricate all'avvio del gioco
 * @author Th3W4r70cK
 *
 */
public class TextureManager {
	private static HashMap<Class<?>, Texture> textureMap;
	public static StaticTexture menuBackground;
	public static StaticTexture castle;
	public static StaticTexture terrain;
	public static StaticTexture sky;
	public static StaticTexture cloud;
	
	static {
		textureMap = new HashMap<>();
		menuBackground = new StaticTexture("/textures/menubg.png");
		castle = new StaticTexture("/textures/castle.png");
		terrain = new StaticTexture("/textures/terrain.png");
		sky = new StaticTexture("/textures/sky.png");
		cloud = new StaticTexture("/textures/cloud.png");
		
		DynamicTexture archer = new DynamicTexture(
				"/textures/archer1.png",
				"/textures/archer2.png"
				);
		DynamicTexture champion = new DynamicTexture(
				"/textures/champion1.png",
				"/textures/champion2.png"
				);
		DynamicTexture piker = new DynamicTexture(
				"/textures/picker1.png",
				"/textures/picker2.png"
				);
		DynamicTexture soldier = new DynamicTexture(
				"/textures/soldier1.png",
				"/textures/soldier2.png"
				);
		DynamicTexture  arrow = new DynamicTexture(
				"/textures/arrow.png"
				);
		
		textureMap.put(Archer.class, archer);
		textureMap.put(Champion.class, champion);
		textureMap.put(Piker.class, piker);
		textureMap.put(Soldier.class, soldier);
		textureMap.put(BasicArrow.class, arrow);
		textureMap.put(Castle.class, castle);
		
	}
	
	public BufferedImage getByClass(Class<?> EntityType, long timeOffset) {
		return textureMap.get(EntityType).getTexture(timeOffset);
	}
	
	public static BufferedImage flipTextureHorizzontaly(BufferedImage pImage) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-pImage.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(pImage, null);
	}
	
	public static BufferedImage flipTextureVertically(BufferedImage pImage) {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -pImage.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(pImage, null);
	}
}
