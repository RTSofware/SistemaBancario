package edu.uclm.esi.iso2.banco20193capas.exceptions;

/**
 * The Class CuentaSinTitularesException.
 */
public class CuentaSinTitularesException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5926431854385978310L;

	/**
	 * Instantiates a new cuenta sin titulares exception.
	 */
	public CuentaSinTitularesException() {
		super("Falta indicar el titular o titulares");
	}
}
