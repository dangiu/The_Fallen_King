package core;

/**
 * Rappresenta una freccia in grado di colpire una sola volta e che si distrugge non appena
 * colpisce per la prima volta.
 * 
 * @author Willi Menapace
 *
 */
public class BasicArrow extends KamikazeAttacker implements MobileEntity {
	
	private static final double height = 0.2;
	private static final double width = 0.5;
	private int attack; //Il suo attacco dipende dall'entita' che spawna la freccia
	private int attackCooldown = -1; //Non effettua piu' di un attacco
	private double range = 0; //Colpisce solo al contatto
	private double movementSpeed = 5;
	private MovementEngine movementEngine;

	public BasicArrow(Team team, double xSpawnCoordinates, double ySpawnCoordinates, MovementDirection direction, int attack) {
		super(team, xSpawnCoordinates, ySpawnCoordinates, width, height);
		this.attack = attack;
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
	public void move(int time) {
		this.box = movementEngine.getNewPosition(box, time);
	}
}
