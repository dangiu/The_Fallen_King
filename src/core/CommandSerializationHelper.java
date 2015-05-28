package core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import core.entities.BuyableEntity;
import core.entities.Entity;
import core.entities.EntitySerializationHelper;

/**
 * Classe helper che fornisce metodi di convenienza per la serializzazione e deserializzazione
 * di comandi
 * 
 * @author Willi Menapace
 *
 */
public class CommandSerializationHelper {

	private static final int SPAWN_ENTITY_ID = 0;
	private static final int BUY_AND_SPAWN_ENTITY_ID = 1;
	
	private CommandSerializationHelper() {};
	
	/**
	 * Serializza un comando sullo stream fornito
	 * @param command Il comando da serializzare
	 * @param out Lo stream su cui serializzare il comando
	 * @throws IOException Lanciata in caso di impossibilita' di serializzare il comando sullo stream fornito
	 */
	public static void serializeCommand(Command command, DataOutputStream out) throws IOException {
		if(command.getClass().equals(SpawnEntityCommand.class)) {
			out.writeInt(SPAWN_ENTITY_ID);
			SpawnEntityCommand spawnCommand = (SpawnEntityCommand) command;
			EntitySerializationHelper.serializeEntity(spawnCommand.getEntity(), out);
		} else if(command.getClass().equals(BuyAndSpawnEntityCommand.class)) {
			out.writeInt(BUY_AND_SPAWN_ENTITY_ID);
			SpawnEntityCommand spawnCommand = (SpawnEntityCommand) command;
			EntitySerializationHelper.serializeEntity(spawnCommand.getEntity(), out);
		} else {
			throw new UnsupportedOperationException("Command not supported by serializer");
		}
		out.flush();
	}
	
	/**
	 * Deserializza un comando a partire dallo stream fornito
	 * @param in Lo stream da cui deserializzare il comando
	 * @return Il comando deserializzato
	 * @throws IOException Lanciata in caso di impossibilita' di deserializzare il comando dallo stream fornito
	 */
	public static Command deserializeCommand(DataInputStream in) throws IOException {
		int commandId = in.readInt();
		
		Command deserializedCommand;
		
		switch(commandId) {
		case SPAWN_ENTITY_ID:
			Entity deserializedEntity = EntitySerializationHelper.deserializeEntity(in);
			deserializedCommand = new SpawnEntityCommand(deserializedEntity);
			break;
		case BUY_AND_SPAWN_ENTITY_ID:
			BuyableEntity deserializedBuyableEntity = (BuyableEntity) EntitySerializationHelper.deserializeEntity(in);
			//L'entita' serializzata a partire da un BuyAndSpawnEntityCommand era sicuramente una BuyableEntity
			deserializedCommand = new BuyAndSpawnEntityCommand(deserializedBuyableEntity);
			break;
		default:
			throw new UnsupportedOperationException("Command not supported by deserializer");
		}
		
		return deserializedCommand;
	}
	
}
