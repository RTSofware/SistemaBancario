/**
 * Paquete modelo.
 */
package edu.uclm.esi.iso2.banco20193capas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Clase Cliente.
 */
@Entity
public class Cliente {
	/**
	 * Identificador del Cliente.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	/**
	 * NIF del Cliente.
	 */
	@Column(unique = true)
	private String nif;
	/**
	 * Nombre del Cliente.
	 */
	private String nombre;
	/**
	 * Apellidos del Cliente.
	 */
	private String apellidos;

	/**
	 * Crea un cliente vacio.
	 */
	public Cliente() {
		// This constructor is intentionally empty.
	}

	/**
	 * Crea un cliente.
	 * @param nifCliente       NIF del cliente
	 * @param nombreCliente    Nombre del cliente
	 * @param apellidosCliente Apellidos del cliente
	 */
	public Cliente(final String nifCliente, final String nombreCliente, 
					final String apellidosCliente) {
		this.nif = nifCliente;
		this.nombre = nombreCliente;
		this.apellidos = apellidosCliente;
	}

	/**
	 * Getter de Id.
	 * @return id del cliente
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter de Id.
	 * @param idCliente id del cliente
	 */
	public void setId(final Long idCliente) {
		this.id = idCliente;
	}

	/**
	 * Getter de NIF.
	 * @return NIF del cliente
	 */
	public String getNif() {
		return nif;
	}

	/**
	 * Setter de NIF.
	 * @param nifCliente NIF del cliente
	 */
	public void setNif(final String nifCliente) {
		this.nif = nifCliente;
	}

	/**
	 * Getter de Nombre.
	 * @return nombre del cliente
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Setter de Nombre.
	 * @param nombreCliente Nombre del cliente
	 */
	public void setNombre(final String nombreCliente) {
		this.nombre = nombreCliente;
	}

	/**
	 * Getter de Apellidos.
	 * @return apellidos del cliente
	 */
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * Setter de Apellidos.
	 * @param apellidosCliente Apellidos del cliente
	 */
	public void setApellidos(final String apellidosCliente) {
		this.apellidos = apellidosCliente;
	}

	/**
	 * Inserta un cliente en la base de datos.
	 */
	public void insert() {
		ManagerHelper.getClienteDAO().save(this);
	}
}
