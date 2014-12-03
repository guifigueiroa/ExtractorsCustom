package extractors;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.LinkedList;
import javax.media.jai.PlanarImage;

/**
 * Extrai o numero de objetos (componentes) em uma imagem segmentada.
 * 
 * Normaliza o valor obtido dividindo-o pelo valor do parametro {@code normalizer},
 * definido pelo usuario.
 * 
 * AUMENTAR MEMORIA: -Xmx1586m
 */
public class ExtractorNumObjects implements IExtractor {
	
	private int normalizer = 10;
	private int thr = 0;
	private int[][] pixelsMatrix;
	private LinkedList<Coordenadas> toVisit = new LinkedList<Coordenadas>();
	
	@Override
	public String getName() {	
		return "Number of Objects";
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyNewValue) {
		if (propertyName.equals("normalizer")) {
			Integer newValue = new Integer(propertyNewValue.toString());
			
			if(newValue.intValue() > 0) {
				normalizer = newValue;
			}
			else {
				System.out.println("Error: The property " + propertyName + " can not be changed.");
			}
		}
		else if(propertyName.equals("thr")) {
			Integer newValue = new Integer(propertyNewValue.toString());
			thr = newValue;
		}
		else {
			System.out.println("Error: The property " + propertyName + " can not be changed.");
		}
	}
	
	@Override
	public Object getProperty(String propertyName) {
		if (propertyName.equals("normalizer")) {
			return normalizer;
		}
		else if(propertyName.equals("thr")) {
			return thr;
		}
		else {
			return null;
		}
	}
	
	@Override
	public Object[] getProperties() {
		Object[] valuesObj = new Object[] {normalizer, thr};
		return valuesObj;
	}
	
	@Override
	public String[] getPropertiesNames() {
		return new String[] {"normalizer", "thr"};
	}
	
	@Override
	public double computeValue(Image image) {
		BufferedImage bufferImage = (BufferedImage) image;
		PlanarImage planarImg = PlanarImage.wrapRenderedImage(bufferImage);
		
		//System.out.println("thr = " + thr + ", norm = " + normalizer);
		// CAPTURA DOS PIXELS
		Raster pixels = planarImg.getData(); // captura de pixels da imagem em um raster
		int width = planarImg.getWidth();
		int height = planarImg.getHeight();
		int numBands = planarImg.getNumBands();
		int[] pixelsValues = new int[width*height*numBands] ; // criacao do vetor de pixels
		
		pixels.getPixels(0, 0, width, height, pixelsValues); // povoacao do vetor com os valores dos pixels
		
		// BINARIZACAO SEM PERDA DE INFORMACAO
		for (int i = 0; i < pixelsValues.length; i++) {
			if(pixelsValues[i] > thr) {
				// garante que todos os pixels que pertencem a regiao da imagem possuem um unico valor
				pixelsValues[i] = 255;
			}
			else {
				pixelsValues[i] = 0;
			}
		}
		
		// CRIACAO DA MATRIZ
		// Representando os pixels em uma matriz
		pixelsMatrix = new int[height][width]; // admite-se que numBands = 1
		int k = 0;
		
		for (int i = 0; i < pixelsMatrix.length; i++) {
			for (int j = 0; j < pixelsMatrix[i].length; j++) {
				pixelsMatrix[i][j] = pixelsValues[k];
				k++;
			}
		}
		
		// CONTANDO AS REGIOES
		Boolean feito = false;
		Integer numRegioes = 0;
		
		while(!feito) {
			Object[] result = checkingRegions(numRegioes);
			feito = (Boolean) result[0];
			numRegioes = (Integer) result[1];
			//System.out.println(numRegioes);
		}
		
		return ((double) numRegioes.intValue()) / ((double) normalizer);
	}
	
	private Object[] checkingRegions(Integer numRegioes) {
		for (int i = 0; i < pixelsMatrix.length; i++) {
			for (int j = 0; j < pixelsMatrix[i].length; j++) {
				if (pixelsMatrix[i][j] == 255) {
					changingColor(i, j);
					numRegioes++;
					return new Object[] {false, numRegioes};
				}
			}
		}
		
		return new Object[]{true, numRegioes};
	}
	
	private void changingColor(int i, int j) {
		toVisit.offer(new Coordenadas(i, j));
		pixelsMatrix[i][j] = 100;
		int[] iCoo = {-1, -1, -1, 0, 0, 1, 1, 1};
		int[] jCoo = {0, -1, 1, -1, 1, 0, -1, 1};
		Coordenadas atual;
		int iA, jA;
		
		while(!toVisit.isEmpty()) {
			atual = toVisit.poll();
			
			for (int k = 0; k < 8; k++) {
				iA = atual.getI() + iCoo[k];
				jA = atual.getJ() + jCoo[k];
				
				if(iA >= 0 && iA < pixelsMatrix.length && jA >= 0 && jA < pixelsMatrix[iA].length) {
					if (pixelsMatrix[iA][jA] == 255) {
						toVisit.offer(new Coordenadas(iA, jA));
						pixelsMatrix[iA][jA] = 100;
					}
				}
			}
		}
	}
	
	/**
	 * Classe interna auxiliar para armazenar as coordenadas de um pixel
	 * na matriz auxiliar
	 */
	public class Coordenadas {
		
		int i;
		int j;
		
		public Coordenadas(int i, int j) {
			this.i = i;
			this.j = j;
		}
		
		public int getI() {
			return this.i;
		}
		
		public int getJ() {
			return this.j;
		}
	}
}
