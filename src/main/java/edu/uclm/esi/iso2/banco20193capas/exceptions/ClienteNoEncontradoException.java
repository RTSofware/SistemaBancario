package edu.uclm.esi.iso2.banco20193capas.exceptions;

/**
 * The Class ClienteNoEncontradoException.
 */
public class ClienteNoEncontradoException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3725453645479642452L;

	/**
	 * Instantiates a new cliente no encontrado exception.
	 *
	 * @param nif the nif
	 */
	public ClienteNoEncontradoException(final String nif) {
		super("No se encuentra el cliente con NIF " + nif);
	}
}
