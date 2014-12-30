package core;

/**
 * 
 * Rappresenta informazioni non modificabili del mondo
 * La classe e' immutabile
 * 
 * @author Willi Menapace
 * 
 */
public final class WorldInfo {
	private final int worldWidth;
	
	public final int getWorldWidth() {
		return worldWidth;
	}
	
	/**
	 * Genera una descrizione immutabile del mondo
	 * 
	 * @param worldWidth La larghezza del mondo in metri. Deve essere > 0
	 */
	public WorldInfo(int worldWidth) {
		if(worldWidth <= 0) {
			throw new IllegalArgumentException("World width must be > 0");
		}
		
		this.worldWidth = worldWidth;
	}
}
