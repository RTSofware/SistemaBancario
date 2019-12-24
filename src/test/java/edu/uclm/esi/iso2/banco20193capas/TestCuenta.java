package edu.uclm.esi.iso2.banco20193capas;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.uclm.esi.iso2.banco20193capas.model.Cuenta;
import edu.uclm.esi.iso2.banco20193capas.model.ManagerHelper;
import edu.uclm.esi.iso2.banco20193capas.exceptions.ClienteNoAutorizadoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.ClienteNoEncontradoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaInvalidaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaSinTitularesException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaYaCreadaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.ImporteInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.SaldoInsuficienteException;
import edu.uclm.esi.iso2.banco20193capas.model.Cliente;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaCredito;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaDebito;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCuenta extends TestCase {

	private Cuenta cuentaPepe, cuentaAna;
	private Cliente pepe, ana;
	private TarjetaCredito tcPepe, tcAna;
	private TarjetaDebito tdPepe, tdAna;
	private int pinPepe, pinAna;
	private int tokenInternet;
	private double saldoPepe, SaldoAna;
	private double creditoPepe, creditoAna;

	@Before
	public void setUp() {
		// Limpiar BB.DD.
		ManagerHelper.getMovimientoDAO().deleteAll();
		ManagerHelper.getMovimientoTarjetaCreditoDAO().deleteAll();
		ManagerHelper.getTarjetaCreditoDAO().deleteAll();
		ManagerHelper.getTarjetaDebitoDAO().deleteAll();
		ManagerHelper.getCuentaDAO().deleteAll();
		ManagerHelper.getClienteDAO().deleteAll();

		// Pin
		this.pinPepe = 1234;
		this.pinAna = 1234;

		// Token
		this.tokenInternet = 1234;

		// Saldo
		this.saldoPepe = 1000.0;
		this.SaldoAna = 1000.0;

		// Credito
		this.creditoPepe = 2000;
		this.creditoAna = 2000;

		// Cliente
		this.pepe = new Cliente("12345X", "Pepe", "Pérez");
		this.pepe.insert();

		this.ana = new Cliente("98765F", "Ana", "López");
		this.ana.insert();

		// Cuenta
		this.cuentaPepe = new Cuenta(1L);
		this.cuentaAna = new Cuenta(2L);
		try {

			this.cuentaPepe.addTitular(pepe);
			this.cuentaPepe.insert();
			this.cuentaPepe.ingresar(saldoPepe);

			this.cuentaAna.addTitular(ana);
			this.cuentaAna.insert();
			this.cuentaAna.ingresar(SaldoAna);

			// Tarjeta de Credito
			this.tcPepe = this.cuentaPepe.emitirTarjetaCredito(pepe.getNif(), 2000);
			this.tcPepe.cambiarPin(this.tcPepe.getPin(), pinPepe);

			this.tcAna = this.cuentaAna.emitirTarjetaCredito(ana.getNif(), 10000);
			this.tcAna.cambiarPin(this.tcAna.getPin(), pinAna);

			// Tarjeta de Debito
			this.tdPepe = this.cuentaPepe.emitirTarjetaDebito(pepe.getNif());
			this.tdPepe.cambiarPin(tdPepe.getPin(), pinPepe);

			this.tdAna = this.cuentaAna.emitirTarjetaDebito(ana.getNif());
			this.tdAna.cambiarPin(tdAna.getPin(), pinAna);

		} catch (Exception e) {
			fail("Excepción inesperada en setUp(): " + e);
		}
	}

	@Test
	public void emitirTarjetaCredito_sinCliente() {

		try {
			cuentaPepe.emitirTarjetaCredito("", creditoPepe);
			fail("Esperaba ClienteNoEncontradoException");
		} catch (ClienteNoEncontradoException e) {

		} catch (ClienteNoAutorizadoException e) {
			fail("Excepción inesperada (ClienteNoAutorizadoException): " + e.getMessage());
		}
	}

	// testEmitirTarjetaCreditoNegativo
	// Crear tarjeta de Credito con credito negativo no esta controlado

	@Test
	public void emitirTarjetaCredito_sinAutorizacion() {

		try {
			cuentaPepe.emitirTarjetaCredito(ana.getNif(), creditoPepe);
			fail("Esperaba ClienteNoAutorizadoException");
		} catch (ClienteNoEncontradoException e) {
			fail("Excepción ClienteNoEncontradoException inesperada: " + e.getMessage());
		} catch (ClienteNoAutorizadoException e) {
		}
	}

	@Test
	public void emitirTarjetaCredito_exito() {

		try {
			TarjetaCredito tarjetaNueva = cuentaPepe.emitirTarjetaCredito(pepe.getNif(), creditoPepe);
			tarjetaNueva.setActiva(true);
			assertEquals(creditoPepe, tarjetaNueva.getCreditoDisponible());

		} catch (ClienteNoEncontradoException e) {
			fail("Excepción ClienteNoEncontradoException inesperada: " + e);

		} catch (ClienteNoAutorizadoException e) {
			fail("Excepción ClienteNoAutorizadoException inesperada: " + e);
		}
	}

	@Test
	public void emitirTarjetaDebito_sinCliente() {

		try {
			cuentaPepe.emitirTarjetaDebito("");
			fail("Esperaba ClienteNoEncontradoException");
		} catch (ClienteNoEncontradoException e) {

		} catch (ClienteNoAutorizadoException e) {
			fail("Excepción ClienteNoAutorizadoException inesperada: " + e.getMessage());
		}
	}

	@Test
	public void emitirTarjetaDebito_sinAutorizacion() {

		try {
			cuentaPepe.emitirTarjetaDebito("98765F");
			fail("Esperaba ClienteNoAutorizadoException");
		} catch (ClienteNoEncontradoException e) {
			fail("Excepción ClienteNoEncontradoException inesperada: " + e.getMessage());
		} catch (ClienteNoAutorizadoException e) {
		}
	}

	@Test
	public void emitirTarjetaDebito_exito() {

		try {
			TarjetaDebito tarjetaNueva = cuentaPepe.emitirTarjetaDebito(pepe.getNif());
			tarjetaNueva.setActiva(true);
			assertTrue(tarjetaNueva.isActiva());

		} catch (ClienteNoEncontradoException e) {
			fail("Excepción inesperada (ClienteNoEncontradoException): " + e);

		} catch (ClienteNoAutorizadoException e) {
			fail("Excepción inesperada (ClienteNoAutorizadoException): " + e);
		}
	}

	@Test
	public void transferir_aLaMismaCuenta() {

		try {
			cuentaPepe.transferir(1L, 100, "Alquiler");
			fail("Esperaba CuentaInvalidaException");
		} catch (CuentaInvalidaException e) {
			assertEquals(this.saldoPepe, cuentaPepe.getSaldo());
		} catch (Exception e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		}
	}

	@Test
	public void transferir_aCuentaIncorrecta() {

		long cuentaInexistente = 3L;
		double transferencia = 500.0;

		try {
			cuentaPepe.transferir(cuentaInexistente, transferencia, "Alquiler");
			fail("Esperaba CuentaInvalidaException");
		} catch (CuentaInvalidaException e) {
			assertEquals(this.saldoPepe, cuentaPepe.getSaldo());
		} catch (Exception e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		}
	}

	@Test
	public void transferir_importeInvalido() {

		double transferenciaInvalida = -1;

		try {
			cuentaPepe.transferir(cuentaAna.getId(), transferenciaInvalida, "Alquiler");
			fail("Esperado ImporteInvalidoException");

		} catch (CuentaInvalidaException e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		} catch (ImporteInvalidoException e) {
			assertEquals(saldoPepe, cuentaPepe.getSaldo());
		} catch (SaldoInsuficienteException e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		} catch (Exception e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		}
	}

	@Test
	public void transferir_saldoInsuficiente() {

		double transferencia = 5000.0;

		try {
			cuentaPepe.transferir(cuentaAna.getId(), transferencia, "Alquiler");
			fail("Esperado SaldoInsuficiente");

		} catch (CuentaInvalidaException e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		} catch (ImporteInvalidoException e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		} catch (SaldoInsuficienteException e) {
			assertEquals(saldoPepe, cuentaPepe.getSaldo());
		}
	}

	@Test
	public void transferir_saldoSuficienteSinComision() {

		double transferencia = 1000.0;

		try {
			cuentaPepe.transferir(cuentaAna.getId(), transferencia, "Alquiler");
			fail("Esperado SaldoInsuficiente");
		} catch (CuentaInvalidaException e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		} catch (ImporteInvalidoException e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		} catch (SaldoInsuficienteException e) {
			System.out
			.println("testTransferenciaSaldoSuficienteSinComision - Saldo restante: " + cuentaPepe.getSaldo());
			assertEquals(1000.0, cuentaPepe.getSaldo());
		}
	}

	@Test
	public void transferir_exito() {

		double transferencia = 500;

		try {
			cuentaPepe.transferir(cuentaAna.getId(), transferencia, "Alquiler");
			assertEquals(495.0, cuentaPepe.getSaldo());
			assertEquals(1500.0, cuentaAna.getSaldo());

		} catch (CuentaInvalidaException e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		} catch (ImporteInvalidoException e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		} catch (SaldoInsuficienteException e) {
			fail("Se ha lanzado una excepción inesperada: " + e);
		}
	}

	@Test
	public void retirar_importeInvalido() {

		double importe = -1.0;

		try {
			cuentaPepe.retirar(importe);
			fail("Esperado ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
			assertEquals(saldoPepe, cuentaPepe.getSaldo());

		} catch (SaldoInsuficienteException e) {
			fail("Excepción inesperada (SaldoInsuficienteException): " + e);
		}
	}

	@Test
	public void retirar_saldoInsuficiente() {

		double importe = 2000.0;

		try {
			cuentaPepe.retirar(importe);
			fail("Esperaba SaldoInsuficienteException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			assertEquals(saldoPepe, cuentaPepe.getSaldo());
		}
	}

	@Test
	public void retirar_exito() {

		double importe = 1000.0;

		try {
			cuentaPepe.retirar(importe);
			assertEquals(0.0, cuentaPepe.getSaldo());

		} catch (ImporteInvalidoException e) {
			fail("Excepción inesperada (ImporteInvalidoException): " + e);

		} catch (SaldoInsuficienteException e) {
			fail("Excepción inesperada (SaldoInsuficienteException): " + e);
		}
	}

	@Test
	public void cuentaSinTitular() {

		Cuenta cuentaNueva = new Cuenta(3L);

		try {
			cuentaNueva.insert();
			fail("Esperaba CuentaSinTitularesException");
		} catch (CuentaSinTitularesException e) {
		} catch (CuentaYaCreadaException e) {
			//a raiz de la correccion del issue #6
			fail("Esperaba CuentaSinTitularesException en lugar de CuentaYaCreadaException");
		}
	}

	@Test
	public void cuentaDuplicada() {

		Cuenta cuentaPepeDuplicada = new Cuenta(1L);

		try {
			cuentaPepeDuplicada.addTitular(pepe);
			try {
				cuentaPepeDuplicada.insert();
				fail("Excepción esperada (CuentaYaCreadaException)");
			} catch (CuentaSinTitularesException e) {
				// TODO Auto-generated catch block
				fail("Se ha lanzado una excepción inesperada: " + e);
			}
		} catch (CuentaYaCreadaException e) {
		}
	}

	@Test
	public void addTitular_dosVeces() {

		try {
			cuentaPepe.addTitular(pepe);
			fail("Excepción esperada (CuentaYaCreadaException)");

			try {
				cuentaPepe.insert();
			} catch (CuentaSinTitularesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (CuentaYaCreadaException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		assertEquals(1, cuentaPepe.getTitulares().size());
	}

	@Test
	public void addTitular_variosTitulares() {

		Cuenta cuentaNueva = new Cuenta(3L);

		try {
			cuentaNueva.addTitular(pepe);
			cuentaNueva.addTitular(ana);

			try {
				cuentaNueva.insert();
			} catch (CuentaSinTitularesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (CuentaYaCreadaException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		assertEquals(2, cuentaNueva.getTitulares().size());
	}

	@Test
	public void ingresar_importeInvalido() {

		double importe = -1.0;

		try {
			cuentaPepe.ingresar(importe);
			fail("Excepción esperada (ImporteInvalidoException)");
		} catch (ImporteInvalidoException e) {
			assertEquals(saldoPepe, cuentaPepe.getSaldo());
		}

	}
	@Test
	public void test_saldoInsuficienteConComision(){
		double transferencia=1000.0;
		try {
			this.cuentaPepe.transferir(this.cuentaAna.getId(), transferencia, "Ordenador nuevo");
			fail("Se esperaba SaldoInsuficienteException");

		}catch(SaldoInsuficienteException e) {


		}catch(Exception e) {
			fail("Se esperaba SaldoInsuficienteException");
		}
	}

}
