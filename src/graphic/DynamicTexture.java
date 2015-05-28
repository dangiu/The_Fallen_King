package graphic;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class DynamicTexture implements Texture{
	private ArrayList<BufferedImage> textureArray;
	private int textureSwitchTime = 250;
	
	/**
	 * Costruttore che specifica "manualmente" il tempo di switch delle texture ed i percorsi
	 * @param textureSwitchTime
	 * @param paths
	 */
	public DynamicTexture(String...paths) {
		textureArray = new ArrayList<>();
		for (String path : paths) {
			BufferedImage texture = null;
			try {
				texture = ImageIO.read(getClass().getResourceAsStream(path));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			textureArray.add(texture);
		}
	}
	
	/**
	 * Costruttore che specifica "manualmente" il tempo di switch delle texture ed i percorsi
	 * @param textureSwitchTime
	 * @param paths
	 */
	public DynamicTexture(int textureSwitchTime, String...paths) {
		textureArray = new ArrayList<>();
		for (String path : paths) {
			BufferedImage texture = null;
			try {
				texture = ImageIO.read(getClass().getResourceAsStream(path));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			textureArray.add(texture);
		}
		this.textureSwitchTime = textureSwitchTime;
	}
	
	/**
	 * Ritorna la texture appropriata in base ai tick del server
	 * @param timeOffset
	 * @return
	 */
	public BufferedImage getTexture(long timeOffset) {
		int textureIndex = (int) (timeOffset % (textureArray.size() * textureSwitchTime)); // tempo per ogni texture
		textureIndex = textureIndex / textureSwitchTime; // indice della texture per il tempo passato
		return textureArray.get(textureIndex);
	}
}
