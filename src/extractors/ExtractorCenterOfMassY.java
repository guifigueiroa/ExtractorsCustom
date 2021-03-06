package extractors;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.media.jai.PlanarImage;

/**
 * Extrai a coordenada Y do pixel centro de massa da imagem segmentada.
 * 
 * Como se trata de uma caracteristica e, por padrao, todas as caracteristicas estao normalizadas
 * no intervalo [0,1], esta coordenada e dividida pela maxima coordenada Y da imagem.
 * 
 */
public class ExtractorCenterOfMassY implements IExtractor {
	
	@Override
	public String getName() {
		return "Center of Mass Y";
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) {
		System.out.println("There is no adjustable parameters.");
	}
	
	@Override
	public Object getProperty(String propertyName) {
		System.out.println("There is no adjustable parameters.");
		return null;
	}
	
	@Override
	public Object[] getProperties() {
		System.out.println("There is no adjustable parameters.");
		return null;
	}
	
	@Override
	public String[] getPropertiesNames() {
		System.out.println("There is no adjustable parameters.");
		return null;
	}
	
	@Override
	public double computeValue(Image image) {
		BufferedImage bufferImage = (BufferedImage) image;
		PlanarImage planarImg = PlanarImage.wrapRenderedImage(bufferImage);
		
		int width = planarImg.getWidth();
		int height = planarImg.getHeight();
		int numBands = planarImg.getNumBands();
		double sumY = 0;
		double totalColor = 0;
		int[] pixelsValues = new int[width * height * numBands];
		
		planarImg.getData().getPixels(0, 0, width, height, pixelsValues); // Raster
		
		int k = 0;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				sumY += i * pixelsValues[k];
				totalColor += pixelsValues[k];
				k++;
			}
		}
		
		if((totalColor == 0) || (height == 1)) return 0.0;
		
		return (sumY / totalColor) / (double) (height - 1);
	}
}
