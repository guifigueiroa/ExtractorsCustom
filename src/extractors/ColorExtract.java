package extractors;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;

import com.sun.media.jai.util.PlanarImageProducer;

import extractors.IExtractor;

public class ColorExtract implements IExtractor {
	
	private int x = 0; 
	private int y = 0;
	private int width = 1;
	private int height = 1;
	
	
	
	@Override
	public double computeValue(Image image) {
		BufferedImage bufferImage = (BufferedImage) image;
		PlanarImage pI = PlanarImage.wrapRenderedImage(bufferImage);
		
		int pow = (bufferImage.getColorModel().getPixelSize()/pI.getNumBands());
		//capturo o numero de bits da imagem, na maioria dos casos 8 bits por pixel
		
		int maxValuePixels =(int)(Math.pow(2,pow));
		// capturo o valor maximo de cada pixel individualmente, por exemplo 256
		//System.out.println("maxValuePixels "+maxValuePixels+" pow "+pow);
		
		Raster raster = pI.getData();
		int[] pixel = new int[pI.getNumBands()];
		//verifica qual ? o sistemas de cores da imagem
		double totalSum = 0;
		raster.getPixel(0,0, pixel);
		//pega os pixels
		
		
		for (int w=x; w<width;w++)	
			for (int h=y; h<height;h++)
			{
				raster.getPixel(w, h,  pixel);				
				totalSum = totalSum+ mediaPonderada(pixel);	
				// tira uma m?dia dos pixels
			}
	
		/*
		try{
		ImageIO.write(bufferImage2, "PNG", new File("c:/region2.png"));
		}catch(Exception e){System.out.println("pau");}
		*/
		
		double average =  totalSum/(height*width);
		
	/*	for (int w=0; w<pI.getWidth();w++)	
			for (int h=0; h<pI.getHeight();h++)
			{
				raster.getPixel(w,h, pixel);
				totalSum = totalSum+ mediaPonderada(pixel);	
				// tira uma m?dia dos pixels
			}
	
		double average =  totalSum/(pI.getHeight()*pI.getWidth());
	*/	
		
		
		
		return (average/(maxValuePixels-1));
	}

	
	
	private double mediaPonderada(int[] values)
	{
		int lenght = values.length; //em geral, tres bandas
	
		double sum = 0;
		double pesos = 0;
		for (int i=0; i<lenght; i++)
		{
			sum = sum+(values[i]*(i+1));
			pesos =  pesos+(i+1);			
		}
		// faz-se uma media dos valores de determinado pixel pegando cada um dos valores de sua banda
		return (double)(sum/pesos);
	}
	
	
	@Override
	public String getName() {
		return "color";
	}

	@Override
	public Object[] getProperties() {
		Object[] properties = new Object[] {x, y, width, height};
		return properties;
	}

	@Override
	public Object getProperty(String propertyName) {
		
		if (propertyName.toUpperCase().equals("X"))
		{
			return x;
		}
		else if (propertyName.toUpperCase().equals("Y"))
		{
			return y;
		}
		else if (propertyName.toUpperCase().equals("WIDTH"))
		{
			return width;
		}	
		else if (propertyName.toUpperCase().equals("HEIGHT"))
		{
			return height;
		}	
		else		
		return null;
	}

	@Override
	public String[] getPropertiesNames() {		
		return new String[]{"x", "y", "width", "heitght"};
	}

	@Override
	public void setProperty(String prop, Object arg1) {
		
		Integer value ;
		value =  (Integer) arg1;
		if (prop.toUpperCase().equals("X"))
		{	x = value;
		}
		else if (prop.toUpperCase().equals("Y"))
		{
			y = value;
		}
		else if (prop.toUpperCase().equals("WIDTH"))
		{
			width = value;
		}	
		else if (prop.toUpperCase().equals("HEIGHT"))
		{
			height = value;
		}	

	}

}
