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
	private static MovimientoTarjetaCreditoDAO movimientoTCDAO;
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
	 * @param cuentaIn the cuenta dao in
	 * @param movimientoIn the movimiento dao in
	 * @param clienteIn the cliente DAO in
	 * @param movimientoTCIn the movimiento TCDAO in
	 * @param tarjetaDebitoIn the tarjeta debito DAO in
	 * @param tarjetaCreditoIn the tarjeta credito DAO in
	 */
	@Autowired
	private synchronized void loadDAO(
			final CuentaDAO cuentaIn,
			final MovimientoCuentaDAO movimientoIn,
			final ClienteDAO clienteIn,
			final MovimientoTarjetaCreditoDAO movimientoTCIn,
			final TarjetaDebitoDAO tarjetaDebitoIn,
			final TarjetaCreditoDAO tarjetaCreditoIn) {
		ManagerHelper.cuentaDAO = cuentaIn;
		ManagerHelper.movimientoDAO = movimientoIn;
		ManagerHelper.clienteDAO = clienteIn;
		ManagerHelper.movimientoTCDAO = movimientoTCIn;
		ManagerHelper.tarjetaDebitoDAO = tarjetaDebitoIn;
		ManagerHelper.tarjetaCreditoDAO = tarjetaCreditoIn;
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
		return movimientoTCDAO;
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
