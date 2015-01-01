package core;

/**
 * Comando in grado di modificare lo stato del mondo
 * 
 * @author Willi Menapace
 *
 */
public interface Command {
	/**
	 * Esegue il comando che modifica lo stato del mondo
	 * 
	 * @param world Lo stato del mondo da modificare
	 */
	public void execute(World world);
	
}
