package core.entities;

import java.awt.geom.Rectangle2D;

import core.Team;
import core.World;

public class Castle implements DamageableEntity {

	Rectangle2D.Double box; //La hitbox dell'unita'. Deve essere accessbile alle sottoclassi in caso dovessero muovere l'unita'
	private Team team;
	
	private static final double height = 10;
	public static final double width = 15;
	
	private int maxHp = 300;
	private int currentHp = maxHp;
	private int defense = 3;
	
	public int baseMoneyOnKill = 1500;
	
	public Castle(Team team, double xSpawnCoordinates) {
		this.team = team;
		this.box = new Rectangle2D.Double(xSpawnCoordinates, 0, width, height);
	}
	
	@Override
	public Rectangle2D getBox() {
		return box;
	}

	@Override
	public Team getTeam() {
		return team;
	}

	@Override
	public void evolve(int time, World world) {
		//Does nothing
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

}
