package edu.uclm.esi.iso2.banco20193capas.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.uclm.esi.iso2.banco20193capas.dao.ClienteDAO;
import edu.uclm.esi.iso2.banco20193capas.dao.CuentaDAO;
import edu.uclm.esi.iso2.banco20193capas.dao.MovimientoCuentaDAO;
import edu.uclm.esi.iso2.banco20193capas.dao.MovimientoTarjetaCreditoDAO;
import edu.uclm.esi.iso2.banco20193capas.dao.TarjetaCreditoDAO;
import edu.uclm.esi.iso2.banco20193capas.dao.TarjetaDebitoDAO;

/**
 * El Manager da acceso a las clases DAO asociadas a las clases de dominio.
 */
@Component
public final class ManagerHelper {
	/**
	 * CuentaDAO.
	 */
	private static CuentaDAO cuentaDAO;
	/**
	 * Movimiento CuentaDAO.
	 */
	private static MovimientoCuentaDAO movimientoDAO;
	/**
	 * Movimiento TarjetaCreditoDAO.
	 */
	private static MovimientoTarjetaCreditoDAO movimientoTCreditoDAO;
	/**
	 * ClienteDAO.
	 */
	private static ClienteDAO clienteDAO;
	/**
	 * TarjetaDebitoDAO.
	 */
	private static TarjetaDebitoDAO tarjetaDebitoDAO;
	/**
	 * TarjetaCreditoDAO.
	 */
	private static TarjetaCreditoDAO tarjetaCreditoDAO;

	/**
	 * Crea Manager.
	 */
	private ManagerHelper() {
		// Creamos Manager
	}

	/**
	 * Carga el DAO.
	 *
	 * @param cuentaDaoIn the cuenta dao in
	 * @param movimientoDaoIn the movimiento dao in
	 * @param clienteDAOIn the cliente DAO in
	 * @param movimientoTCDAOIn the movimiento TCDAO in
	 * @param tarjetaDebitoDAOIn the tarjeta debito DAO in
	 * @param tarjetaCreditoDAOIn the tarjeta credito DAO in
	 */
	@Autowired
	private synchronized void loadDAO(
			final CuentaDAO cuentaDaoIn,
			final MovimientoCuentaDAO movimientoDaoIn,
			final ClienteDAO clienteDAOIn,
			final MovimientoTarjetaCreditoDAO movimientoTCDAOIn,
			final TarjetaDebitoDAO tarjetaDebitoDAOIn,
			final TarjetaCreditoDAO tarjetaCreditoDAOIn) {
		ManagerHelper.cuentaDAO = cuentaDaoIn;
		ManagerHelper.movimientoDAO = movimientoDaoIn;
		ManagerHelper.clienteDAO = clienteDAOIn;
		ManagerHelper.movimientoTCreditoDAO = movimientoTCDAOIn;
		ManagerHelper.tarjetaDebitoDAO = tarjetaDebitoDAOIn;
		ManagerHelper.tarjetaCreditoDAO = tarjetaCreditoDAOIn;
	}

	/**
	 * Devuelve CuentaDAO.
	 * @return CuentaDAO
	 */
	public static CuentaDAO getCuentaDAO() {
		return cuentaDAO;
	}

	/**
	 * Devuelve MovimientoCuentaDAO.
	 * @return MovimientoCuentaDAO
	 */
	public static MovimientoCuentaDAO getMovimientoDAO() {
		return movimientoDAO;
	}

	/**
	 * Devuelve ClienteDAO.
	 * @return ClienteDAO
	 */
	public static ClienteDAO getClienteDAO() {
		return clienteDAO;
	}

	/**
	 * Devuelve MovimientoTarjetaCreditoDAO.
	 * @return MovimientoTarjetaCreditoDAO
	 */
	public static MovimientoTarjetaCreditoDAO
	getMovimientoTarjetaCreditoDAO() {
		return movimientoTCreditoDAO;
	}

	/**
	 * Devuelve TarjetaDebitoDAO.
	 * @return TarjetaDebitoDAO
	 */
	public static TarjetaDebitoDAO getTarjetaDebitoDAO() {
		return tarjetaDebitoDAO;
	}

	/**
	 * Devuelve TarjetaCreditoDAO.
	 * @return TarjetaCreditoDAO
	 */
	public static TarjetaCreditoDAO getTarjetaCreditoDAO() {
		return tarjetaCreditoDAO;
	}
}
