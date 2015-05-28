package graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Comonente grafico che funziona da textbox dove è possibile inserire del testo.
 * @author Th3W4r70cK
 *
 */
public class Textbox {
	
	private String text;
	private Rectangle2D rect;
	private boolean focus;
	private Font textFont;
	
	public Textbox(int x, int y, int width, int height) {
		text = "";
		rect = new Rectangle2D.Double(x, y, width, height);
		focus = false;
		int fontSize = (int) (6 / 7.0 * height - (3 / 10.0 * height)); //FORMULA TO CALCULATE FONT HEIGHT
		textFont = new Font("Arial", Font.PLAIN, fontSize);
	}
	
	public boolean getFocus() {
		return focus;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void mouseClicked(MouseEvent e) {
		if(rect.contains(e.getX(), e.getY())) {
			focus = true;
		}else {
			focus = false;
		}
	}
	
	/**
	 * Metodo che gestisce la pressione dei pulsanti e la modifica del testo nella textbox
	 * @param e
	 */
	public void keyTyped(KeyEvent e) {
		if(focus) {
			if((int)e.getKeyChar() == KeyEvent.VK_BACK_SPACE){
				if(text.length()>0){
					text = text.substring(0, text.length() - 1);
				}
			}else {
				text = text + e.getKeyChar();
			}
		}
	}
	
	public void update() {
		//useless method
	}
	
	public void draw(Graphics2D g) {
		
		//BACKGROUND OF THE TEXTBOX
		g.setColor(Color.BLACK);
		//draw rectangle
		g.fill(rect);
		
		
		//STRING INSIDE THE TEXTBOX
		g.setColor(Color.WHITE);
		g.setFont(textFont);
		FontMetrics fm = g.getFontMetrics();
		
		//resize the string to fit in the textbox
		String textToWrite = text;
		while(fm.stringWidth(textToWrite) >= rect.getWidth() - 2) {// -2 becouse the rectangle is not perfect
			textToWrite = textToWrite.substring(1);
		}
		//draw string
		g.drawString(textToWrite,  (int) (rect.getMinX() + rect.getHeight()*0.1), (int) (rect.getCenterY() + fm.getHeight() / 2));
		
		//BORDER OF THE TEXTBOX
		if(focus) {
			g.setColor(Color.RED);
		}else {
			g.setColor(Color.WHITE);
		}
		//draw rectangle
		g.draw(rect);
	}
}
