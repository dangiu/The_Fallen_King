package core;

/**
 * Condizione che viene soddisfatta se l'entita' specificata muore
 *  
 * @author Willi Menapace
 *
 */
public class KillEntityCondition implements VictoryCondition {

	private Damageable entity;
	
	/**
	 * 
	 * @param entity L'entita' la cui uccisione soddisfa la condizione. Deve essere non null
	 */
	public KillEntityCondition(Damageable entity) {
		if(entity == null) {
			throw new NullPointerException("The entity to monitor must be non null");
		}
		
		this.entity = entity;
	}
	
	public boolean testVictory() {
		if(entity.getHp() > 0) {
			return false;
		} else {
			return true;
		}
	}
	
}
