package core;

/**
 * Attaccante che attacca direttamente tutte le unita' in range colpendole simultaneamente
 * 
 * @author Willi Menapace
 */
abstract class PhysicalAttacker extends RangeFinderAttacker {
	
	@Override
	boolean attack(World world, DamageableEntity damageableEntity) {

		int damageDealt = AttackHelper.calculateNormalDamageDealt(this, damageableEntity);
		damageableEntity.inflictDamage(damageDealt);
		
		//Si attaccano tutte le unita' in range con un singolo attacco
		return true;
	}
	
}
