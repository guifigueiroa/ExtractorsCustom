
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import similarityFunctions.*;
import extractors.*;


public class Main {

	public static void main(String[] args){
		try {
			
			Image image1 = ImageIO.read (new File("/Users/guilhermefigueiroa/Dropbox/workspace/ExtractorsCustom/src/images/monalisa1.jpg"));
			Image image2 = ImageIO.read (new File("/Users/guilhermefigueiroa/Dropbox/workspace/ExtractorsCustom/src/images/monalisa2.jpg"));
			
			// Calcula medidas de similaridade
			double result1 = getSimilarity(new CanberraSimilarity(), image1, image2);
			double result2 = getSimilarity(new ChebychevSimilarity(), image1, image2);
			double result3 = getSimilarity(new Chi2Similarity(), image1, image2);
			double result4 = getSimilarity(new CosineSimilarity(), image1, image2);
			double result5 = getSimilarity(new EuclideanSimilarity(), image1, image2);
			double result6 = getSimilarity(new JeffreySimilarity(), image1, image2);
			double result7 = getSimilarity(new ManhattanSimilarity(), image1, image2);
			double result8 = getSimilarity(new ModifiedTrigonometricSimilarity(), image1, image2);
			double result9 = getSimilarity(new TrigonometricSimilarity(), image1, image2);
			
			System.out.println(result1 + "\n" + result2 + "\n" + result3 + "\n" + result4 
					+ "\n" + result5 + "\n" + result6 + "\n" + result7 + "\n" + result8 
					+ "\n" + result9);
			
			double result = result1 + result2 + result3 + result4 
					+ result5 + result6 + result7 + result8 
					+ result9;
			
			System.out.println("result = " + result);
			
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
		//sim.addExtractor(new ExtractorSignature());//Retorna sempre NaN
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
