package core;

/** 
 * Contiene lo stato di un giocatore
 * 
 * @author Willi Menapace
 * 
 */
public class PlayerInfo {
	private double money;
	private double moneyPerSecond = 2;
	
	/**
	 * Crea un nuovo giocatore assegnando denaro iniziale
	 * @param initialMoney Il denaro iniziale. Deve essere >= 0
	 */
	public PlayerInfo(double initialMoney) {
		if(initialMoney < 0) {
			throw new IllegalArgumentException("Initial money must be >= 0");
		}
		
		this.money = initialMoney;
	}
	
	/**
	 * @return Il denaro attualmente disponibile al giocatore
	 */
	public double getMoney() {
		return money;
	}
	
	/**
	 * Aggiunge una certa somma di denaro al giocatore
	 * @param money Il denaro da aggiungere al giocatore. Deve essere >= 0
	 */
	public void giveMoney(double money) {
		if(money < 0) {
			throw new IllegalArgumentException("Money to add to the player must be non negative");
		}
		this.money += money;
	}
	
	/**
	 * Rimuove una certa somma di denaro al giocatore
	 * @param moneyToRemove Il denaro da rimuovere al giocatore. Deve essere <= getMoney()
	 */
	public void removeMoney(double moneyToRemove) {
		if(getMoney() < moneyToRemove) {
			throw new IllegalArgumentException("Money to remove can't be > than total player money");
		}
		
		money -= moneyToRemove;
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
