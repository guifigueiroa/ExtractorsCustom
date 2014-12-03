package similarityFunctions;

import java.awt.Image;
import java.util.Vector;
import javax.media.jai.PlanarImage;
import extractors.IExtractor;

/**
 * Implementa a dist?ncia Manhattan.
 * @author Vagner M. Gon?alves
 */
public class ManhattanSimilarity implements ISimilarity {
	
	@SuppressWarnings("rawtypes")
	Vector vectorOfExtractors = new Vector(); // Vetor de extratores de
											  // caracter?sticas.
	
	/**
	 * Adiciona um <i>extrator de caracter?stica</i> ao <i>vetor de
	 * extratores</i>.
	 * @param extractor Extrator a ser adicionado.
	 */
	@SuppressWarnings("unchecked")
	public void addExtractor(IExtractor extractor) {
		vectorOfExtractors.add(extractor);
	}
	
	/**
	 * Computa a dist?ncia entre os vetores de caracter?sticas da imagem
	 * modelo e a da imagem em teste.
	 * @param vModel Vetor de caracter?sticas da imagem modelo.
	 * @param vTest Vetor de caracter?sticas da imagem em teste.
	 * @return Valor num?rico double representando a dist?ncia entre os
	 * vetores.
	 */
	@SuppressWarnings("rawtypes")
	public double computeSimilarity(double[] vModel, double[] vTest) {
		double d1, d2, distance = 0.0;
		for(int i = 0; i < vectorOfExtractors.size(); i++) {
			d1 = vModel[i];
			d2 = vTest[i];
			distance += Math.abs(d1 - d2);
		}
		return distance;
	}
	
	/**
	 * Obt?m nome da fun??o.
	 * @return {@link String} representando o nome da fun??o.
	 */
	public String getName() {
		return "Manhattan";
	}
	
	/**
	 * Obt?m <i>vetor de caracter?sticas</i> da imagem argumento.
	 * @return Vetor de caracter?sticas do tipo {@link Vector}.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public double[] getVectorSimilarity(Image imLoaded) {
		double[] vSimilarities = new double[vectorOfExtractors.size()];
		Object objAux;
		IExtractor eAux;
		for(int i = 0; i < vectorOfExtractors.size(); i++) {
			objAux = vectorOfExtractors.get(i);
			eAux = (IExtractor) objAux;
			vSimilarities[i] = eAux.computeValue(imLoaded);
		}
		return vSimilarities;
	}
	
	/**
	 * Obt?m <i>vetor de extratores</i> desta fun??o.
	 * @return Vetor de extratores do tipo {@link Vector}.
	 */
	@SuppressWarnings("rawtypes")
	public Vector getVector() {
		return vectorOfExtractors;
	}
}
