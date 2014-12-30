package core;

import java.awt.geom.Rectangle2D;

/**
 *
 * Rappresenta un elemento che si evolve dinamicamente durante una partita 
 * 
 * @author Willi Menapace
 *
 */
public interface Entity {
	/**
	 * @return rettangolo rappresentante la posizione e le dimensioni logiche dell'elemento
	 */
	public Rectangle2D getBox();
	/**
	 * @return il team che ha generato l'elemento
	 */
	public Team getTeam();
	/**
	 * Evolve del periodo di tempo specificato all'interno del mondo
	 * 
	 * @param time Il tempo in ms di cui l'entita' deve evolvere. Deve esserre > 0
	 * @param world Il contesto all'interno del quale l'entita' deve evolvere. Deve essere != null
	 */
	public void evolve(long time, World world);
	
}
