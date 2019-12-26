package edu.uclm.esi.iso2.banco20193capas.exceptions;

/**
 * The Class TarjetaBloqueadaException.
 */
public class TarjetaBloqueadaException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2929538693963971794L;

	/**
	 * Instantiates a new tarjeta bloqueada exception.
	 */
	public TarjetaBloqueadaException() {
		super("La tarjeta esta bloqueada");
	}
}
