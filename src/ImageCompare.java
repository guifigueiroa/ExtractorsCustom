
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import similarityFunctions.*;
import extractors.*;


public class ImageCompare {

	public static void main(String[] args){
		try {
			
			Image image1 = ImageIO.read(new File(ImageCompare.class.getResource("image1.jpg").getPath()));
			Image image2 = ImageIO.read(new File(ImageCompare.class.getResource("image2.jpg").getPath()));
			

			double result = getSimilarity(new ModifiedTrigonometricSimilarity(), image1, image2);
			
			System.out.println(result);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static double getSimilarity(ISimilarity sim, Image im1, Image im2){
		// Adiciona todos os extratores
		sim.addExtractor(new ColorExtract());
		sim.addExtractor(new ExtractorArea());
		sim.addExtractor(new ExtractorPerimeter());
		//sim.addExtractor(new ExtractorSignature()); //Retorna sempre NaN
		sim.addExtractor(new ExtractorWidth());
		sim.addExtractor(new ExtractorHeight());
		sim.addExtractor(new ExtractorCenterOfMassX());
		sim.addExtractor(new ExtractorCenterOfMassY());
		sim.addExtractor(new ExtractorNumObjects());
		
		double[] v1 = sim.getVectorSimilarity(im1);
		double[] v2 = sim.getVectorSimilarity(im2);
		
		return sim.computeSimilarity(v1, v2);
	}
}
