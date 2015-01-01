package core;

import core.entities.Entity;

/**
 * Comando che inserisce una certa entita' nello stato del mondo specificato
 * 
 * @author Willi Menapace
 */
public class SpawnEntityCommand implements Command {
	
	Entity entityToSpawn;
	
	/**
	 * Inserisce immediatamente all'interno del mondo in cui e' eseguito l'entita' specificata senza che venga comprata.
	 * Non puo' essere eseguito mentre una simulazione del mondo e' in corso
	 * @param entity L'entita' da inserire nel mondo. Deve essere != null
	 */
	public SpawnEntityCommand(Entity entity) {
		this.entityToSpawn = entity;
	}
	
	@Override
	public void execute(World world) {
		world.addEntity(entityToSpawn);
	}
}
