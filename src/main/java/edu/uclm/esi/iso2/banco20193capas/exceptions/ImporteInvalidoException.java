package edu.uclm.esi.iso2.banco20193capas.exceptions;

/**
 * The Class ImporteInvalidoException.
 */
public class ImporteInvalidoException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3442164126438326314L;

	/**
	 * Instantiates a new importe invalido exception.
	 *
	 * @param importe the importe
	 */
	public ImporteInvalidoException(final double importe) {
		super("El importe " + importe
				+ " no es valido para esta operacion");
	}
}
