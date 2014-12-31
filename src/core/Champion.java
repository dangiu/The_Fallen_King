package core;

/**
 * Unita' in grado di effettuare attacchi fisici avanzata.
 * 
 * @author Willi Menapace
 *
 */
public class Champion extends PhysicalAttacker implements MobileEntity, DamageableEntity {
	private static final double height = 2.0;
	private static final double width = 0.3;
	private int attack = 20;
	private int attackCooldown = 1500;
	private double range = 2;
	private int maxHp = 40;
	private int currentHp = maxHp;
	private int defense = 3;
	private double movementSpeed = 1.2;
	public int baseMoneyOnKill = 200;
	private MovementEngine movementEngine;

	public Champion(Team team, double xSpawnCoordinates, MovementDirection direction) {
		super(team, xSpawnCoordinates, width, height);
		movementEngine = new LinearMovementEngine(movementSpeed, direction);
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
	public int getBaseMoneyOnKill() {
		return baseMoneyOnKill;
	}
	
	@Override
	public void move(int time) {
		this.box = movementEngine.getNewPosition(box, time);
	}
}
