package core;

/**
 * Unita' in grado di attaccare a distanza attraverso frecce.
 * 
 * @author Willi Menapace
 *
 */
public class Archer extends SpawnerAttacker implements MobileEntity, DamageableEntity {
	
	private static final double height = 1.7;
	private static final double width = 0.3;
	private int attack = 10;
	private int attackCooldown = 1500;
	private double range = 1.2;
	private int maxHp = 20;
	private int currentHp = maxHp;
	private int defense = 1;
	private double movementSpeed = 2;
	private MovementEngine movementEngine;

	public Archer(Team team, double xSpawnCoordinates, MovementDirection direction) {
		super(team, xSpawnCoordinates, width, height);
		movementEngine = new LinearMovementEngine(movementSpeed, direction);
	}
	
	@Override
	Entity getSpawnedEntity(DamageableEntity damageableEntity) {
		BasicArrow arrow = new BasicArrow(getTeam(), box.getCenterX(), box.getCenterY(), movementEngine.getDirection(), attack);
		return arrow;
	}
	
	@Override
	public int getAttack() {
		return attack;
	}

	@Override
	public int getAttackCooldown() {
		return attackCooldown;
	}

	@Override
	public double getRange() {
		return range;
	}

	@Override
	public int getHp() {
		return currentHp;
	}

	@Override
	public int getMaxHP() {
		return maxHp;
	}

	@Override
	public void inflictDamage(int damage) {
		currentHp -= damage;
	}

	@Override
	public boolean isDead() {
		if(currentHp <= 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getDefense() {
		return defense;
	}
	
	@Override
	public void move(int time) {
		this.box = movementEngine.getNewPosition(box, time);
	}
	
}
