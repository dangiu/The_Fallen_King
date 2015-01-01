package core.entities;

import core.World;

/**
 * Entita' che puo' infliggere danni
 * 
 * @author Willi Menapace
 *
 */
public interface AttackerEntity extends RangedEntity {
	/**
	 * 
	 * @return Il numero di punti attacco dell'elemento
	 */
	public int getAttack();
	/**
	 * 
	 * @return Il numero di ms che intercorrono normalmente tra un attacco e l'altro
	 */
	public int getAttackCooldown();
	/**
	 * Simula gli attacchi dell'entita' all'interno del mondo da svolgersi entro il tempo specificato
	 * 
	 * @param world Il mondo in cui avviene la simulazione
	 * @param time Il tempo in ms che l'unita' ha a disposizione per attaccare
	 * @return true se l'unita' ha attaccato nel periodo specificato
	 */
	public boolean evalAndAttack(World world, int time);
}
