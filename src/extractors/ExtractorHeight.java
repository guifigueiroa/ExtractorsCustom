package extractors;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.media.jai.PlanarImage;

/**
 * Extrai a altura de uma regiao ou conjunto de regioes de interesse em uma imagem segmentada.
 * 
 * Calcula o numero de linhas compreendidas entre a primeira e a ultima linha, inclusive, que
 * possuem um pixel ou mais pertencente(s) a regiao de interesse. Normaliza dividindo o numero
 * pela altura total em pixels da imagem.
 * 
 */
public class ExtractorHeight implements IExtractor {
	
	private int thr = 0;
	
	@Override
	public String getName() {
		return "Height";
	}
	
	@Override
	public void setProperty(String propertieName, Object propertieNewValue) {
		if(propertieName.equals("thr")) {
			Integer newValue = new Integer(propertieNewValue.toString());
			thr = newValue;
		}
		else {
			System.out.println("Error: The property " + propertieName + " can not be changed.");
		}
	}
	
	@Override
	public Object getProperty(String propertieName) {
		if(propertieName.equals("thr")) {
			return thr;
		}
		else {
			return null;
		}
	}
	
	@Override
	public Object[] getProperties() {
		Object valuesObj[] = new Object[]{thr};
		return valuesObj;
	}
	
	@Override
	public String[] getPropertiesNames() {
		return new String[] {"thr"};
	}
	
	@Override
	public double computeValue(Image image) {
		BufferedImage bufferImage = (BufferedImage) image;
		PlanarImage planarImg = PlanarImage.wrapRenderedImage(bufferImage);
		
		int limSupImg = -1, limInfImg = -1;
		int width = planarImg.getWidth();
		int height = planarImg.getHeight();
		int numBands = planarImg.getNumBands();
		int[] pixelsValues = new int[width*height*numBands];
		
		planarImg.getData().getPixels(0, 0, width, height, pixelsValues);
		
		for (int i = 0; i < pixelsValues.length; i++) {
			// Encontra a primeira linha com um pixel ou mais que pertence(m) a regiao de interesse.
			if(pixelsValues[i] <= thr) continue;
			
			limSupImg = i / width;
			break;
		}
		
		for (int i = (pixelsValues.length - 1); i >= 0; i--) {
			// Encontra a ultima linha com um pixel (ou mais) que pertence(m) a regiao de interesse.
			if(pixelsValues[i] <= thr) continue;
			
			limInfImg = i / width;
			break;
		}
		
		if(limSupImg < 0)
			return 0.0;
		
		return (((double) (limInfImg - limSupImg) + 1) / (double) height);
	}
}
