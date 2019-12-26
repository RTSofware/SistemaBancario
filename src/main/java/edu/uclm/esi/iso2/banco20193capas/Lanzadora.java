package edu.uclm.esi.iso2.banco20193capas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaSinTitularesException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaYaCreadaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.ImporteInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.model.Cliente;
import edu.uclm.esi.iso2.banco20193capas.model.Cuenta;

/**
 * The Class Lanzadora.
 */
@SpringBootApplication
public class Lanzadora {

	/** The Constant INGRESO. */
	private static final double INGRESO = 1000.0;

	/** The Constant LOGGER. */
	private static final Logger LOGGER =
			LoggerFactory.getLogger(Lanzadora.class);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(final String[] args) {
		SpringApplication.run(Lanzadora.class, args);

		final Cliente pepe = new Cliente("12345X", "Pepe", "Perez");
		pepe.insert();

		final Cuenta cuenta = new Cuenta();
		try {
			cuenta.addTitular(pepe);
		} catch (CuentaYaCreadaException e) {
			LOGGER.error("Excepcion no esperada!: "
		+ e.getMessage());
		}
		try {
			cuenta.insert();
		} catch (CuentaSinTitularesException e) {
			LOGGER.error("Excepcion no esperada!: "
		+ e.getMessage());

		} catch (CuentaYaCreadaException e) {
			LOGGER.error("Excepcion no esperada!: "
		+ e.getMessage());

		}

		try {
			cuenta.ingresar(INGRESO);
		} catch (ImporteInvalidoException e) {
			LOGGER.error("Excepcion no esperada!: "
		+ e.getMessage());

		}
	}

}
