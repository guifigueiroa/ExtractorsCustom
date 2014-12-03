package extractors;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import javax.media.jai.PlanarImage;

public class ExtractorArea implements IExtractor {
	
	private int thr = 0;
	
	@Override
	public String getName() {
		return "Area";
	}
	
	@Override
	public void setProperty(String propertieName, Object propertieNewValue) {
		if (propertieName.equals("thr")) {
			Integer newValue = new Integer(propertieNewValue.toString());
			thr = newValue;
		}
		else {
			System.out.println("Error: The property " + propertieName + " can not be changed.");
		}
	}
	
	@Override
	public Object getProperty(String propertieName) {
		if (propertieName.equals("thr")) return thr;
		else return null;
	}
	
	@Override
	public Object[] getProperties() {
		Object[] valuesObj = new Object[] { thr };
		return valuesObj;
	}
	
	@Override
	public String[] getPropertiesNames() {
		return new String[] { "thr" };
	}
	
	@Override
	public double computeValue(Image image) {

		BufferedImage bufferImage = (BufferedImage) image;
		PlanarImage planarImg = PlanarImage.wrapRenderedImage(bufferImage);
		
		Raster pixels = planarImg.getData();
		int width = planarImg.getWidth();
		int height = planarImg.getHeight();
		int numBands = planarImg.getNumBands();
		
		int[] pixelsValues = new int[width * height * numBands];
		pixels.getPixels(0, 0, width, height, pixelsValues);
		
		double area = 0;
		
		for (int i = 0; i < pixelsValues.length; i++) {
			if (pixelsValues[i] > thr) {
				area++;
			}
		}
		
		return area / ((double) pixelsValues.length);
	}

}
