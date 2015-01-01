package core.entities;

/**
 * Entita' che puo' essere comperata
 * 
 * @author Willi Menapace
 */
public interface BuyableEntity extends Entity {
	/**
	 * @return Il costo in denaro per comperare l'unta'
	 */
	public int getBaseCost();
}
