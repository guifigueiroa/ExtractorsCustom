package extractors;


import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.media.jai.PlanarImage;
import extractors.IExtractor;

public class ExtractorPerimeter implements IExtractor {
	
	private int thr = 0;
	
	@Override
	public String getName() {
		return "Perimeter";
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
		
		int width = planarImg.getWidth();
		int height = planarImg.getHeight();
		int numBands = planarImg.getNumBands();
		
		double perimetro = 0.0;
		
		int pixelsValues[] = new int[width * height * numBands];
		planarImg.getData().getPixels(0, 0, width, height, pixelsValues);
		
		double maxPerimeter = (width + height) * 2;
		
		for (int i = width; i < pixelsValues.length - width; i++) {
			if (((i % width) == 0) || (((i + 1) % width) == 0)) continue;
			
			try {
				if ((pixelsValues[i] > thr) && 
						((pixelsValues[i + 1] == thr) || 
						 (pixelsValues[i - 1] == thr) || 
						 (pixelsValues[i + width] == thr) || 
						 (pixelsValues[i - width] == thr) || 
						 (pixelsValues[i - width - 1] == thr) || 
						 (pixelsValues[i - width + 1] == thr) || 
						 (pixelsValues[i + width - 1] == thr) || 
						 (pixelsValues[i + width + 1] == thr))) {
					
					perimetro++;
				}
			}
			catch (Exception e) {
				System.out.println("********** Exception thrown!");
			}
		}
		
		return (perimetro / maxPerimeter);
	}
}
