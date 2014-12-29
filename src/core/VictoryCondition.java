package core;

/**
 * Una condizione che se soddisfatta comporta la vittoria del giocatore a cui appartiene
 * 
 * @author Willi Menapace
 *
 */
public interface VictoryCondition {
	/**
	 * 
	 * @return true se il giocatore ha soddisfatto la condizione di vincita
	 */
	public boolean testVictory();
}
