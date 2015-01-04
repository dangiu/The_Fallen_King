package core.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Classe helper che fornisce metodi di convenienza per la serializzazione
 * e deserializzazione di direzioni di movimento
 * 
 * @author Willi Menapace
 *
 */
public class MovementDirectionSerializationHelper {

	private static final int LEFT_ID = 0;
	private static final int RIGHT_ID = 1;
	
	private MovementDirectionSerializationHelper() {};
	
	/**
	 * Serializza una direzione di movimento
	 * @param direction La direzione di movimento
	 * @param out Lo stream su cui serializzare la direzione di movimento
	 * @throws IOException Lanciata in caso di impossibilita' di serializzare la direzione di movimento
	 */
	public static void serializaMovementDirection(MovementDirection direction, DataOutputStream out) throws IOException {
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
	 * Serializza la direzione di movimento
	 * @param in Lo stream da cui deserializzare la direzione di movimento
	 * @return La direzione di moviemnto deserializzata
	 * @throws IOException Lanciata in caso di impossibilita' di deserializzare da direzione di movimento
	 */
	public static MovementDirection deserializeMovementDirection(DataInputStream in) throws IOException {
		int directionId = in.readInt();
		
		if(directionId == LEFT_ID) {
			return MovementDirection.LEFT;
		} else if(directionId == RIGHT_ID) {
			return MovementDirection.RIGHT;
		} else {
			throw new UnsupportedOperationException("Direction not supported by deserializer");
		}
	}
	
}
