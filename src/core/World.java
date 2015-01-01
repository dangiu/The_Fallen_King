package core;

//TODO implementare la deserializzazione

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import core.entities.Archer;
import core.entities.BasicArrow;
import core.entities.Champion;
import core.entities.DamageableEntity;
import core.entities.Entity;
import core.entities.MobileEntity;
import core.entities.MovementDirection;
import core.entities.Piker;
import core.entities.Soldier;

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
	 * 
	 * @return Le informazioni non modificabili del mondo
	 */
	public WorldInfo getWorldInfo() {
		return worldInfo;
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
	public void addDeferredEntity(Entity entity) {
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
	
	public static final class SerializationHelper {
		
		private static final int BLUE_ID = 0;
		private static final int RED_ID = 1;
		
		private static final int LEFT_ID = 0;
		private static final int RIGHT_ID = 1;
		
		private static final int ARCHER_ID = 0;
		private static final int BASIC_ARROW_ID = 1;
		private static final int CHAMPTION_ID = 2;
		private static final int PIKER_ID = 3;
		private static final int SOLDIER_ID = 4;
		
		//Evita che venga istanziata
		private SerializationHelper() {};
		
		/**
		 * Serializza sullo stream fornito uno stato incompleto ma riassuntivo del mondo
		 * @param world Lo stato del mondo da serializzare
		 * @throws IOException Lancia l'eccezione in caso sia impossibile serializzare sullo stream specificato
		 * @param out Lo stream su cui serializzare il mondo. Si ottengono performance migliori usando stream bufferizzati.
		 */
		public static void serialize(World world, DataOutputStream out) throws IOException {

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
				out.writeInt(BLUE_ID);
				break;
			case RED:
				out.writeInt(RED_ID);
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
			
			//Effettua un controllo sulle classi e non usando instanceof in modo da evitare
			//problemi relativi ad eventuali sottoclassi aggiunte in un secondo momento
			if(entity.getClass().equals(Archer.class)) {
				Archer archer = (Archer) entity;
				out.writeInt(ARCHER_ID);
				serializeMobileEntity(archer, out);
				serializeDamageableEntity(archer, out);
			} else if(entity.getClass().equals(BasicArrow.class)) {
				BasicArrow arrow = (BasicArrow) entity;
				out.writeInt(BASIC_ARROW_ID);
				//Le frecce potrebbero avere attacchi diversi
				out.writeInt(arrow.getAttack());
				serializeMobileEntity(arrow, out);
			} else if(entity.getClass().equals(Champion.class)) {
				Champion champion = (Champion) entity;
				out.writeInt(CHAMPTION_ID);
				serializeMobileEntity(champion, out);
				serializeDamageableEntity(champion, out);
			} else if(entity.getClass().equals(Piker.class)) {
				Piker piker = (Piker) entity;
				out.writeInt(PIKER_ID);
				serializeMobileEntity(piker, out);
				serializeDamageableEntity(piker, out);
			} else if(entity.getClass().equals(Soldier.class)) {
				Soldier soldier = (Soldier) entity;
				out.writeInt(SOLDIER_ID);
				serializeMobileEntity(soldier, out);
				serializeDamageableEntity(soldier, out);
			} else {
				throw new UnsupportedOperationException("Entity not supported by serializer");
			}
		}
		
		/**
		 * Serializza le informazioni riassuntive relative alle entita' danneggiabili
		 * @param entity L'entita' da serializzare
		 * @param out Lo stream su cui serializzare l'unita'
		 * @throws IOException Lanciata in caso di impossibilita' di scrivere sullo stream fornito
		 */
		private static void serializeDamageableEntity(DamageableEntity entity, DataOutputStream out) throws IOException {
			out.writeInt(entity.getHp());
		}
		
		/**
		 * Serializza le informazioni riassuntibe relative alle entita' mobili
		 * @param entity L'entita' da serializzare
		 * @param out Lo stream su cui serializzare l'unita'
		 * @throws IOException Lanciata in caso di impossibilita' di scrivere sullo stream fornito
		 */
		private static void serializeMobileEntity(MobileEntity entity, DataOutputStream out) throws IOException {
			MovementDirection direction = entity.getMovementDirection();
			switch(direction) {
			case LEFT:
				out.writeInt(LEFT_ID);
				break;
			case RIGHT:
				out.writeInt(RIGHT_ID);
				break;
			default:
				throw new UnsupportedOperationException("MovementDirection not supported by serializer");
			}
		}
		
		/**
		 * Deserializza uno stato riassuntivo del mondo usando lo stream fornito
		 * @param in Lo stream da deserializzare
		 * @throws IOException Lancia l'eccezione in caso sia impossibile deserializzare dal flusso specificato
		 * @return Lo stato riassuntivo del mondo deserializzato
		 */
		public static World deserialize(DataInputStream in) throws IOException {

			long timeOffset = in.readLong();
			
			//Legge le informazioni sul mondo
			int worldWidth = in.readInt();
			WorldInfo worldInfo = new WorldInfo(worldWidth);
			
			//Legge le informazioni sui giocatori
			Map<Team, PlayerInfo> playerInfo = new HashMap<>();
			int playerNumber = in.readInt();
			for(int i = 0; i < playerNumber; ++i) {
				Team team = deserializeTeam(in);
				double playerMoney = in.readDouble();
				
				playerInfo.put(team, new PlayerInfo(playerMoney));
			}
			
			World world = new World(worldInfo, playerInfo);
			world.timeOffset = timeOffset;
			
			//Legge le informazioni sulle entita'
			int entityNumber = in.readInt();
			for(int i = 0; i < entityNumber; ++i) {
				Entity entity = deserializeEntity(in);
				world.addEntity(entity);
			}
			
			return world;
		}
		
		private static Team deserializeTeam(DataInputStream in) throws IOException {
			int teamId = in.readInt();
			
			if(teamId == BLUE_ID) {
				return Team.BLUE;
			} else if(teamId == RED_ID) {
				return Team.RED;
			} else {
				throw new UnsupportedOperationException("Team not supported by deserializer");
			}
		}
		
		private static MovementDirection deserializeMovementDirection(DataInputStream in) throws IOException {
			int directionId = in.readInt();
			
			if(directionId == LEFT_ID) {
				return MovementDirection.LEFT;
			} else if(directionId == RIGHT_ID) {
				return MovementDirection.RIGHT;
			} else {
				throw new UnsupportedOperationException("Direction not supported by deserializer");
			}
		}
		
		private static Entity deserializeEntity(DataInputStream in) throws IOException {
			Team team = deserializeTeam(in);
			double x = in.readDouble();
			double y = in.readDouble();
			
			int entityId = in.readInt();
			
			Entity deserializedEntity;
			
			if(entityId == ARCHER_ID) {
				MovementDirection direction = deserializeMovementDirection(in);
				int currentHp = in.readInt();
				Archer archer = new Archer(team, x, direction);
				archer.setHp(currentHp);
				deserializedEntity = archer;
			} else if(entityId == BASIC_ARROW_ID) {
				int attack = in.readInt();
				MovementDirection direction = deserializeMovementDirection(in);
				BasicArrow arrow = new BasicArrow(team, x, y, direction, attack);
				deserializedEntity = arrow;
			} else if(entityId == CHAMPTION_ID) {
				MovementDirection direction = deserializeMovementDirection(in);
				int currentHp = in.readInt();
				Champion champion = new Champion(team, x, direction);
				champion.setHp(currentHp);
				deserializedEntity = champion;
			} else if(entityId == PIKER_ID) {
				MovementDirection direction = deserializeMovementDirection(in);
				int currentHp = in.readInt();
				Piker piker = new Piker(team, x, direction);
				piker.setHp(currentHp);
				deserializedEntity = piker;
			} else if(entityId == SOLDIER_ID) {
				MovementDirection direction = deserializeMovementDirection(in);
				int currentHp = in.readInt();
				Soldier soldier = new Soldier(team, x, direction);
				soldier.setHp(currentHp);
				deserializedEntity = soldier;
			} else {
				throw new UnsupportedOperationException("Entity not supporded by deserializer");
			}
			
			return deserializedEntity;
		}
		
		
	}
	
	
}
