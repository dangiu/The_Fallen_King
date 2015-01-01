package core.entities;

import java.awt.geom.Rectangle2D;

/**
 * Classe in grado di calcolare una posizione dopo un certo intervallo di tempo
 * 
 * @author Willi Menapace
 *
 */
public interface MovementEngine {
	/**
	 * Calcola la nuova posizione trascorso un certo lasso di tempo
	 * 
	 * @param initialPosition La posizione iniziale dell'elemento di cui si vuole calcolare lo spostamento
	 * @param time Il tempo in ms trascorso il quale si vuole sapere al nuova posizione
	 * @return La nuova posizione calcolata dell'elemento dopo il tempo specificato
	 */
	public Rectangle2D.Double getNewPosition(Rectangle2D initialPosition, int time);
	
	/**
	 * 
	 * @return la direzione in cui avviene lo spostamento
	 */
	public MovementDirection getDirection();
}
