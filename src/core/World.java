package core;

//TODO implementare la deserializzazione

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Rappresenta uno stato della partita
 * 
 * @author Willi Menapace
 *
 */
public class World {
	//Contiene tutte le entita' presenti nello stato del mondo rappresentato
	private List<Entity> entities;
	//Buffer temporanei in cui sono memorizzate le entita' da aggiungere o rimuovere in maniera differita
	private List<Entity> deferredAddEntityBuffer;
	private List<Entity> deferredRemoveEntityBuffer;
	
	//Contiene lo stato globale dei giocatori
	private Map<Team, PlayerInfo> playerInfo;
	//Contiene le informazioni sul mondo
	private WorldInfo worldInfo;
	//Contiene le condizioni di vincita dei giocatori
	private Map<Team, List<VictoryCondition>> victoryConditions;
	
	//Il tempo totale in millisecondi che e' stato simulato dal mondo
	private long timeOffset = 0;
	
	/**
	 * Crea un mondo senza entita' e condizioni di vittoria
	 * 
	 * @param worldInfo La descrizione del mondo da creare
	 * @param playerInfo Mappa contenente le informazioni iniziali dei giocatori.
	 */
	public World(WorldInfo worldInfo, Map<Team, PlayerInfo> playerInfo) {
		if(worldInfo == null) {
			throw new NullPointerException("The world must be initialized with valid parameters");
		}
		
		this.worldInfo = worldInfo;
		
		if(playerInfo == null) {
			throw new NullPointerException("The world must be initialized with valid player informations");
		}
		
		this.playerInfo = new HashMap<>(); //Will create a new data structure to ensure it is a suitable implementation
		for(Team team : Team.values()) {
			if(!playerInfo.containsKey(team)) {
				throw new IllegalArgumentException("The world must contain valid information for each player");
			}
			this.playerInfo.put(team, playerInfo.get(team));
		}
		
		entities = new ArrayList<Entity>();
		victoryConditions = new HashMap<>();
		for(Team team : Team.values()) {
			victoryConditions.put(team, new ArrayList<VictoryCondition>());
		}
		
		deferredAddEntityBuffer = new ArrayList<>();
		deferredRemoveEntityBuffer = new ArrayList<>();
	}
	
	/**
	 * Effettua la sincronizzazione della lista di entita' effettuando le
	 * aggiunte e le rimozioni differite
	 */
	private void performDeferredEntityModifications() {
		for(Entity currentEntity : deferredAddEntityBuffer) {
			entities.add(currentEntity);
		}
		for(Entity currentEntity : deferredRemoveEntityBuffer) {
			entities.remove(currentEntity);
		}
		deferredAddEntityBuffer.clear();
		deferredRemoveEntityBuffer.clear();
	}
	
	/**
	 * Inserisce un'entita' nel mondo.
	 * Non puo' essere richiamato durante una simulazione
	 * 
	 * @param entity L'entita' da inserire. Deve essere != null
	 */
	public void addEntity(Entity entity) {
		if(entity == null) {
			throw new NullPointerException("Entity to insert inside the world must be non null");
		}
		
		entities.add(entity);
	}
	
	/**
	 * Inserisce un'entita' nel mondo all'inizio della prossima simulazione
	 * 
	 * @param entity L'entita' da inserire. Deve essere != null
	 */
	void addDeferredEntity(Entity entity) {
		if(entity == null) {
			throw new NullPointerException("Entity to insert inside the world must be non null");
		}
		
		deferredAddEntityBuffer.add(entity);
	}
	
	/**
	 * Rimuove un'entita' dal mondo
	 * Non puo' essere richiamato durante una simulazione
	 *  
	 * @param entity L'entita' da rimuovere. Deve essere presente nel mondo e != null
	 */
	public void removeEntity(Entity entity) {
		if(entity == null) {
			throw new NullPointerException("Entity to remove must be non null");
		}
		if(!entities.contains(entity)) {
			throw new IllegalArgumentException("The entity to remove is not contained in the world");
		}
		
		entities.remove(entity);
	}
	
	/**
	 * Rimuove un'entita' dal mondo all'inizio della prossima simulazione
	 *  
	 * @param entity L'entita' da rimuovere. Deve essere presente nel mondo e != null
	 */
	public void removeDeferredEntity(Entity entity) {
		if(entity == null) {
			throw new NullPointerException("Entity to remove must be non null");
		}
		//Se la richiesta di rimozione viene fatta nello stesso periodo di simulazione dell'
		//aggiunta allora l'entita' da rimuovere potrebbe trovarsi nel buffer di aggiunta differita
		if(!entities.contains(entity) && !deferredAddEntityBuffer.contains(entity)) {
			throw new IllegalArgumentException("The entity to remove is not contained in the world");
		}
		
		deferredRemoveEntityBuffer.add(entity);
	}
	
	public Iterator<Entity> getEntityIterator() {
		return entities.iterator();
	}
	
	/**
	 * Aggiunge una nuova condizione che se soddisfatta provoca la vincita del giocatore
	 * 
	 * @param team Il giocatore che vincera' se la condizione risulta soddisfatta
	 * @param condition La condizione che se soddisfatta fa vincere il giocatore. Deve essere != null
	 */
	public void addVictoryCondition(Team team, VictoryCondition condition) {
		if(condition == null) {
			throw new NullPointerException("Cant add a null victory condition");
		}
		
		victoryConditions.get(team).add(condition);
	}
	
	/**
	 * Simula lo stato del mondo dopo un certo lasso di tempo
	 * 
	 * @param time Il periodo di tempo in ms di cui simulare l'evoluzione del mondo
	 */
	public void simulate(int time) {
		timeOffset += time;
		
		//Effettua le aggiunte e le rimozioni richieste nel mezzo della simulazione precedente
		performDeferredEntityModifications();
		for(Entity entity : entities) {
			entity.evolve(time, this);
		}
		//Permette ai giocatori di modificare il denaro a disposizione
		for(PlayerInfo player : playerInfo.values()) {
			player.simulate(time);
		}
	}
	
	/**
	 * Ritorna le informazioni relative ad un team
	 * 
	 * @param team il team di cui si vogliono avere le informazioni
	 * @return Le informazioni del team richiesto
	 */
	public PlayerInfo getPlayerInfo(Team team) {
		return playerInfo.get(team);
	}
	
	/**
	 * Restituisce la lista di condizioni di vittoria soddisfatte per un dato team
	 * 
	 * @param team Il team di cui controllare le condizioni di vittoria
	 * @return Lista di condizioni di vittoria soddisfatte
	 */
	public List<VictoryCondition> checkSatisfiedVictoryConditions(Team team) {
		List<VictoryCondition> satisfiedConditions = new ArrayList<>();
		List<VictoryCondition> conditions = victoryConditions.get(team);
		for(VictoryCondition condition : conditions) {
			if(condition.testVictory()) {
				satisfiedConditions.add(condition);
			}
		}
		return satisfiedConditions;
	}
	
	private static final class SerializerHelper {
		
		private static final int BLUE_ID = 0;
		private static final int RED_ID = 1;
		
		private static final int ARCHER_ID = 0;
		private static final int BASIC_ARROW_ID = 1;
		private static final int CHAMPTION_ID = 2;
		private static final int PIKER_ID = 3;
		private static final int SOLDIER_ID = 4;
		
		//Evita che venga istanziata
		private SerializerHelper() {};
		
		/**
		 * Serializza sullo stream fornito uno stato incompleto ma riassuntivo del mondo
		 * @param world Lo stato del mondo da serializzare
		 * @param out Lo stream su cui serializzare il mondo. Si ottengono performance migliori usando stream bufferizzati.
		 */
		public void serialize(World world, DataOutputStream out) {
			try {
				out.writeLong(world.timeOffset);
				
				//Scrive le informazioni sul mondo
				out.writeInt(world.worldInfo.getWorldWidth());
				
				//Scrive le informazioni sui giocatori
				int playerInfoSize = world.playerInfo.size();
				out.writeInt(playerInfoSize);
				for(Team team : world.playerInfo.keySet()) {
					serializeTeam(team, out);
					out.writeDouble(world.playerInfo.get(team).getMoney());
				}
				
				//Scrive le informazioni sulle entita'
				int entitiesSize = world.entities.size();
				out.writeInt(entitiesSize);
				for(Entity entity : world.entities) {
					serializeEntity(entity, out);
				}
				
			} catch(IOException e) {
				System.out.println("Was not able to serizlize World status\n" + e.getMessage());
			}
		}
		
		/**
		 * Serializza un team
		 * @param team Il team da serializzare
		 * @param out Lo stream su cui serializzare il team
		 * @throws IOException Lanciata in caso di impossibilita' di scrittura sullo stream fornito
		 */
		private static void serializeTeam(Team team, DataOutputStream out) throws IOException {
			switch(team) {
			case BLUE:
				out.writeInt(0);
				break;
			case RED:
				out.writeInt(1);
				break;
			default:
				throw new UnsupportedOperationException("Team not implemented yet");
			}
		}
		
		/**
		 * Serializza riassuntivamente l'entita' inclusi gli attributi significativi delle sottoclassi conosciute
		 * @param entity L'entita' da serializzare
		 * @param out Lo stream su cui serializzare l'entita'
		 * @throws IOException Lanciata in caso di impossibilita' di scrittura sullo stream fornito
		 */
		private static void serializeEntity(Entity entity, DataOutputStream out) throws IOException {
			//Serializza le informazioni comuni
			serializeTeam(entity.getTeam(), out);
			out.writeDouble(entity.getBox().getX());
			out.writeDouble(entity.getBox().getY());
			out.writeDouble(entity.getBox().getWidth());
			out.writeDouble(entity.getBox().getHeight());
			
			if(entity instanceof Archer) {
				Archer archer = (Archer) entity;
				out.writeInt(ARCHER_ID);
				serializeDamageableEntity(archer, out);
			} else if(entity instanceof BasicArrow) {
				out.writeInt(BASIC_ARROW_ID);
			} else if(entity instanceof Champion) {
				Champion champion = (Champion) entity;
				out.writeInt(CHAMPTION_ID);
				serializeDamageableEntity(champion, out);
			} else if(entity instanceof Piker) {
				Piker piker = (Piker) entity;
				out.writeInt(PIKER_ID);
				serializeDamageableEntity(piker, out);
			} else if(entity instanceof Soldier) {
				Soldier soldier = (Soldier) entity;
				out.writeInt(SOLDIER_ID);
				serializeDamageableEntity(soldier, out);
			} else {
				throw new UnsupportedOperationException("Entity not supported by serializer");
			}
		}
		
		/**
		 * Serializza le informazioni riassuntive relative alle entita' danneggiabili
		 * 
		 * @param entity L'entita' da serializzare
		 * @param out Lo stream su cui serializzare l'unita'
		 * @throws IOException Lanciata in caso di impossibilita' di scrivere sullo stream fornito
		 */
		private static void serializeDamageableEntity(DamageableEntity entity, DataOutputStream out) throws IOException {
			out.writeInt(entity.getHp());
		}
		
	}
	
	
}
