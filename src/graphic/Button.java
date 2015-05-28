package graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

/**
 * Classe generica che permette di creare dei bottoni.
 * @author Th3W4r70cK
 *
 */
public class Button {
	private Rectangle2D.Double rect;
	private Color bgColor;
	private String text;
	private Font textFont;
	private Color textColor;
	private boolean pressed;
	
	public Button(int x, int y, int width, int height, Color bgColor, String text, Color textColor, Font textFont) {
		
		rect = new Rectangle2D.Double(x, y, width, height);
		this.bgColor = bgColor;
		
		this.text = text;
		this.textFont = textFont;
		this.textColor = textColor;
		
		pressed = false;
	}
	
	public boolean mouseClicked(MouseEvent e) {
		if(rect.contains(e.getX(), e.getY())) {
			return true;
		}
		return false;
	}

	public void mousePressed(MouseEvent e) {
		if(rect.contains(e.getX(), e.getY())) {
			pressed = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(rect.contains(e.getX(), e.getY())) {
			pressed = false;
		}
	}
	
	public void draw(Graphics2D g) {
		//DRAWING IMAGE
		
		if(!pressed) {
			//button not pressed
			g.setColor(bgColor);
			g.fill(rect);
		}else {
			//button pressed
			g.setColor(new Color(bgColor.getRed()/2, bgColor.getGreen()/2, bgColor.getBlue()/2));//darker color
			g.fill(rect);
		}
		
		g.setFont(textFont);
		g.setColor(textColor);
		
		String[] stringArray = text.split("\n");
		
		//calculating spacing
		double totalHeight = 0;
		for (int i = 0; i < stringArray.length; i++) {
			GlyphVector gv = textFont.createGlyphVector(g.getFontRenderContext(), stringArray[i]);
			Rectangle2D textBounds = gv.getPixelBounds(null, 0, 0);
			totalHeight += textBounds.getHeight();
		}
		
		//calculating interline
		double interline = (rect.height - totalHeight) / (stringArray.length + 1);
		
		//draw the string
		double progressiveHeight = rect.getY();
		for (int i = 0; i < stringArray.length; i++) {
			GlyphVector gv = textFont.createGlyphVector(g.getFontRenderContext(), stringArray[i]);
			Rectangle2D textBounds = gv.getPixelBounds(null, 0, 0);
			progressiveHeight += + interline;
			progressiveHeight += + textBounds.getHeight();
			g.drawString(stringArray[i], (int) (rect.getCenterX() - textBounds.getWidth()/2.0), (int) progressiveHeight);
		}
	}
}
