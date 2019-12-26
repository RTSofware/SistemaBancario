package edu.uclm.esi.iso2.banco20193capas.exceptions;

/**
 * The Class CuentaInvalidaException.
 */
public class CuentaInvalidaException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -252828750659901956L;

	/**
	 * Instantiates a new cuenta invalida exception.
	 *
	 * @param numero the numero
	 */
	public CuentaInvalidaException(final Long numero) {
		super("La cuenta " + numero + " no existe");
	}

}
