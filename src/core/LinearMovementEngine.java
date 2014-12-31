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
	private MovementDirection direction;
	
	/**
	 * 
	 * @param speed La velocita' di spostamento lineare lungo l'asse X in metri al secondo
	 */
	public LinearMovementEngine(double speed, MovementDirection direction) {
		this.speed = speed;
		this.direction = direction;
	}
	
	@Override
	public Rectangle2D.Double getNewPosition(Rectangle2D initialPosition, int time) {

		
		double orientedSpeed = speed;
		//Inverte la velocita' se l'unita' deve muoversi verso sinistra
		if(direction == MovementDirection.LEFT) {
			orientedSpeed = -speed;
		}
		
		Rectangle2D.Double newPosition = new Rectangle2D.Double(initialPosition.getX() + orientedSpeed * time / 1000,
											  		   initialPosition.getY(),
											  		   initialPosition.getWidth(),
											  		   initialPosition.getHeight());
		return newPosition;
	}
	
	@Override
	public MovementDirection getDirection() {
		return direction;
	}

}
