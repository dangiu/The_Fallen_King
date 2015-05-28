package graphic;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Background {
	//image
	private BufferedImage texture;
	
	//lastTime
	private long classInit;
	
	//parameters
	private int initX;
	private int initY;
	private int width;
	private int height;
	
	//position
	private int x;
	private int y;
	
	//shut up willi! pixel at millisec
	private double dx;
	private double dy;
	
	
	public Background(BufferedImage texture, int x, int y, int width, int height, double dx, double dy)  {
		this.texture = texture;
		this.initX = x;
		this.initY = y;
		this.width = width;
		this.height = height;
		this.dx = dx;
		this.dy = dy;
		classInit = System.currentTimeMillis();
	}
	
	
	public void update() {
		long offset = classInit - System.currentTimeMillis();
		x = (int) (offset * dx) + initX;
		y = (int) (offset * dy) + initY;
		
	}
	
	public void draw(Graphics2D g) {
		//modificare per disegnare l'immagine dall'altra parte dello schermo
		
		g.drawImage(texture, x, y, width, height, null);
	}
}
