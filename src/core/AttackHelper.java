package core;

import java.awt.geom.Rectangle2D;

/**
 * Classe che fornisce metodi di utilita' per gestire gli attacchi
 * 
 * @author Willi Menapace
 *
 */
final class AttackHelper {
	
	/**
	 * Determina se l'unita' attaccata e' nel range dell'unita' attaccante oppure se hanno colliso
	 * 
	 * @param attacker L'unita' attaccante
	 * @param attacked L'unita' attaccata
	 * @return true se l'attaccante ha colliso con l'unita' attaccata o se e' in range
	 */
	static boolean isInRange(RangedEntity attacker, Entity attacked) {
		
		Rectangle2D attackerPosition = attacker.getBox();
		Rectangle2D attackedPosition = attacked.getBox();
		
		if(entitiesCollided(attackerPosition, attackedPosition)) {
			return true;
		}
		
		double xDifference = Math.abs(attackerPosition.getCenterX() - attackedPosition.getCenterX());
		double yDifference = Math.abs(attackerPosition.getCenterY() - attackedPosition.getCenterY());
		
		if(xDifference + yDifference <= attacker.getRange()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determina se due unita' hanno effettuato una collisione
	 * 
	 * @param firstPosition La posizione della prima entita' coinvolta
	 * @param secondPosition La posizione della seconda entita' coinvolta
	 * @return true se le unita' hanno colliso
	 */
	static private boolean entitiesCollided(Rectangle2D firstPosition, Rectangle2D secondPosition) {
		return firstPosition.intersects(secondPosition);
	}
	
	/**
	 * Calcola il danno normale che viene effettuato da un attaccante a un difensore
	 * Non si tiene condo di capacita' di evitare attacchi oppure di attacchi critici
	 * 
	 * @param attacker L'unita' attaccante
	 * @param attacked L'unita' attaccata
	 * @return Il danno effettuato in condizioni normali dall'unita' attaccante all'attaccata
	 */
	static int calculateNormalDamageDealt(AttackerEntity attacker, DamageableEntity attacked) {
		//La difesa e' incrementata di 1 per evitare che le entita' con difesa 0 subiscano danno infinito
		return (int) (attacker.getAttack() * attacker.getAttack() / (attacked.getDefense() + 1));
	}
}
