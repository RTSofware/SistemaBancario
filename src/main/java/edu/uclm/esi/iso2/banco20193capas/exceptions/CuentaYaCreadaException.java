package edu.uclm.esi.iso2.banco20193capas.exceptions;
/**
 * Class CuentaYaCreadaException.
 */
public class CuentaYaCreadaException extends Exception {

	private static final long serialVersionUID = -8447498273059715681L;

	/**
	 * Instantiates a new cuenta ya creada exception.
	 */
	public CuentaYaCreadaException() {
		super("La cuenta esta creada y no admite mas titulares");
	}
}
