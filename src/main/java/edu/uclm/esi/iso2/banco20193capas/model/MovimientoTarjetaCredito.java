package edu.uclm.esi.iso2.banco20193capas.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Representa un movimiento asociado a una tarjeta de credito.
 */
@Entity
public class MovimientoTarjetaCredito {

	/**
	 * Id del Movimiento.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	/**
	 * Tarjeta del Movimiento.
	 */
	@ManyToOne
	private TarjetaCredito tarjeta;

	/**
	 * Importe del movimiento.
	 */
	private double importe;

	/**
	 * Concepto del movimiento.
	 */
	private String concepto;

	/**
	 * Booleano liquidado o no el movimiento.
	 */
	private boolean liquidado;

	/**
	 * Crea MovimientoTarjeta.
	 */
	public MovimientoTarjetaCredito() {
	}

	/**
	 * Crea MovimientoTarjeta.
	 * @param tarjetaMoviento  tarjeta del movimiento
	 * @param importeMoviento  importe del movimiento
	 * @param conceptoMoviento concepto del movimiento
	 */
	public MovimientoTarjetaCredito(
			final TarjetaCredito tarjetaMoviento,
			final double importeMoviento,
			final String conceptoMoviento) {
		this.importe = importeMoviento;
		this.concepto = conceptoMoviento;
		this.tarjeta = tarjetaMoviento;
	}

	/**
	 * Getter del Id del movimiento.
	 * @return id del movimiento
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter del id del movimiento.
	 * @param identificador id del movimiento
	 */
	public void setId(final Long identificador) {
		this.id = identificador;
	}

	/**
	 * Getter de la tarjeta de credido.
	 * @return tarjeta del movimiento
	 */
	public TarjetaCredito getTarjeta() {
		return tarjeta;
	}

	/**
	 * Setter de la tarjeta del movimiento.
	 * @param tarjetaMovimiento tarjeta de credito del movimiento
	 */
	public void setTarjeta(final TarjetaCredito tarjetaMovimiento) {
		this.tarjeta = tarjetaMovimiento;
	}

	/**
	 * Getter del importe.
	 * @return importe del movimiento
	 */
	public double getImporte() {
		return importe;
	}

	/**
	 * Setter del importe.
	 * @param importeMovimiento importe del movimiento
	 */
	public void setImporte(final double importeMovimiento) {
		this.importe = importeMovimiento;
	}

	/**
	 * Getter del concepto.
	 * @return concepto del movimiento
	 */
	public String getConcepto() {
		return concepto;
	}

	/**
	 * Setter del concepto.
	 * @param conceptoMovimiento concepto del movimiento
	 */
	public void setConcepto(final String conceptoMovimiento) {
		this.concepto = conceptoMovimiento;
	}

	/**
	 * Getter liquidado.
	 * @return booleano de liquidado
	 */
	public boolean isLiquidado() {
		return liquidado;
	}

	/**
	 * Setter de liquidado.
	 * @param liquidadoMovimiento booleanno liquidado
	 */
	public void setLiquidado(
			final boolean liquidadoMovimiento) {
		this.liquidado = liquidadoMovimiento;
	}
}
