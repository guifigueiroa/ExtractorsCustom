package similarityFunctions;

import java.awt.Image;
import java.util.Vector;

import extractors.IExtractor;

public class CanberraSimilarity implements ISimilarity {
	
	@SuppressWarnings("rawtypes")
	Vector vectorOfExtractors = new Vector(); // vector of feature extractors
	
	@Override
	public String getName() {
		return "Canberra";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addExtractor(IExtractor extractor) {
		vectorOfExtractors.add(extractor);
	}
	
	public IExtractor[] getExtractors() {
		IExtractor vectorExt[] = new IExtractor[vectorOfExtractors.size()];
		
		for (int i = 0; i < vectorOfExtractors.size(); i++) {
			vectorExt[i] = (IExtractor) (vectorOfExtractors.elementAt(i));
		}
		
		return vectorExt;
	}
	
	@Override
	public double computeSimilarity(double[] vectModel, double[] vectTesting) {
		double d1, d2, distance = 0.0;
		
		for(int i = 0; i < vectModel.length; i++) {
			d1 = vectModel[i];
			d2 = vectTesting[i];
			
			if(d1 == 0.0 && d2 == 0.0) {
				distance += 0.0;
			}
			else {
				distance += ( Math.abs(d1-d2) / (Math.abs(d1) + Math.abs(d2)) );
			}
		}
		
		return distance;
	}
	
	@Override
	public double[] getVectorSimilarity(Image image) {
		double vectorOfSimilarities[] = new double[vectorOfExtractors.size()];
		
		for (int i = 0; i < vectorOfExtractors.size(); i++) {
			vectorOfSimilarities[i] = ((IExtractor) (vectorOfExtractors.elementAt(i))).computeValue(image);
		}
		
		return vectorOfSimilarities;
	}
}
