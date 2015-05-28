package graphic;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class StaticTexture implements Texture{
	private BufferedImage texture;
	
	public StaticTexture(String path) {
		try {
			texture = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	

	@Override
	public BufferedImage getTexture(long timeOffset) {
		return texture;
	}
}
