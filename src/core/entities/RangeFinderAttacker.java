package core.entities;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.PlayerInfo;
import core.Team;
import core.World;


/**
 * Classe astratta che fornisce la base per implementare entita' che attaccano nemici
 * all'interno del loro range. Non e' applicabile a entita' che interagiscono con entita'
 * alleate ad esempio curandole.
 * 
 * @author Willi Menapace
 *
 */
abstract class RangeFinderAttacker implements AttackerEntity {
	
	Rectangle2D.Double box; //La hitbox dell'unita'. Deve essere accessbile alle sottoclassi in caso dovessero muovere l'unita'
	private Team team;
	
	private int currentCooldown = 0; //Il tempo che deve ancora trascorrere prima di poter attaccare nuovamente
	
	private List<Entity> cachedInRangeEnemies = null;
	private boolean enemyCacheAvailable = false;
	
	/**
	 * Costruisce l'entita' ponendola nel team selezionato e assegnandole posizione e dimensioni
	 * @param team Il team a cui appartiene l'entita'
	 * @param xSpawnPosition La posizione in metri in cui posizionare l'entita'
	 * @param ySpawnPosition La posizione in metri dal suolo i cui spawnare l'entita'
	 * @param width L'altezza dell'unita'
	 * @param height La larghezza dell'unita'
	 */
	RangeFinderAttacker(Team team, double xSpawnPosition, double ySpawnPosition, double width, double height) {
		this.team = team;
		box = new Rectangle2D.Double(xSpawnPosition, ySpawnPosition, width, height);
	}
	
	@Override
	public Rectangle2D getBox() {
		return box;
	}

	@Override
	public Team getTeam() {
		return team;
	}
	
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
				//Le unita' che impostano il proprio cooldown a -1 segnalano di non voler piu' attaccare
				if(currentCooldown == -1) {
					break;
				}
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
		
		Iterator<Entity> iterator;
		//Se sono gia' state calcolate le unita' nel range allora non e' necessario ricalcolarle
		if(enemyCacheAvailable) {
			iterator = cachedInRangeEnemies.iterator();
		} else {
			iterator = world.getEntityIterator();			
		}
		
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
						
						//Assegna i soldi corrispondenti all'attaccante
						PlayerInfo playerInfo = world.getPlayerInfo(this.getTeam());
						playerInfo.giveMoney(damageableEntity.getBaseMoneyOnKill());
						
						//Rimuove in maniera differita l'entia' per evitare modifiche alla struttuta dati durante le iterazioni
						world.removeDeferredEntity(damageableEntity);
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
	public void evolve(int time, World world) {
		
		//Se l'entita' puo' muoversi e non ci sono unita' attaccabili
		if(this instanceof MobileEntity) {
			MobileEntity thisEntity = (MobileEntity) this;
			
			cachedInRangeEnemies = new ArrayList<>();
			cachedInRangeEnemies = AttackHelper.getInRangeEntities(this, world, AttackHelper.SearchMode.ENEMY);
			enemyCacheAvailable = true; //Convalida la cache
			
			if(cachedInRangeEnemies.isEmpty()) {
				thisEntity.move(time);
				//Se ci si muove bisogna ricalcolare le unita' in range
				enemyCacheAvailable = false;
			}
		}
		
		evalAndAttack(world, time);
		//Alla fine del periodo di evoluzione le altre entita' nel mondo evolvono a loro volta
		//cambiando posizione
		enemyCacheAvailable = false;
	}
	
}
