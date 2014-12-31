package core;

/**
 * Attaccante che attacca direttamente tutte le unita' in range colpendole simultaneamente
 * 
 * @author Willi Menapace
 */
abstract class PhysicalAttacker extends RangeFinderAttacker {
	
	PhysicalAttacker(Team team, double xSpawnPosition, double width, double height) {
		super(team, xSpawnPosition, 0, width, height);
	}
	
	@Override
	boolean attack(World world, DamageableEntity damageableEntity) {

		int damageDealt = AttackHelper.calculateNormalDamageDealt(this, damageableEntity);
		damageableEntity.inflictDamage(damageDealt);
		
		//Si attaccano tutte le unita' in range con un singolo attacco
		return true;
	}
	
}
