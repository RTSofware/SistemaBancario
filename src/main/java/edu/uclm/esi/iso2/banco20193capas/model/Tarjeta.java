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
 * Representa una tarjeta bancaria, bien de débito o bien de crédito. Una
 * {@code Tarjeta} está asociada a un {@code Cliente} y a una {@code Cuenta}.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Tarjeta {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	/**
	 * Id de la Tarjeta.
	 */
	protected Long id;

	/**
	 * Pin de la Tarjeta. 
	 */
	protected Integer pin;
	
	/**
	 * Booleano que determina si la tarjeta está activa o no.
	 */
	protected Boolean activa;
	
	/**
	 * Numero de intentos.
	 */
	protected Integer intentos;

	/**
	 * Compra.
	 */
	@Transient
	protected Compra compra;

	/**
	 * Titular de la tarjeta.
	 */
	@ManyToOne
	protected Cliente titular;

	/**
	 * Cuenta a la que pertenece la tarjeta.
	 */
	@ManyToOne
	protected Cuenta cuenta;

	/**
	 * Crea una tarjeta.
	 */
	public Tarjeta() {
		activa = true;
		this.intentos = 0;
		final SecureRandom dado = new SecureRandom();
		pin = 0;
		for (int i = 0; i <= 3; i++) {
			pin = (int) (pin + dado.nextInt(10) * Math.pow(10, i));
		}
			
	}

	/**
	 * Comprueba el pin.
	 * @param pinEntrada pin de entrada
	 * @throws TarjetaBloqueadaException si no esta activa
	 * @throws PinInvalidoException si el pin es incorrecto
	 */
	protected void comprobar(final int pinEntrada) throws TarjetaBloqueadaException, PinInvalidoException {
		if (!this.isActiva()) {
			throw new TarjetaBloqueadaException();
		}
		if (this.pin != pinEntrada) {
			this.intentos++;
			if (intentos == 3) {
				bloquear();
			}
			throw new PinInvalidoException();
		}
	}

	/**
	 * Permite confirmar una compra que se ha iniciado por Internet. El método
	 * {@link #comprarPorInternet(int, double)} devuelve un token que debe ser
	 * introducido en este método.
	 * @param tokenEntrada El token que introduce el usuario. Para que la compra se
	 *              confirme, ha de coincidir con el token devuelto por
	 *              {@link #comprarPorInternet(int, double)}
	 * @throws TokenInvalidoException     Si el {@code token} introducido es
	 *                                    distinto del recibido desde
	 *                                    {@link #comprarPorInternet(int, double)}
	 * @throws ImporteInvalidoException   Si el importe{@literal <}=0
	 * @throws SaldoInsuficienteException Si el saldo de la cuenta asociada a la
	 *                                    tarjeta (en el caso de
	 *                                    {@link TarjetaDebito}) es menor que el
	 *                                    importe, o si el crédito disponible en la
	 *                                    tarjeta de crédito es menor que el importe
	 * @throws TarjetaBloqueadaException  Si la tarjeta está bloqueada
	 * @throws PinInvalidoException       Si el pin que se introdujo es inválido
	 */
	public void confirmarCompraPorInternet(final int tokenEntrada) throws TokenInvalidoException, 
	ImporteInvalidoException, SaldoInsuficienteException, TarjetaBloqueadaException, PinInvalidoException {
		if (tokenEntrada != this.compra.getToken()) {
			this.compra = null;
			throw new TokenInvalidoException();
		}
		this.comprar(this.pin, this.compra.getImporte());
	}

	/**
	 * Bloquear
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
	 * Setter de id
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

	public abstract void sacarDinero(int pin, double importe) throws ImporteInvalidoException,
			SaldoInsuficienteException, TarjetaBloqueadaException, PinInvalidoException;

	public abstract Integer comprarPorInternet(int pin, double importe) throws TarjetaBloqueadaException,
			PinInvalidoException, SaldoInsuficienteException, ImporteInvalidoException;
	
	public abstract void comprar(int pin, double importe) throws ImporteInvalidoException, SaldoInsuficienteException,
			TarjetaBloqueadaException, PinInvalidoException;

	/**
	 * Permite cambiar el pin de la tarjeta.
	 * @param pinViejo El pin actual
	 * @param pinNuevo El pin nuevo (el que desea establecer el usuario)
	 * @throws PinInvalidoException Si el {@code pinViejo} es incorrecto
	 */
	public abstract void cambiarPin(int pinViejo, int pinNuevo) throws PinInvalidoException;
}
