package core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Classe helper che fornisce metodi di convenienza per la serializzazione e deserializzazione di team
 * 
 * @author Willi Menapace
 *
 */
public class TeamSerializationHelper {
	
	private static final int BLUE_ID = 0;
	private static final int RED_ID = 1;
	
	private TeamSerializationHelper() {};
	
	/**
	 * Serializza un team
	 * @param team Il team da serializzare
	 * @param out Lo stream su cui serializzare il team
	 * @throws IOException Lanciata in caso di impossibilita' di scrittura sullo stream fornito
	 */
	public static void serializeTeam(Team team, DataOutputStream out) throws IOException {
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
	 * Deserializza un team
	 * @param in Lo stream da cui deserializzare il team
	 * @return Il team deserializzato
	 * @throws IOException Lanciata in caso di impossibilita' di deserializzare il team
	 */
	public static Team deserializeTeam(DataInputStream in) throws IOException {
		int teamId = in.readInt();
		
		if(teamId == BLUE_ID) {
			return Team.BLUE;
		} else if(teamId == RED_ID) {
			return Team.RED;
		} else {
			throw new UnsupportedOperationException("Team not supported by deserializer");
		}
	}
}
