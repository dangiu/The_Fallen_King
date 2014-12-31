package core;

/**
 * Classe che fornisce metodi di utilita' per implementare attaccanti che effettuano
 * il loro attacco spawnando un'entita' (es frecce, magie, ecc)
 * 
 * @author Willi Menapace
 *
 */
abstract class SpawnerAttacker extends RangeFinderAttacker {

	SpawnerAttacker(Team team, double xSpawnPosition, double width, double height) {
		super(team, xSpawnPosition, 0, width, height);
	}
	
	/**
	 * Ritorna l'entita' da spawnare per attaccare l'entita' specificata
	 * 
	 * @param damageableEntity il nemico trovato in range di attacco
	 * @return L'entita' da spawnare nel turno di attacco corrente
	 */
	abstract Entity getSpawnedEntity(DamageableEntity damageableEntity);
	
	@Override
	boolean attack(World world, DamageableEntity damageableEntity) {

		world.addDeferredEntity(getSpawnedEntity(damageableEntity));
		
		//Si spawna una sola entita' per ogni attacco
		return true;
	}
	
}
