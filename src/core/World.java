package core;

import java.util.ArrayList;
import java.util.HashMap;
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
	}
	
	/**
	 * Inserisce un'entita' nel mondo
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
	public void simulate(long time) {
		for(Entity entity : entities) {
			entity.evolve(time, this);
		}
	}
	
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
	

	
	
}
