package edu.uclm.esi.iso2.banco20193capas.model;

/**
 * Clase Compra.
 */
public class Compra {

	/**
	 * Importe de la Compra.
	 */
	private double importe;
	/**
	 * Token de la Compra.
	 */
	private int token;

	/**
	 * Crea una compra.
	 * @param importeCompra Importe de la Compra
	 * @param tokenCompra   Token de la Compra
	 */
	public Compra(final double importeCompra, final int tokenCompra) {
		this.importe = importeCompra;
		this.token = tokenCompra;
	}

	/**
	 * Getter de importe.
	 * @return importe de la compra
	 */
	public double getImporte() {
		return importe;
	}

	/**
	 * Setter de Importe.
	 * @param importeCompra importe de la compra
	 */
	public void setImporte(final double importeCompra) {
		this.importe = importeCompra;
	}
	
	/**
	 * Getter de token.
	 * @return token de la compra
	 */
	public int getToken() {
		return token;
	}

	/**
	 * Setter de token.
	 * @param tokenCompra token de la compra
	 */
	public void setToken(final int tokenCompra) {
		this.token = tokenCompra;
	}

}
