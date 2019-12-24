package edu.uclm.esi.iso2.banco20193capas.model;

import java.security.SecureRandom;
import java.util.List;

import javax.persistence.Entity;

import edu.uclm.esi.iso2.banco20193capas.exceptions.ImporteInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.PinInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.SaldoInsuficienteException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.TarjetaBloqueadaException;

/**
 * Tarjeta de credito.
 */
@Entity
public class TarjetaCredito extends Tarjeta {
	/**
	 * Credito.
	 */
	private Double credito;

	/**
	 * Crea una tarjeta de credito.
	 */
	public TarjetaCredito() {
		super();
	}

	/**
	 * Permite sacar dinero del cajero automático.
	 * 
	 * @param pin     El pin que introduce el usuario
	 * @param importe El {@code importe} que desea sacar
	 * @throws ImporteInvalidoException   Si el {@code importe<=0}
	 * @throws SaldoInsuficienteException Si el crédito disponible de la tarjeta es
	 *                                    menor que el importe
	 * @throws TarjetaBloqueadaException  Si la tarjeta está bloqueada
	 * @throws PinInvalidoException       Si el pin introducido es distinto del pin
	 *                                    de la tarjeta
	 */
	@Override
	public void sacarDinero(final int pin, final double importe) throws ImporteInvalidoException, SaldoInsuficienteException,
			TarjetaBloqueadaException, PinInvalidoException {
		comprobar(pin);
		this.intentos = 0;
		if (importe > getCreditoDisponible()) {
			throw new SaldoInsuficienteException();
		}
		if (importe <= 0) {
			throw new ImporteInvalidoException(importe);
		}
		MovimientoTarjetaCredito principal = new MovimientoTarjetaCredito(this, importe, "Retirada de efectivo");
		double comision = 3;
		MovimientoTarjetaCredito mComision = new MovimientoTarjetaCredito(this, comision,
				"Comisión por retirada de efectivo");
		ManagerHelper.getMovimientoTarjetaCreditoDAO().save(principal);
		ManagerHelper.getMovimientoTarjetaCreditoDAO().save(mComision);
	}

	/**
	 * Inicia una compra por Internet, que debe confirmarse después (ver
	 * {@link #confirmarCompraPorInternet(int)}) mediante el token que devuelve este
	 * método.
	 * 
	 * @param pin     El pin que introduce el usuario
	 * @param importe El importe de la compra
	 * @return Un token que debe introducirse en
	 *         {@link #confirmarCompraPorInternet(int)}
	 * @throws TarjetaBloqueadaException  Si la tarjeta está bloqueada
	 * @throws PinInvalidoException       Si el pin introducido es distinto del pin
	 *                                    de la tarjeta
	 * @throws SaldoInsuficienteException Si el crédito disponible de la tarjeta es
	 *                                    menor que el importe
	 * @throws ImporteInvalidoException   Si el importe{@literal <}=0
	 */
	@Override
	public Integer comprarPorInternet(final int pin, final double importe) throws TarjetaBloqueadaException, PinInvalidoException,
			SaldoInsuficienteException, ImporteInvalidoException {
		comprobar(pin);
		this.intentos = 0;
		if (importe > getCreditoDisponible()) {
			throw new SaldoInsuficienteException();}
		
		if (importe <= 0) {
			throw new ImporteInvalidoException(importe);
		}
		SecureRandom dado = new SecureRandom();
		int token = 0;
		for (int i = 0; i <= 3; i++) {
			token = (int) (token + dado.nextInt(10) * Math.pow(10, i));
		}
		token = 1234;
		this.compra = new Compra(importe, token);
		return token;
	}

	/**
	 * Permite hacer un compra en un comercio.
	 * 
	 * @param pin     El pin que introduce el usuario
	 * @param importe El importe de la compra
	 * @throws ImporteInvalidoException   Si el importe{@literal <}=0
	 * @throws SaldoInsuficienteException Si el crédito disponible
	 *                                    ({@link #getCreditoDisponible()}) de la
	 *                                    tarjeta es menor que el importe
	 * @throws TarjetaBloqueadaException  Si la tarjeta está bloqueada
	 * @throws PinInvalidoException       Si el pin introducido es incorrecto
	 */
	@Override
	public void comprar(final int pin, final double importe) throws ImporteInvalidoException, SaldoInsuficienteException,
			TarjetaBloqueadaException, PinInvalidoException {
		comprobar(pin);
		this.intentos = 0;
		if (importe > getCreditoDisponible()) {
			throw new SaldoInsuficienteException();
		}
		if (importe <= 0) {
			throw new ImporteInvalidoException(importe);
		}
		MovimientoTarjetaCredito principal = new MovimientoTarjetaCredito(this, importe, "Retirada de efectivo");
		ManagerHelper.getMovimientoTarjetaCreditoDAO().save(principal);
	}

	@Override
	protected void bloquear() {
		this.activa = false;
		ManagerHelper.getTarjetaCreditoDAO().save(this);
	}

	/**
	 * Liquida la tarjeta.
	 */
	public void liquidar() {
		double gastos = 0.0;
		List<MovimientoTarjetaCredito> mm = ManagerHelper.getMovimientoTarjetaCreditoDAO().findByTarjetaId(this.id);
		for (MovimientoTarjetaCredito m : mm) {
			if (!m.isLiquidado()) {
				gastos = gastos + m.getImporte();
				m.setLiquidado(true);
				ManagerHelper.getMovimientoTarjetaCreditoDAO().save(m);
			}
		}
		this.cuenta.retiroForzoso(gastos, "Liquidación de tarjeta de crédito");
	}

	/**
	 * Getter de Credito de la tarjeta.
	 * 
	 * @return Credito de la tarjeta
	 */
	public Double getCredito() {
		return credito;
	}

	/**
	 * Getter de Credito disponible de la tarjeta.
	 * 
	 * @return Credito disponible de la tarjeta
	 */
	public Double getCreditoDisponible() {
		double gastos = 0.0;
		List<MovimientoTarjetaCredito> mm = ManagerHelper.getMovimientoTarjetaCreditoDAO().findByTarjetaId(this.id);
		for (MovimientoTarjetaCredito m : mm) {
			if (!m.isLiquidado()) {
				gastos = gastos + m.getImporte();
			}
		}
			
		return credito - gastos;
	}

	/**
	 * Setter del credito de la tarjeta.
	 * 
	 * @param creditoTj credito de la tarjeta
	 */
	public void setCredito(Double creditoTj) {
		this.credito = creditoTj;
	}

	@Override
	public void cambiarPin(final int pinViejo, final int pinNuevo) throws PinInvalidoException {
		if (this.pin != pinViejo) {
			throw new PinInvalidoException();
		}
		this.pin = pinNuevo;
		ManagerHelper.getTarjetaCreditoDAO().save(this);
	}
}
