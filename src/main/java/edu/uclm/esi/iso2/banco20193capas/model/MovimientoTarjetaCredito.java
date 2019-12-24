package edu.uclm.esi.iso2.banco20193capas.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Representa un movimiento asociado a una tarjeta de crédito.
 */
@Entity
public class MovimientoTarjetaCredito {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	/**
	 * Id del Movimiento.
	 */
	private Long id;
	
	@ManyToOne
	/**
	 * Tarjeta del Movimiento.
	 */
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
	 * @param tarjetaMovi tarjeta del movimiento
	 * @param importeMovi importe del movimiento
	 * @param conceptoMovi concepto del movimiento
	 */
	public MovimientoTarjetaCredito(final TarjetaCredito tarjetaMovi, final double importeMovi, final String conceptoMovi) {
		this.importe = importeMovi;
		this.concepto = conceptoMovi;
		this.tarjeta = tarjetaMovi;
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
	 * @param tarjetaMovi tarjeta de credito del movimiento
	 */
	public void setTarjeta(final TarjetaCredito tarjetaMovi) {
		this.tarjeta = tarjetaMovi;
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
	 * @param importeMovi importe del movimiento
	 */
	public void setImporte(final double importeMovi) {
		this.importe = importeMovi;
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
	 * @param conceptoMovi concepto del movimiento
	 */
	public void setConcepto(final String conceptoMovi) {
		this.concepto = conceptoMovi;
	}

	/**
	 * Getter liquidado
	 * @return booleano de liquidado
	 */
	public boolean isLiquidado() {
		return liquidado;
	}

	/**
	 * Setter de liquidado
	 * @param liquidadoMovi booleanno liquidado
	 */
	public void setLiquidado(final boolean liquidadoMovi) {
		this.liquidado = liquidadoMovi;
	}

}
