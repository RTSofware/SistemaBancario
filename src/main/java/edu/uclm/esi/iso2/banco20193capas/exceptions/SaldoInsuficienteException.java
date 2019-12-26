package edu.uclm.esi.iso2.banco20193capas.exceptions;

/**
 * The Class SaldoInsuficienteException.
 */
public class SaldoInsuficienteException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8230647141587135881L;

	/**
	 * Instantiates a new saldo insuficiente exception.
	 */
	public SaldoInsuficienteException() {
		super("Saldo insuficiente para el importe solicitado");
	}
}
