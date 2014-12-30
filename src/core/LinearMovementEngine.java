package core;

import java.awt.geom.Rectangle2D;

/**
 * Motore di movimento che muove la posizione lungo l'asse X con velocita' costante
 * 
 * @author Willi Menapace
 *
 */
public class LinearMovementEngine implements MovementEngine {

	private double speed;
	
	/**
	 * 
	 * @param speed La velocita' di spostamento lineare lungo l'asse X in metri al secondo
	 */
	public LinearMovementEngine(double speed) {
		this.speed = speed;
	}
	
	@Override
	public Rectangle2D getNewPosition(Rectangle2D initialPosition, int time) {

		Rectangle2D newPosition = new Rectangle2D.Double(initialPosition.getX() + speed * time / 1000,
											  		   initialPosition.getY(),
											  		   initialPosition.getWidth(),
											  		   initialPosition.getHeight());
		return newPosition;
	}

}
