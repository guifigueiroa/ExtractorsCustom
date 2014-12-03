package extractors;


import java.awt.Image;

import javax.media.jai.*;
public interface IExtractor {
	

	String getName(); //m?todo que deve retornar o nome do Extrator de caracter?sticas

	void setProperty(String propertyName, Object propertyValue) ; // ajusta o valor de uma propriedade atrav?s de seu nome 
	//deve lan?ar uma exce??o quando o par?metro estiver como o nome incorreto ou quando o objeto tiver tipo incorreto
	
	//void setWindow(Object windowValue);
	
	Object getProperty(String propertyName);// retorna a instancia de determinada propriedade em teste
	
	Object[] getProperties(); //retorna um vetor com todas as propriedades de determinada imagen em teste
	
	String[] getPropertiesNames(); //retorna o nome das propriedades para o referido Extrator de Cracter?sticas de Imagens
	
    double computeValue(Image imageLoaded); //m?todo respons?vel por computar e retornar o valor extra?do daquela caracter?stica da imagem e retorn?-lo em um tipo double
    											  //funciona tbm como um set (ajustar) Imagem 
}
