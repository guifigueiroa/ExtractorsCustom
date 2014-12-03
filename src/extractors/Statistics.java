package extractors;

public class Statistics {
	
	private double array[];
	
	public double getSomaDosElementos() {
		double total = 0;
		for (int counter = 0; counter < array.length; counter++)
			total += array[counter];
		return total;
	}
	
	public double getSomaDosElementosAoQuadrado() {
		double total = 0;
		for (int counter = 0; counter < array.length; counter++)
			total += Math.pow(array[counter], 2);
		return total;
	}
	
	// Variancia amostral
	public double getVariancia() {
		double p1 = 1 / Double.valueOf(array.length - 1);
		double p2 = getSomaDosElementosAoQuadrado() - (Math.pow(getSomaDosElementos(), 2) / Double.valueOf(array.length));
		return p1 * p2;
	}
	
	public double getMedia() {
		return getSomaDosElementos() / array.length;
	}
	
	public double getVariancia2() {
		double total = 0;
		double media = getMedia();
		for (int counter = 0; counter < array.length; counter++) {
			double d = array[counter] - media;
			total += d * d;
		}
		return total / (double) (array.length - 1);
	}
	
	// Desvio Padrao amostral
	public double getDesvioPadrao() {
		return Math.sqrt(getVariancia());
	}
	
	public double getDesvioPadrao2() {
		return Math.sqrt(getVariancia());
	}
	
	public void setArray(double[] array) {
		this.array = array;
	}
	
	public double getMaiorRaio() {
		double maior = array[0];
		
		for (int i = 1; i < array.length; i++) {
			if (array[i] > maior) maior = array[i];
		}
		return maior;
	}
}
