package edu.uclm.esi.iso2.banco20193capas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.uclm.esi.iso2.banco20193capas.model.MovimientoTarjetaCredito;

/**
 * The Interface MovimientoTarjetaCreditoDAO.
 */
public interface MovimientoTarjetaCreditoDAO extends
CrudRepository<MovimientoTarjetaCredito, Long> {

	/**
	 * Find by tarjeta id.
	 *
	 * @param tarjetaID the tarjeta ID
	 * @return the list
	 */
	List<MovimientoTarjetaCredito> findByTarjetaId(Long tarjetaID);
}
