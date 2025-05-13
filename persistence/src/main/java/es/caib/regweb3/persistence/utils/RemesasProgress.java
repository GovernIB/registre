package es.caib.regweb3.persistence.utils;

/**
 * 
 * @author jamal
 *
 */
public class RemesasProgress {

	private int total;
	private int procesadas;
	private int duplicadas;
	
	public RemesasProgress(int total) {
		this.total = total;
		this.procesadas = 0;
	}

	public void incrementarDuplicadas() {
		if (duplicadas < total) {
			duplicadas++;
		}
	}
	
	public void incrementarProcesadas() {
		if (procesadas < total) {
			procesadas++;
		}
	}

	public int getTotal() {
		return total;
	}

	public int getProcesadas() {
		return procesadas;
	}

	public int getDuplicadas() {
		return duplicadas;
	}
	
}
