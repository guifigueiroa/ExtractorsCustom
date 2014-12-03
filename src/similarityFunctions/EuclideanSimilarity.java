package similarityFunctions;

import java.awt.Image;
import java.util.Vector;

import javax.media.jai.PlanarImage;

import extractors.IExtractor;

public class EuclideanSimilarity implements ISimilarity {

	Vector vectorOfExtractors = new Vector();
	
	
	/**
	 * adiciona o objeto da classe extratora de caracater?sticas em um vetor
	 */
	
	public void addExtractor(IExtractor extractor) {
		vectorOfExtractors.add(extractor);
	}

	

	@Override
	public String getName() {		
		return "Euclidean";
	}

	@Override
	public double[] getVectorSimilarity (Image imLoaded) {
		
		double[] vSimilarities = new double[vectorOfExtractors.size()];
		Object objAux = new Object();
		IExtractor eAux ;
		
		for (int i=0; i < vectorOfExtractors.size() ; i++)
		{
			objAux = vectorOfExtractors.elementAt(i); //pega o objeto na posi??o i do vetor
			
			eAux = (IExtractor) objAux; //coer??o do objeto para IExtractor   
			
			vSimilarities[i] = eAux.computeValue(imLoaded);
			//adiciona o valor double no ultimo indice do vetor de caracteristicas
		}
		
				return vSimilarities;
	}
	
	public Vector getVector()
	{
		return vectorOfExtractors;
	}
	
	@Override
	public double computeSimilarity(double[] vModel, double[] vTest) {
		 Double d1,d2,distancia=0.0;	
		
//		 System.out.println("Computando: ");
		for (int i=0 ; i < vModel.length ; i ++)
		{
			d1 = vModel[i];
			d2 = vTest[i];
			distancia += (d1 - d2)*(d1 - d2);		
//			System.out.println("Valores usados: " + d1 + " " + d2);
		}
//		System.out.println("Resultado: " + Math.sqrt(distancia));
		return Math.sqrt(distancia);
	}

}
