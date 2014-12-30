package core;

/**
 * Entita' che puo' subire danni
 * 
 * @author Willi Menapace
 *
 */
public interface DamageableEntity extends Entity {
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
    /**
     * Infligge un danno all'entita'
     * 
     * @param damage Il danno da infliggere all'elemento
     */
    public void inflictDamage(int damage);
    /**
     * 
     * @return true se l'elemento e' da considerarsi morto
     */
    public boolean isDead();
    /**
     * 
     * @return Il numero di punti difesa dell'elemento
     */
    public int getDefense();
}
