package core;

/** 
 * Contiene lo stato di un giocatore
 * 
 * @author Willi Menapace
 * 
 */
public class PlayerInfo {
	private double money;
	private double moneyPerSecond;
	
	/**
	 * Crea un nuovo giocatore assegnando denaro iniziale e guadagno al secondo
	 * @param initialMoney Il denaro iniziale. Deve essere >= 0
	 * @param moneyPerSecond Il denaro guadagnato al secondo.
	 */
	public PlayerInfo(double initialMoney, double moneyPerSecond) {
		if(initialMoney < 0) {
			throw new IllegalArgumentException("Initial money must be >= 0");
		}
		
		this.money = initialMoney;
		this.moneyPerSecond = moneyPerSecond;
	}
	
	public double getMoney() {
		return money;
	}
	
	public void giveMoney(double money) {
		this.money += money;
	}
	
	/**
	 * Simula la variazione di denaro nel periodo di tempo specificato
	 * 
	 * @param time Il periodo di tempo in ms da simulare
	 */
	public void simulate(int time) {
		money += time / 1000.0 * moneyPerSecond;
	}
}
