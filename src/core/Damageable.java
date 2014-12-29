package core;

/**
 * Entita' che puo' subire danni
 * 
 * @author Willi Menapace
 *
 */
public interface Damageable extends Entity {
    /**
     * Il numero di Hit Points rimanenti. Hp <= 0 implica la distruzione dell'elemento
     * 
     * @return Il numero di Hit Points rimanenti all'elemento
     */
	public int getHp();
	/**
	 * 
	 * @return Il numero di Hit Points massimi consentiti all'elemento
	 */
    public int getMaxHP();
}
