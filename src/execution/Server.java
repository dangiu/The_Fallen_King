package execution;

import java.util.HashMap;
import java.util.Map;

import core.PlayerInfo;
import core.Team;
import core.World;
import core.WorldInfo;

//TODO implementare castello, finire implementazione server

/**
 * Server di esecuzione della simulazione del mondo.
 * Gestisce le connessioni con i client e l'esecuzione delle simulazioni.
 * 
 * @author Willi Menapace
 *
 */
class Server {

	private World world;
	
	/**
	 * Configura un server impostato per un match standard
	 */
	public Server() {
		WorldInfo worldInfo = new WorldInfo(50);
		
		Map<Team, PlayerInfo> playerInfo = new HashMap<>();
		
		playerInfo.put(Team.BLUE, new PlayerInfo(500));
		playerInfo.put(Team.RED, new PlayerInfo(500));
		
		world = new World(worldInfo, playerInfo);
	}
}
