package core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import core.entities.Entity;
import core.entities.EntitySerializationHelper;

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
				TeamSerializationHelper.serializeTeam(team, out);
				out.writeDouble(world.playerInfo.get(team).getMoney());
			}
			
			//Scrive le informazioni sulle entita'
			int entitiesSize = world.entities.size();
			out.writeInt(entitiesSize);
			for(Entity entity : world.entities) {
				EntitySerializationHelper.serializeEntity(entity, out);
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
				Team team = TeamSerializationHelper.deserializeTeam(in);
				double playerMoney = in.readDouble();
				
				playerInfo.put(team, new PlayerInfo(playerMoney));
			}
			
			World world = new World(worldInfo, playerInfo);
			world.timeOffset = timeOffset;
			
			//Legge le informazioni sulle entita'
			int entityNumber = in.readInt();
			for(int i = 0; i < entityNumber; ++i) {
				Entity entity = EntitySerializationHelper.deserializeEntity(in);
				world.addEntity(entity);
			}
			
			return world;
		}
		
		
	}
	
	
}
