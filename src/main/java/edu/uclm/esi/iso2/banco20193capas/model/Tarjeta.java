package edu.uclm.esi.iso2.banco20193capas.model;

import java.security.SecureRandom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import edu.uclm.esi.iso2.banco20193capas.exceptions.ImporteInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.PinInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.SaldoInsuficienteException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.TarjetaBloqueadaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.TokenInvalidoException;

/**
 * Representa una tarjeta bancaria, bien de debito o bien de credito. Una
 * {@code Tarjeta} esta asociada a un {@code Cliente} y a una {@code Cuenta}.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Tarjeta {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	/**
	 * Id de la Tarjeta.
	 */
	private Long id;

	/**
	 * Gets the intentos.
	 * @return the intentos
	 */
	public Integer getIntentos() {
		return intentos;
	}

	/**
	 * Sets the intentos.
	 * @param intentosParametro the new intentos
	 */
	public void setIntentos(final Integer intentosParametro) {
		this.intentos = intentosParametro;
	}

	/**
	 * Gets the compra.
	 * @return the compra
	 */
	public Compra getCompra() {
		return compra;
	}

	/**
	 * Sets the compra.
	 * @param compraParametro the new compra
	 */
	public void setCompra(final Compra compraParametro) {
		this.compra = compraParametro;
	}

	/**
	 * Gets the activa.
	 * @return the activa
	 */
	public Boolean getActiva() {
		return activa;
	}

	/**
	 * Pin de la Tarjeta.
	 */
	private Integer pin;

	/**
	 * Booleano que determina si la tarjeta está activa o no.
	 */
	private Boolean activa;

	/**
	 * Numero de intentos.
	 */
	private Integer intentos;

	/**
	 * Compra.
	 */
	@Transient
	private Compra compra;

	/**
	 * Titular de la tarjeta.
	 */
	@ManyToOne
	private Cliente titular;

	/**
	 * Cuenta a la que pertenece la tarjeta.
	 */
	@ManyToOne
	private Cuenta cuenta;

	/**
	 * Crea una tarjeta.
	 */
	public Tarjeta() {
		final int maxDigitosPin = 3;
		final int decimalNumber = 10;
		activa = true;
		this.intentos = 0;
		final SecureRandom dado = new SecureRandom();
		pin = 0;
		for (int i = 0; i <= maxDigitosPin; i++) {
			pin = (int) (pin + dado.nextInt(decimalNumber)
			* Math.pow(decimalNumber, i));
		}

	}

	/**
	 * Comprueba el pin.
	 *
	 * @param pinEntrada pin de entrada
	 * @throws TarjetaBloqueadaException si no esta activa
	 * @throws PinInvalidoException      si el pin es incorrecto
	 */
	protected void comprobar(final int pinEntrada)
			throws
			TarjetaBloqueadaException,
			PinInvalidoException {

		final int maxIntentos = 3;
		if (!this.isActiva()) {
			throw new TarjetaBloqueadaException();
		}
		if (this.pin != pinEntrada) {
			this.intentos++;
			if (intentos == maxIntentos) {
				bloquear();
			}
			throw new PinInvalidoException();
		}
	}

	/**
	 * Permite confirmar una compra que se ha iniciado por Internet.
	 * El metodo {@link #comprarPorInternet(int, double)}
	 * devuelve un token que debe ser introducido en este metodo.
	 * @param tokenEntrada El token que introduce el usuario. Para que la
	 * compra confirme, ha de coincidir con el token devuelto por
	 * {@link #comprarPorInternet(int, double)}
	 * @throws TokenInvalidoException     Si el {@code token} introducido es
	 *                                    distinto del recibido
	 * @throws ImporteInvalidoException   Si el importe{@literal <}=0
	 * @throws SaldoInsuficienteException Si el saldo de la cuenta asociada
	 *                                    a la tarjeta (en el caso de
	 * {@link TarjetaDebito}) es menor que el importe, o si el credito
	 * disponible en la tarjeta de es menor que el importe
	 * @throws TarjetaBloqueadaException  Si la tarjeta esta bloqueada
	 * @throws PinInvalidoException       Si el pin que se introdujo
	 * es invalido
	 */
	public void confirmarCompraPorInternet(final int tokenEntrada)
			throws
			TokenInvalidoException,
			ImporteInvalidoException,
			SaldoInsuficienteException,
			TarjetaBloqueadaException,
			PinInvalidoException {
		if (tokenEntrada != this.compra.getToken()) {
			this.compra = null;
			throw new TokenInvalidoException();
		}
		this.comprar(this.pin, this.compra.getImporte());
	}

	/**
	 * Bloquear.
	 */
	protected abstract void bloquear();

	/**
	 * Getter de id.
	 * @return id de la tarjeta
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter de id.
	 * @param identificador id de la tarjeta
	 */
	public void setId(final Long identificador) {
		this.id = identificador;
	}

	/**
	 * Geter de pin.
	 * @return pin de la tarjeta
	 */
	public Integer getPin() {
		return pin;
	}

	/**
	 * Setter de pin.
	 * @param pinTj pin de la Tarjeta
	 */
	public void setPin(final Integer pinTj) {
		this.pin = pinTj;
	}

	/**
	 * Getter de titular.
	 * @return titular de la tarjeta
	 */
	public Cliente getTitular() {
		return titular;
	}

	/**
	 * Setter de titular.
	 * @param titularTj titular de la tarjeta
	 */
	public void setTitular(final Cliente titularTj) {
		this.titular = titularTj;
	}

	/**
	 * Getter de cuenta.
	 * @return cuenta de la tarjeta
	 */
	public Cuenta getCuenta() {
		return cuenta;
	}

	/**
	 * Setter de cuenta.
	 * @param cuentaTj cuenta de la tarjeta
	 */
	public void setCuenta(final Cuenta cuentaTj) {
		this.cuenta = cuentaTj;
	}

	/**
	 * Valor de activa.
	 * @return true si la tarjeta está activa; false si está bloqueada
	 */
	public Boolean isActiva() {
		return activa;
	}

	/**
	 * Setter de activa.
	 * @param boolActiva booleano activa
	 */
	public void setActiva(final Boolean boolActiva) {
		this.activa = boolActiva;
	}

	/**
	 * Sacar dinero.
	 *
	 * @param pinTarjeta the pin
	 * @param importe the importe
	 * @throws ImporteInvalidoException the importe invalido exception
	 * @throws SaldoInsuficienteException the saldo insuficiente exception
	 * @throws TarjetaBloqueadaException the tarjeta bloqueada exception
	 * @throws PinInvalidoException the pin invalido exception
	 */
	public abstract void sacarDinero(int pinTarjeta, double importe)
			throws
			ImporteInvalidoException,
			SaldoInsuficienteException,
			TarjetaBloqueadaException,
			PinInvalidoException;

	/**
	 * Comprar por internet.
	 *
	 * @param pinTarjeta the pin
	 * @param importe the importe
	 * @return the integer
	 * @throws TarjetaBloqueadaException the tarjeta bloqueada exception
	 * @throws PinInvalidoException the pin invalido exception
	 * @throws SaldoInsuficienteException the saldo insuficiente exception
	 * @throws ImporteInvalidoException the importe invalido exception
	 */
	public abstract Integer comprarPorInternet(
			int pinTarjeta,
			double importe)
			throws
			TarjetaBloqueadaException,
			PinInvalidoException,
			SaldoInsuficienteException,
			ImporteInvalidoException;

	/**
	 * Comprar.
	 *
	 * @param pinTarjeta the pin
	 * @param importe the importe
	 * @throws ImporteInvalidoException the importe invalido exception
	 * @throws SaldoInsuficienteException the saldo insuficiente exception
	 * @throws TarjetaBloqueadaException the tarjeta bloqueada exception
	 * @throws PinInvalidoException the pin invalido exception
	 */
	public abstract void comprar(int pinTarjeta, double importe)
			throws
			ImporteInvalidoException,
			SaldoInsuficienteException,
			TarjetaBloqueadaException,
			PinInvalidoException;

	/**
	 * Permite cambiar el pin de la tarjeta.
	 * @param pinViejo El pin actual
	 * @param pinNuevo El pin nuevo (el que desea establecer el usuario)
	 * @throws PinInvalidoException Si el {@code pinViejo} es incorrecto
	 */
	public abstract void cambiarPin(int pinViejo, int pinNuevo)
			throws PinInvalidoException;
}
