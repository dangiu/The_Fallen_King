package core.entities;

/**
 * Entita' in grado di muoversi
 * 
 * @author Willi Menapace
 *
 */
public interface MobileEntity extends Entity {
	/**
	 * Muove l'entita' nella posizione che deve occupare' trascorso il tempo specificato
	 * 
	 * @param time Il tempo in ms alla fine del quale si vuole sapere la posizione dell'entita'
	 */
	public void move(int time);
	/**
	 * 
	 * @return La direzione in cui l'entita' si muovera' al prossimo spostamento
	 */
	public MovementDirection getMovementDirection();
}
