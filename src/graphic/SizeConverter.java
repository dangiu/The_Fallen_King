package graphic;

/**
 * Classe che effettua le conversioni dalla lunghezza del mondo (in metri)
 * a quella della grafica (in pixel) per disporre correttamente gli
 * elementi
 * 
 * @author Daniele Giuliani
 * 
 */
public class SizeConverter {
	//costanti di conversione
	private double pixelsToMeters;
	private double metersToPixels;
	
	
	public SizeConverter(double pWorldWidth, int pPixelWidth) {
		pixelsToMeters = pWorldWidth / pPixelWidth;
		metersToPixels = pPixelWidth / pWorldWidth;
		
	}
	
	public double toMeters(int nPixels) {
		return (nPixels * pixelsToMeters);
	}
	
	public int toPixels(double nMeters) {
		return (int) (nMeters * metersToPixels);
	}
}
