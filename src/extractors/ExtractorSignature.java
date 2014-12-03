package extractors;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import javax.media.jai.PlanarImage;
import extractors.IExtractor;

public class ExtractorSignature implements IExtractor {
	
	private double angleRadius = 10.0;
	
	@Override
	public String getName() {
		return "Signature";
	}
	
	@Override
	public void setProperty(String propertieName, Object propertieNewValue) {
		if (propertieName.equals("angleRadius")) {
			Double value = new Double(propertieNewValue.toString());
			setAngleRadius(value);
		}
		else
			System.out.println("Error: The property " + propertieName + " can not be changed.");
	}
	
	@Override
	public Object getProperty(String propertieName) {
		if (propertieName.equals("angleRadius"))
			return new Double(getAngle());
		else
			return null;
	}
	
	@Override
	public Object[] getProperties() {
		return new Object[] { angleRadius };
	}
	
	@Override
	public String[] getPropertiesNames() {
		return new String[] { "angleRadius" };
	}
	
	@Override
	public double computeValue(Image image) {
		
		BufferedImage bufferImage = (BufferedImage) image;
		PlanarImage planarImg = PlanarImage.wrapRenderedImage(bufferImage);
		
		boolean side; // side of the breast
		int sideVector = (int) (180 / getAngle()) + 1;
		double[] distanceValues = new double[sideVector]; // distances between each point and the center of the breast
		int valueRadius = 0;
		
		int width = planarImg.getWidth(); // image weidth
		int height = planarImg.getHeight(); // image height 
		int numBands = planarImg.getNumBands(); // bands number of the image pixels
		
		Raster inputRaster = planarImg.getData();
		int[] pixels = new int[numBands * width * height];
		inputRaster.getPixels(0, 0, width, height, pixels);
		
		int offset, flagFim = 0;
		long PixelInicial = 0, PixelFinal = 0, xCentro, yCentro;
		
		int h1_3 = (int) height / 3;
		int firstLine, lastLine, centralLine;
		
		firstLine = getFirstSegPosition(pixels) / width;
		lastLine = getLastSegPosition(pixels) / width;
		centralLine = (int) ((firstLine + lastLine) / 2);
		
		side = getImageSide(pixels, centralLine, width);
		
		if (side) {
			/*
			 * Percorre a ??ltima coluna procurando o centro da mama, caso n??o encontre, vai para a pen??ltima
			 * e assim por diante, at?? que seja encontrado pelo menos um pixel preto.
			 * 
			 */
			int w = width;
			
			do {
				w = w - 1;
				for (int h = 0; h < height; h++) {
					offset = h * width * numBands + w * numBands;
					for (int band = 0; band < numBands; band++)
						if (pixels[offset + band] == 0) {
							flagFim = 1;
							if (h < h1_3) {
								flagFim = 0;
								PixelInicial = h;
							}
							else PixelFinal = h;
						}
				}
			} while (flagFim == 0);
			
			xCentro = w;
		}
		
		else {
			// Percorre a primeira coluna procurando o centro da mama
			int w = 0;
			
			do {
				w = w + 1;
				for (int h = 0; h < height; h++) {
					offset = h * width * numBands + w * numBands;
					for (int band = 0; band < numBands; band++)
						if (pixels[offset + band] == 0) {
							flagFim = 1;
							if (h < h1_3) {
								flagFim = 0;
								PixelInicial = h;
							}
							else PixelFinal = h;
						}
				}
			} while (flagFim == 0);
			
			xCentro = w;
		}
		
		yCentro = centralLine;
		
		/*
		 * Lan??amento de raios a partir do ponto central definido acima a fim de encontrar a borda da mama.
		 */
		long vetorX[] = new long[362];
		long vetorY[] = new long[362];
		int x, y;
		double Angulo, AnguloRadiano, Xaux, Yaux;
		int posVetor = 0;
		long menorY = height, maiorY = 0;
		
		// Se a mama esta do lado direito
		if (side) {
			// Vai indo de angleRadius em angleRadius graus, de 90o a 270o
			for (Angulo = 90.0; Angulo < 271; Angulo += getAngle()) {
				AnguloRadiano = Math.toRadians(Angulo);
				flagFim = 0; // ainda n??o chegou ao fim
				x = (int) xCentro;
				y = (int) yCentro;
				valueRadius = 0;
				Xaux = (double) xCentro;
				Yaux = (double) yCentro;
				
				while ((flagFim == 0) && (x < width) && (x > 0) && (y > PixelInicial) && (y < PixelFinal)) {
					Xaux = Xaux + Math.cos(AnguloRadiano);
					x = (int) Xaux;
					Yaux = Yaux - Math.sin(AnguloRadiano);
					y = (int) Yaux;
					offset = y * width * numBands + x * numBands;
					
					for (int band = 0; band < numBands; band++) {
						if (pixels[offset + band] == 0) flagFim = 1;
					}
					
					valueRadius++; // contagem de pixels do raio
				}
				
				vetorX[posVetor] = x;
				vetorY[posVetor] = y;
				
				try {
					distanceValues[posVetor] = valueRadius;
				}
				catch (Exception e) {
					System.out.println("********** Exception thrown! " + e.getMessage());
				}
				
				if (y > maiorY) maiorY = y;
				if (y < menorY) menorY = y;
				
				posVetor++;
				Xaux = x;
				Yaux = y;
			}
			
		}
		
		// Se a mama est?? do lado esquerdo
		else {
			// Vai indo de angleRadius em angleRadius graus, de 90o a 270o
			for (Angulo = 90.0; Angulo > -89.0; Angulo -= getAngle()) {
				if (Angulo < 0)
					AnguloRadiano = Math.toRadians(360 + Angulo);
				else
					AnguloRadiano = Math.toRadians(Angulo);
				
				flagFim = 0; // ainda n??o chegou ao fim
				x = (int) xCentro;
				y = (int) yCentro;
				valueRadius = 0;
				Xaux = (double) xCentro;
				Yaux = (double) yCentro;
				
				while ((flagFim == 0) && (x < width) && (x > 0) && (y > PixelInicial) && (y < PixelFinal)) {
					Xaux = Xaux + Math.cos(AnguloRadiano);
					x = (int) Xaux;
					Yaux = Yaux - Math.sin(AnguloRadiano);
					y = (int) Yaux;
					offset = y * width * numBands + x * numBands;
					
					for (int band = 0; band < numBands; band++) {
						if (pixels[offset + band] == 0) flagFim = 1;
					}
					
					valueRadius++; // contagem de pixels do raio
				}
				
				vetorX[posVetor] = x;
				vetorY[posVetor] = y;
				
				try {
					distanceValues[posVetor] = valueRadius;
				}
				catch (Exception e) {
					System.out.println("********** Exception thrown! " + e.getMessage());
				}
				
				if (y > maiorY) maiorY = y;
				if (y < menorY) menorY = y;
				
				posVetor++;
				Xaux = x;
				Yaux = y;
			}
		}
		
		Statistics teste = new Statistics();
		teste.setArray(distanceValues);
		
		return (teste.getDesvioPadrao() / teste.getMaiorRaio()); // retorno do desvio padrao normalizado pelo maior raio encontrado 
	}
	
	public double getAngle() {
		return angleRadius;
	}
	
	private int getFirstSegPosition(int[] rImage) {
		int firstSegPosition = -1;
		int count = 0;
		while ((firstSegPosition == -1) && (count < rImage.length)) {
			if (rImage[count] != 0) firstSegPosition = count;
			else count++;
		}
		return firstSegPosition;
	}
	
	private int getLastSegPosition(int[] rImage) {
		int lastSegPosition = -1;
		int count = rImage.length - 1;
		
		while ((lastSegPosition == -1) && (count >= 0)) {
			if (rImage[count] != 0) lastSegPosition = count;
			else count--;
		}
		
		return lastSegPosition;
	}
	
	private boolean getImageSide(int[] arrayOfPixels, int line, int imageWidth) {
		boolean rightside = true; // default Imagem mamografica do lado direto 
		
		if (arrayOfPixels[(line * imageWidth) + (imageWidth - 2)] == 0)
			rightside = false; // Imagem mamografica do lado esquerdo
		
		return rightside;
	}
	
	public void setAngleRadius(double valueAngle) {
		if ((valueAngle <= 90.0) && (valueAngle > 0.5))
			angleRadius = valueAngle;
		else
			System.out.println("It is impossible to change the angle to " + valueAngle);
	}
}
