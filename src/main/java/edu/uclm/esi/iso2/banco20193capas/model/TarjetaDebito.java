package edu.uclm.esi.iso2.banco20193capas.model;

import java.security.SecureRandom;

import javax.persistence.Entity;

import edu.uclm.esi.iso2.banco20193capas.exceptions.ImporteInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.PinInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.SaldoInsuficienteException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.TarjetaBloqueadaException;

/**
 * The Class TarjetaDebito.
 */
@Entity
public class TarjetaDebito extends Tarjeta {

	/**
	 * Permite sacar dinero del cajero automatico.
	 * @param pin     El pin que introduce el usuario
	 * @param importe El {@code importe} que desea sacar
	 * @throws ImporteInvalidoException   Si el {@code importe<=0}
	 * @throws SaldoInsuficienteException Si el saldo de la cuenta asociada
	 * ({@link edu.uclm.esi.iso2.banco20193capas.model.Cuenta#getSaldo()
	 * Cuenta.getSaldo()}) a la tarjeta es menor que el importe
	 * @throws TarjetaBloqueadaException  Si la tarjeta esta bloqueada
	 * @throws PinInvalidoException       Si el pin introducido es distinto
	 * del pin de la tarjeta
	 */
	@Override
	public void sacarDinero(final int pin, final double importe)
			throws
			ImporteInvalidoException,
			SaldoInsuficienteException,
			TarjetaBloqueadaException,
			PinInvalidoException {

		comprobar(pin);
		this.setIntentos(0);
		this.getCuenta().retirar(importe);
	}

	/**
	 * Inicia una compra por Internet, que debe confirmarse despues (ver
	 * {@link #confirmarCompraPorInternet(int)}) mediante el token que
	 * devuelve este metodo.
	 * @param pin     El pin que introduce el usuario
	 * @param importe El importe de la compra
	 * @return Un token que debe introducirse en
	 *         {@link #confirmarCompraPorInternet(int)}
	 * @throws TarjetaBloqueadaException  Si la tarjeta esta bloqueada
	 * @throws PinInvalidoException       Si el pin introducido es distinto
	 * del pin de la tarjeta
	 * @throws SaldoInsuficienteException Si el saldo de la cuenta asociada
	 * a la tarjeta es menor que el importe
	 * @throws ImporteInvalidoException   Si el importe{@literal <}=0
	 */
	@Override
	public Integer comprarPorInternet(final int pin, final double importe)
			throws
			TarjetaBloqueadaException,
			PinInvalidoException,
			SaldoInsuficienteException,
			ImporteInvalidoException {

		final int maxDigitosPin = 3;
		final int decimalNumber = 10;
		final int defaultToken = 1234;

		comprobar(pin);
		this.setIntentos(0);
		SecureRandom dado = new SecureRandom();
		int token = 0;
		for (int i = 0; i <= maxDigitosPin; i++) {
			token = (int) (token + dado.nextInt(decimalNumber)
			* Math.pow(decimalNumber, i));
		}
		token = defaultToken;
		this.setCompra(new Compra(importe, token));
		return token;
	}

	/**
	 * Permite hacer un compra en un comercio.
	 * @param pin     El pin que introduce el usuario
	 * @param importe El importe de la compra
	 * @throws ImporteInvalidoException   Si el importe{@literal <}=0
	 * @throws SaldoInsuficienteException Si el saldo de la cuenta asociada
	 *                                    ({@link Cuenta#getSaldo()})
	 * a la tarjeta es menor que el importe
	 * @throws TarjetaBloqueadaException  Si la tarjeta esta bloqueada
	 * @throws PinInvalidoException       Si el pin introducido es
	 * incorrecto
	 */
	@Override
	public void comprar(final int pin, final double importe)
			throws
			ImporteInvalidoException,
			SaldoInsuficienteException,
			TarjetaBloqueadaException,
			PinInvalidoException {

		comprobar(pin);
		this.setIntentos(0);
		this.getCuenta().retirar(importe);
	}

	/**
	 * Bloquear.
	 */
	@Override
	protected void bloquear() {
		this.setActiva(false);
		ManagerHelper.getTarjetaDebitoDAO().save(this);
	}

	/**
	 * Cambiar pin.
	 * @param pinViejo the pin viejo
	 * @param pinNuevo the pin nuevo
	 * @throws PinInvalidoException the pin invalido exception
	 */
	@Override
	public void cambiarPin(final int pinViejo, final int pinNuevo)
			throws PinInvalidoException {
		if (this.getPin() != pinViejo) {
			throw new PinInvalidoException();
		}
		this.setPin(pinNuevo);
		ManagerHelper.getTarjetaDebitoDAO().save(this);
	}
}
