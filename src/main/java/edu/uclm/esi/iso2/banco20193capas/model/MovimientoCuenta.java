package edu.uclm.esi.iso2.banco20193capas.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Representa un movimiento en una cuenta bancaria.
 */
@Entity
public class MovimientoCuenta {
	/**
	 * Id del movimiento.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Cuenta del movimiento.
	 */
	@ManyToOne
	private Cuenta cuenta;

	/**
	 * Importe del movimiento.
	 */
	private double importe;

	/**
	 * Concepto del movimiento.
	 */
	private String concepto;

	/**
	 * Crea un movimiento.
	 */
	public MovimientoCuenta() {
		// Crea un movimiento
	}

	/**
	 * Crea un movimiento.
	 * @param cuentaMovimiento   del movimiento
	 * @param importeMovimiento  del movimiento
	 * @param conceptoMovimiento del movimiento
	 */
	public MovimientoCuenta(
			final Cuenta cuentaMovimiento,
			final double importeMovimiento,
			final String conceptoMovimiento) {
		this.importe = importeMovimiento;
		this.concepto = conceptoMovimiento;
		this.cuenta = cuentaMovimiento;
	}

	/**
	 * Getter del Id.
	 * @return Id del Movimiento
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter del Id.
	 * @param identificador Id del Movimiento
	 */
	public void setId(final Long identificador) {
		this.id = identificador;
	}

	/**
	 * Getter de la cuenta.
	 * @return cuenta del Movimiento
	 */
	public Cuenta getCuenta() {
		return cuenta;
	}

	/**
	 * Setter de la cuenta.
	 * @param cuentaMovi del movimiento
	 */
	public void setCuenta(final Cuenta cuentaMovi) {
		this.cuenta = cuentaMovi;
	}

	/**
	 * Getter del Importe.
	 * @return Importe del Movimiento
	 */
	public double getImporte() {
		return importe;
	}

	/**
	 * Setter del Importe.
	 * @param importeMovi Importe del Movimiento
	 */
	public void setImporte(final double importeMovi) {
		this.importe = importeMovi;
	}

	/**
	 * Getter del Concepto.
	 * @return Concepto del Movimiento
	 */
	public String getConcepto() {
		return concepto;
	}

	/**
	 * Setter del Concepto.
	 * @param conceptoMovi Concepto del Movimiento
	 */
	public void setConcepto(final String conceptoMovi) {
		this.concepto = conceptoMovi;
	}
}
