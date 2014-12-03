package similarityFunctions;
/**
 * Esta interface deve ser implementada por toda fun??o de similaridade que ser? instalada
 * na ferramenta para que tenha seu uso como v?lido.
 * @author rafael
 *
 */
import javax.media.jai.*;

import extractors.IExtractor;

import java.awt.Image;
import java.util.Vector;
public interface ISimilarity {
	
	String getName(); //deve retornar uma String com o nome da classe principal da Fun??o de Similaridade
	
	void addExtractor(IExtractor extractor); //deve ser usado para adicionar um extrator ? fun??o
	
	double computeSimilarity(double[] vectModel, double[] vectTesting); //deve realizar a principal opera??o da fun??o de similaridade, que ? utilizar os extratores para retornar um valor
	
	double[] getVectorSimilarity(Image image);
	
							}
