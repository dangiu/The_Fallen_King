package core.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import core.Team;
import core.TeamSerializationHelper;

/**
 * Classe helper che fornisce metodi di convenienza per la serializzazione e deserializzazione di entita'
 * 
 * @author Willi Menapace
 */
public class EntitySerializationHelper {
	
	private static final int ARCHER_ID = 0;
	private static final int BASIC_ARROW_ID = 1;
	private static final int CHAMPTION_ID = 2;
	private static final int PIKER_ID = 3;
	private static final int SOLDIER_ID = 4;
	
	private EntitySerializationHelper() {};

	/**
	 * Serializza riassuntivamente l'entita' inclusi gli attributi significativi delle sottoclassi conosciute
	 * @param entity L'entita' da serializzare
	 * @param out Lo stream su cui serializzare l'entita'
	 * @throws IOException Lanciata in caso di impossibilita' di scrittura sullo stream fornito
	 */
	public static void serializeEntity(Entity entity, DataOutputStream out) throws IOException {
		//Serializza le informazioni comuni
		TeamSerializationHelper.serializeTeam(entity.getTeam(), out);
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
		MovementDirectionSerializationHelper.serializaMovementDirection(direction, out);
	}
	
	public static Entity deserializeEntity(DataInputStream in) throws IOException {
		Team team = TeamSerializationHelper.deserializeTeam(in);
		double x = in.readDouble();
		double y = in.readDouble();
		
		int entityId = in.readInt();
		
		Entity deserializedEntity;
		
		if(entityId == ARCHER_ID) {
			MovementDirection direction = MovementDirectionSerializationHelper.deserializeMovementDirection(in);
			int currentHp = in.readInt();
			Archer archer = new Archer(team, x, direction);
			archer.setHp(currentHp);
			deserializedEntity = archer;
		} else if(entityId == BASIC_ARROW_ID) {
			int attack = in.readInt();
			MovementDirection direction = MovementDirectionSerializationHelper.deserializeMovementDirection(in);
			BasicArrow arrow = new BasicArrow(team, x, y, direction, attack);
			deserializedEntity = arrow;
		} else if(entityId == CHAMPTION_ID) {
			MovementDirection direction = MovementDirectionSerializationHelper.deserializeMovementDirection(in);
			int currentHp = in.readInt();
			Champion champion = new Champion(team, x, direction);
			champion.setHp(currentHp);
			deserializedEntity = champion;
		} else if(entityId == PIKER_ID) {
			MovementDirection direction = MovementDirectionSerializationHelper.deserializeMovementDirection(in);
			int currentHp = in.readInt();
			Piker piker = new Piker(team, x, direction);
			piker.setHp(currentHp);
			deserializedEntity = piker;
		} else if(entityId == SOLDIER_ID) {
			MovementDirection direction = MovementDirectionSerializationHelper.deserializeMovementDirection(in);
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
