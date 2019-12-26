package edu.uclm.esi.iso2.banco20193capas.exceptions;

/**
 * The Class ClienteNoAutorizadoException.
 */
public class ClienteNoAutorizadoException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -262756992297932533L;

	/**
	 * Instantiates a new cliente no autorizado exception.
	 *
	 * @param nif       the nif
	 * @param clienteID the cliente ID
	 */
	public ClienteNoAutorizadoException(final String nif,
			final Long clienteID) {
		super("El cliente con NIF " + nif
				+
				" no esta autorizado para operar en la cuenta "
				+
				clienteID);
	}
}
