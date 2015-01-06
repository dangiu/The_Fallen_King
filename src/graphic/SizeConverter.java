package graphic;

/**
 * Classe che effettua le conversioni dalla lunghezza del mondo (in metri)
 * a quella della grafica (in pixel) per disporre correttamente gli
 * elementi
 * 
 * @author Daniele Giuliani
 */
public class SizeConverter {
	//costanti di conversione
	private long pixelsToMeters;
	private long metersToPixels;
	
	
	public SizeConverter(long pWorldWidth, long pPixelWidth) {
		pixelsToMeters = pWorldWidth / pPixelWidth;
		metersToPixels = pPixelWidth / pWorldWidth;
		
	}
	
	public long toMeters(long nPixels) {
		return (nPixels * pixelsToMeters);
	}
	
	public long toPixels(long nMeters) {
		return (nMeters * metersToPixels);
	}
}
