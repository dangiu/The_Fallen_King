package graphic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import javax.imageio.ImageIO;

import core.entities.Archer;
import core.entities.Champion;
import core.entities.Piker;
import core.entities.Soldier;

public class TextureProvider {
	
	private BufferedImage walkTexture1;
	private BufferedImage walkTexture2;
	private static Map<Class<?>, TextureProvider> textureMap;
	
	private TextureProvider(String pathWalkTexture1, String pathWalkTexture2) {
		//walk1
		try {
			walkTexture1 = ImageIO.read(new File(getClass().getResource(pathWalkTexture1).toURI()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		//walk2
		try {
			walkTexture2 = ImageIO.read(new File(getClass().getResource(pathWalkTexture2).toURI()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	static {
		TextureProvider archer = new TextureProvider("/resources/textures/archer1.png", "/resources/textures/archer1.png");
		TextureProvider chamption = new TextureProvider("/resources/textures/chamption1.png", "/resources/textures/chamption1.png");
		//le texture di piker sono nominate sbagliate!!!! PIKER --> PICKER
		TextureProvider piker = new TextureProvider("/resources/textures/picker1.png", "/resources/textures/picker1.png");
		TextureProvider soldier = new TextureProvider("/resources/textures/soldier1.png", "/resources/textures/soldier1.png");
		
		textureMap.put(Archer.class, archer);
		textureMap.put(Champion.class, chamption);
		textureMap.put(Piker.class, piker);
		textureMap.put(Soldier.class, soldier);
	}
	
	public BufferedImage getWalkTexture(long timeOffset) {
		long textureToReturn = timeOffset%2;
		if(textureToReturn < 1) {
			return walkTexture1;
		}else{
			return walkTexture2;
		}
	}
	
	public BufferedImage getByClass(Class<?> EntityType, long timeOffset) {
		return textureMap.get(EntityType).getWalkTexture(timeOffset);
	}
}
