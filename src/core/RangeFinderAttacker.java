package core;

import java.util.Iterator;

//TODO Sistemare i range usando i double perche' gli interi non sono abbastanza precisi

abstract class RangeFinderAttacker implements AttackerEntity {
	
	int currentCooldown = 0; //Il tempo che deve ancora trascorrere prima di poter attaccare nuovamente
	
	@Override
	public boolean evalAndAttack(World world, int time) {
		boolean attackedSomeone = false;
		while(true) {
			//C'e' il tempo per attaccare
			if(time >= currentCooldown) {
				time -= currentCooldown;
				attackedSomeone = findInRange(world);
				//Se non e' possibile colpire nessuno allora e' come se l'attacco non fosse stato
				//fatto (non si ripristina il cooldown) e non c'e' motivo di provare ad attaccare
				//nuovamente
				if(!attackedSomeone) {
					break;
				}
				currentCooldown = getAttackCooldown(); //Ripristina il cooldown per il prossimo attacco
			//Il tempo per attaccare e' terminato
			} else {
				currentCooldown -= time;
				break;
			}
		}
		return attackedSomeone;
	}
	
	/**
	 * Ricerca le unita' di altri team che si trovano in range e per ognuna richiama attack(...)
	 * 
	 * @param world
	 * @return true se e' stata attaccata almeno un'unita'
	 */
	private boolean findInRange(World world) {
		boolean attackedSomeone = false;
		Iterator<Entity> iterator = world.getEntityIterator();
		while(iterator.hasNext()) {
			Entity currentEntity = iterator.next();
			
			//Se l'unita' analizzata e' un nemico e se si puo' danneggiare
			if(currentEntity.getTeam() != this.getTeam() && currentEntity instanceof DamageableEntity) {
				DamageableEntity damageableEntity = (DamageableEntity) currentEntity;
				
				//Se il nemico e' in range
				if(AttackHelper.isInRange(this, damageableEntity)) {
					boolean noMoreAttacks = attack(world, damageableEntity);
					attackedSomeone = true;
					
					//Le unita' morte sono rimosse dal mondo
					if(damageableEntity.isDead()) {
						world.removeEntity(damageableEntity);
					}
					
					//La subclasse segnala che non vuole piu' attaccare in questo cooldown
					if(noMoreAttacks) {
						break;
					}
				}
			}
		}
		return attackedSomeone;
	}
	
	/**
	 * Richiamato allo scadere di ogni cooldown per ogni unita' di diversi team in range.
	 * Se damageableEntity.isDead() == true durante l'attacco damageableEntity non deve essere rimossa manualmente
	 * 
	 * @param damageableEntity L'entita' nemica trovata in range
	 * @return true per evitare che attack venga richiamato per le restanti unita' in range, false per proseguire con la normale iterazione
	 */
	abstract boolean attack(World world, DamageableEntity damageableEntity);
	
	@Override
	public void evolve(long time, World world) {
		
	}
	
	
}
