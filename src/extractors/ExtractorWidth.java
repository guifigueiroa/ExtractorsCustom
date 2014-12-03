package extractors;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.media.jai.PlanarImage;
import extractors.IExtractor;

/**
 * 
 * Extrai a largura de uma regiao ou conjunto de regioes de interesse em uma imagem segmentada.
 * 
 * Calcula o numero de colunas compreendidas entre a primeira e a ultima coluna, inclusive, que
 * possuem um pixel ou mais pertencente(s) a regiao de interesse. Normaliza dividindo o numero
 * pela largura total em pixels da imagem.
 * 
 */
public class ExtractorWidth implements IExtractor {
	
	private int thr = 0;
	
	@Override
	public String getName() {
		return "Width";
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
		return new String[]{"thr"};
	}
	
	@Override
	public double computeValue(Image image) {
		BufferedImage bufferImage = (BufferedImage) image;
		PlanarImage planarImg = PlanarImage.wrapRenderedImage(bufferImage);
		
		int limEsqImg = Integer.MAX_VALUE, limDirImg = -1, atual = 0;
		int width = planarImg.getWidth();
		int height = planarImg.getHeight();
		int numBands = planarImg.getNumBands();
		int[] pixelsValues = new int[width*height*numBands];
		
		planarImg.getData().getPixels(0, 0, width, height, pixelsValues);
		
		for (int i = 0; i < pixelsValues.length; i = i + width) {
			// Encontra a primeira coluna com um pixel (ou mais) que pertence(m) a regiao de interesse.
			for (int j = i; j <= i + width - 1; j++) {
				if(pixelsValues[j] <= thr) continue;
				
				atual = j % width;
				
				if( atual < limEsqImg ) {
					limEsqImg = atual;
				}
				
				break;
			}
		}
		
		for (int i = pixelsValues.length - 1; i >= 0; i = i - width) {
			// Encontra a ultima coluna com um pixel (ou mais) que pertence(m) a regiao de interesse.
			for (int j = i; j >= i - width + 1; j--) {
				if(pixelsValues[j] <= thr) continue;
				
				atual = j % width;
				
				if( atual > limDirImg ) {
					limDirImg = atual;
				}
				
				break;
			}
		}
		
		if(limDirImg < 0)
			return 0.0;
		
		return (((double) (limDirImg - limEsqImg) + 1) / (double) width);
	}	
}
