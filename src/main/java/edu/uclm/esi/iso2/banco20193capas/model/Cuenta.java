package edu.uclm.esi.iso2.banco20193capas.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import edu.uclm.esi.iso2.banco20193capas.exceptions.ClienteNoAutorizadoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.ClienteNoEncontradoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaInvalidaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaSinTitularesException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaYaCreadaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.ImporteInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.SaldoInsuficienteException;

/**
 * La clase @code Cuenta representa una cuenta bancaria, que ha de tener al
 * menos un @see Cliente que sea titular.
 */
@Entity
public class Cuenta {
	/**
	 * Identificador de la Cuenta.
	 */
	@Id
	private Long id;

	/**
	 * Titulares de la Cuenta.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Cliente> titulares;
	/**
	 * Boolean Creada.
	 */
	private boolean creada;

	/**
	 * Crea una cuenta.
	 */
	public Cuenta() {
		this.titulares = new ArrayList<>();
	}

	/**
	 * Crea una cuenta.
	 * @param identificador identificador de la cuenta
	 */
	public Cuenta(final Long identificador) {
		this();
		this.id = identificador;
	}

	/**
	 * Crea una cuenta.
	 * @param identificador identificador de la cuenta
	 */
	public Cuenta(final Integer identificador) {
		this(Long.valueOf(identificador));
	}

	/**
	 * Incluye un cliente a la lista de titulares de esta cuenta.
	 * @param cliente El cliente que se incluye a la lista de titulares
	 * @throws CuentaYaCreadaException Si la cuenta ya se ha almacenado
	 * en la BBDD
	 */
	public void addTitular(final Cliente cliente)
			throws CuentaYaCreadaException {
		if (creada) {
			throw new CuentaYaCreadaException();
		}
		this.titulares.add(cliente);
	}

	/**
	 * Realiza un ingreso en la cuenta.
	 * @param importe El importe que se ingresa
	 * @throws ImporteInvalidoException Si el importe es {@literal <}=0
	 */
	public void ingresar(final double importe)
			throws ImporteInvalidoException {
		this.ingresar(importe, "Ingreso de efectivo");
	}

	/**
	 * Realiza un ingreso en la cuenta.
	 * @param importe  El importe que se ingresa
	 * @param concepto Concepto del ingreso
	 * @throws ImporteInvalidoException Si el importe es {@literal <}=0
	 */
	private void ingresar(final double importe,
			final String concepto) throws ImporteInvalidoException {
		if (importe <= 0) {
			throw new ImporteInvalidoException(importe);
		}
		final MovimientoCuenta movimiento =
				new MovimientoCuenta(this, importe, concepto);
		ManagerHelper.getMovimientoDAO().save(movimiento);
	}

	/**
	 * Realiza una retirada de la cuenta.
	 * @param importe El importe que se retira
	 * @throws ImporteInvalidoException
	 * Si el importe es {@literal <}=0
	 * @throws SaldoInsuficienteException
	 * Si el importe{@literal >}getSaldo()
	 */
	public void retirar(final double importe)
			throws
			ImporteInvalidoException,
			SaldoInsuficienteException {
		this.retirar(importe, "Retirada de efectivo");
	}

	/**
	 * Realiza una retirada de la cuenta.
	 * @param importe  El importe que se retira
	 * @param concepto Concepto de la retirada
	 * @throws ImporteInvalidoException
	 * Si el importe es {@literal <}=0
	 * @throws SaldoInsuficienteException
	 * Si el importe{@literal >}getSaldo()
	 */
	private void retirar(final double importe, final String concepto)
			throws ImporteInvalidoException,
			SaldoInsuficienteException {
		if (importe <= 0) {
			throw new ImporteInvalidoException(importe);
		}
		if (importe > getSaldo()) {
			throw new SaldoInsuficienteException();
		}
		final MovimientoCuenta movimiento =
				new MovimientoCuenta(this, -importe, concepto);
		ManagerHelper.getMovimientoDAO().save(movimiento);
	}

	/**
	 * Retira el importe de la cuenta, incluso aunque esta no tenga saldo
	 * suficiente.
	 * @param importe  El importe que se retira
	 * @param concepto El concepto del movimiento
	 */
	public void retiroForzoso(
			final double importe,
			final String concepto) {
		final MovimientoCuenta movimiento =
				new MovimientoCuenta(this, -importe, concepto);
		ManagerHelper.getMovimientoDAO().save(movimiento);
	}

	/**
	 * Realiza una transferencia desde esta cuenta a la cuenta que se pasa
	 * como primer parametro. Se cobra una comision del 1%, con un minimo
	 * de 1.5.
	 * @param idCuentaDestino El identificador de la cuenta destino
	 * @param importe         El importe que se transfiere
	 * @param concepto        El concepto de la transferencia
	 * @throws CuentaInvalidaException    Si la cuenta destino es esta
	 *                                    misma o no existe en la BD
	 * @throws ImporteInvalidoException   Si el importe es {@literal <}=0
	 * @throws SaldoInsuficienteException Si la cuenta no tiene saldo
	 *                                    suficiente para afrontar el
	 *                                    importe y la comision
	 */
	public void transferir(
			final Long idCuentaDestino,
			final double importe,
			final String concepto)
			throws
			CuentaInvalidaException,
			ImporteInvalidoException,
			SaldoInsuficienteException {
		if (this.getId().equals(idCuentaDestino)) {
			throw new CuentaInvalidaException(idCuentaDestino);
		}
		final Optional<Cuenta> optCuenta =
				ManagerHelper.getCuentaDAO().
				findById(idCuentaDestino);
		if (!optCuenta.isPresent()) {
			throw new CuentaInvalidaException(idCuentaDestino);
		}
		final double comision = Math.max(0.01 * importe, 1.5);
		if (this.getSaldo() < importe + comision) {
			throw new SaldoInsuficienteException();
		}
		this.retirar(importe, "Transferencia emitidentificadora");

		this.retirar(comision, "Comision por transferencia");
		final Cuenta destino = this.load(idCuentaDestino);
		destino.ingresar(importe, "Transferencia recibidentificadora");
	}

	/**
	 * Carga una cuenta.
	 * @param numero identificador de la Cuenta
	 * @return Cuenta con ese identificador
	 * @throws CuentaInvalidaException Si la cuenta no existe
	 */
	private Cuenta load(final Long numero) throws CuentaInvalidaException {
		final Optional<Cuenta> optCuenta =
				ManagerHelper.getCuentaDAO().findById(numero);
		if (!optCuenta.isPresent()) {
			throw new CuentaInvalidaException(numero);
		}
		return optCuenta.get();
	}

	/**
	 * Devuelve el saldo de la cuenta.
	 * @return El saldo de la cuenta
	 */
	public double getSaldo() {
		final List<MovimientoCuenta> listaMovimientos =
				ManagerHelper.getMovimientoDAO().
				findByCuentaId(this.id);
		double saldo = 0.0;
		for (final MovimientoCuenta movimiento : listaMovimientos) {
			saldo = saldo + movimiento.getImporte();
		}
		return saldo;
	}

	/**
	 * Inserta la cuenta en la base de datos.
	 * @throws CuentaSinTitularesException Si no se ha asignado ningun
	 *                                     titular a la cuenta
	 * @throws CuentaYaCreadaException     Si existe una cuenta con el mismo
	 *                                     identificador
	 */
	public void insert() throws
	CuentaSinTitularesException,
	CuentaYaCreadaException {
		if (this.titulares.isEmpty()) {
			throw new CuentaSinTitularesException();
		}
		// Correccion de issue #6
		final Optional<Cuenta> optCuenta =
				ManagerHelper.getCuentaDAO().findById(this.id);
		if (optCuenta.isPresent()) {
			throw new CuentaYaCreadaException();
		}
		this.creada = true;
		ManagerHelper.getCuentaDAO().save(this);
	}

	/**
	 * Emite una tarjeta de debito asociada a esta cuenta.
	 * @param nif NIF del cliente para el que se emite la tarjeta
	 * @return La tarjeta de debito
	 * @throws ClienteNoEncontradoException Si el cliente no esta en la base
	 *                                      de datos
	 * @throws ClienteNoAutorizadoException Si el cliente no es titular de
	 *                                      esta cuenta
	 */
	public TarjetaDebito emitirTarjetaDebito(final String nif)
			throws ClienteNoEncontradoException,
			ClienteNoAutorizadoException {
		final Optional<Cliente> optCliente =
				ManagerHelper.getClienteDAO().findByNif(nif);
		if (!optCliente.isPresent()) {
			throw new ClienteNoEncontradoException(nif);
		}
		final Cliente cliente = optCliente.get();
		boolean encontrado = false;
		for (final Cliente titular : this.titulares) {
			if (titular.getNif().equals(cliente.getNif())) {
				encontrado = true;
				break;
			}
		}
		if (!encontrado) {
			throw new ClienteNoAutorizadoException(nif, this.id);
		}
		final TarjetaDebito tarjeta = new TarjetaDebito();
		tarjeta.setCuenta(this);
		tarjeta.setTitular(cliente);
		ManagerHelper.getTarjetaDebitoDAO().save(tarjeta);
		return tarjeta;
	}

	/**
	 * Emite una tarjeta de debito asociada a esta cuenta.
	 * @param nif     El nif del cliente para el cual se emite esta tarjeta
	 * @param credito El credito concedidentificadoro
	 * @return La tarjeta de credito
	 * @throws ClienteNoEncontradoException Si el cliente no esta en la base
	 *                                      de datos
	 * @throws ClienteNoAutorizadoException Si el cliente no es titular de
	 *                                      esta cuenta
	 */
	public TarjetaCredito emitirTarjetaCredito(
			final String nif,
			final double credito)
			throws
			ClienteNoEncontradoException,
			ClienteNoAutorizadoException {
		final Optional<Cliente> optCliente =
				ManagerHelper.getClienteDAO().findByNif(nif);
		if (!optCliente.isPresent()) {
			throw new ClienteNoEncontradoException(nif);
		}
		final Cliente cliente = optCliente.get();
		boolean encontrado = false;
		for (final Cliente titular : this.titulares) {
			if (titular.getNif().equals(cliente.getNif())) {
				encontrado = true;
				break;
			}
		}
		if (!encontrado) {
			throw new ClienteNoAutorizadoException(nif, this.id);
		}
		final TarjetaCredito tarjeta = new TarjetaCredito();
		tarjeta.setCuenta(this);
		tarjeta.setTitular(cliente);
		tarjeta.setCredito(credito);
		ManagerHelper.getTarjetaCreditoDAO().save(tarjeta);
		return tarjeta;
	}

	/**
	 * Getter de id.
	 * @return id de la cuenta
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter de id.
	 * @param identificador id de la cuenta.
	 */
	public void setId(final Long identificador) {
		this.id = identificador;
	}

	/**
	 * Getter de los titulares.
	 * @return lista de titulares
	 */
	public List<Cliente> getTitulares() {
		return titulares;
	}

	/**
	 * Setter de los titulares.
	 * @param listaTitulares lista de titulares
	 */
	public void setTitulares(final List<Cliente> listaTitulares) {
		this.titulares = listaTitulares;
	}

	/**
	 * Getter de Creada.
	 * @return booleano creada
	 */
	public boolean isCreada() {
		return creada;
	}

	/**
	 * Setter de creada.
	 * @param boolCreada booleano creada
	 */
	public void setCreada(final boolean boolCreada) {
		this.creada = boolCreada;
	}
}
