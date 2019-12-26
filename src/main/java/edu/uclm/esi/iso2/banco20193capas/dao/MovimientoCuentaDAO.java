package edu.uclm.esi.iso2.banco20193capas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.uclm.esi.iso2.banco20193capas.model.MovimientoCuenta;

/**
 * The Interface MovimientoCuentaDAO.
 */
public interface MovimientoCuentaDAO extends
CrudRepository<MovimientoCuenta, Long> {

	/**
	 * Find by cuenta id.
	 *
	 * @param cuentaID the cuenta ID
	 * @return the list
	 */
	List<MovimientoCuenta> findByCuentaId(Long cuentaID);
}
