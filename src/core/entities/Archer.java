package core.entities;

import core.Team;

/**
 * Unita' in grado di attaccare a distanza attraverso frecce.
 * 
 * @author Willi Menapace
 *
 */
public class Archer extends SpawnerAttacker implements MobileEntity, DamageableEntity, BuyableEntity {
	
	private static final double height = 1.7;
	private static final double width = 1.75;
	private int attack = 10;
	private int attackCooldown = 2000;
	private double range = 20;
	private int maxHp = 20;
	private int currentHp = maxHp;
	private int defense = 0;
	private double movementSpeed = 2;
	public int baseMoneyOnBuy = 100;
	public int baseMoneyOnKill = 100;
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
	public void setHp(int hp) {
		if(hp > getMaxHp()) {
			throw new IllegalArgumentException("New HP must not be greater than MaxHp");
		}
		
		currentHp = hp;
	}

	@Override
	public int getMaxHp() {
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
	
	@Override
	public MovementDirection getMovementDirection() {
		return movementEngine.getDirection();
	}
	
	@Override
	public int getBaseCost() {
		return baseMoneyOnBuy;
	}
	
}
