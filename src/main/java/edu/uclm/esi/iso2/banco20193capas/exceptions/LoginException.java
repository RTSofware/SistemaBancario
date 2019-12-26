package edu.uclm.esi.iso2.banco20193capas.exceptions;

/**
 * The Class LoginException.
 */
public class LoginException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2244339538229915272L;

	/**
	 * Instantiates a new login exception.
	 */
	public LoginException() {
		super("Credenciales invalidas");
	}
}
