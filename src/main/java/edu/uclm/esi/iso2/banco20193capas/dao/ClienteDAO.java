
package edu.uclm.esi.iso2.banco20193capas.dao;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import edu.uclm.esi.iso2.banco20193capas.model.Cliente;

/**
 * The Interface ClienteDAO.
 */
public interface ClienteDAO extends CrudRepository<Cliente, Long> {

	/**
	 * Find by nif.
	 *
	 * @param nif the nif
	 * @return the optional
	 */
	Optional<Cliente> findByNif(String nif);
}
