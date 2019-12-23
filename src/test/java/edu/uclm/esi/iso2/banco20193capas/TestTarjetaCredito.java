package edu.uclm.esi.iso2.banco20193capas;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.uclm.esi.iso2.banco20193capas.exceptions.ImporteInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.PinInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.SaldoInsuficienteException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.TarjetaBloqueadaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.TokenInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.model.Cliente;
import edu.uclm.esi.iso2.banco20193capas.model.Cuenta;
import edu.uclm.esi.iso2.banco20193capas.model.Manager;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaCredito;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTarjetaCredito extends TestCase {
	private Cuenta cuentaPepe, cuentaAna;
	private Cliente pepe, ana;
	private TarjetaCredito tcPepe, tcAna;

	@Before
	public void setUp() {
		Manager.getMovimientoDAO().deleteAll();
		Manager.getMovimientoTarjetaCreditoDAO().deleteAll();
		Manager.getTarjetaCreditoDAO().deleteAll();
		Manager.getTarjetaDebitoDAO().deleteAll();
		Manager.getCuentaDAO().deleteAll();
		Manager.getClienteDAO().deleteAll();

		this.pepe = new Cliente("12345X", "Pepe", "Pérez");
		this.pepe.insert();
		this.ana = new Cliente("98765F", "Ana", "López");
		this.ana.insert();
		this.cuentaPepe = new Cuenta(1);
		this.cuentaAna = new Cuenta(2);
		try {
			this.cuentaPepe.addTitular(pepe);
			this.cuentaPepe.insert();
			this.cuentaPepe.ingresar(1000);
			this.cuentaAna.addTitular(ana);
			this.cuentaAna.insert();
			this.cuentaAna.ingresar(5000);
			this.tcPepe = this.cuentaPepe.emitirTarjetaCredito(pepe.getNif(), 2000);
			this.tcPepe.cambiarPin(this.tcPepe.getPin(), 1234);
			this.tcAna = this.cuentaAna.emitirTarjetaCredito(ana.getNif(), 10000);
			this.tcAna.cambiarPin(this.tcAna.getPin(), 1234);

		} catch (Exception e) {
			fail("Excepción inesperada en setUp(): " + e);
		}
	}

	@Test
	public void comprar_bloqueoDeTarjeta() {
		try {
			this.tcPepe.comprar(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			this.tcPepe.comprar(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			this.tcPepe.comprar(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			this.tcPepe.comprar(1234, 100);
		} catch (TarjetaBloqueadaException e) {
			assertFalse(tcPepe.isActiva());
		} catch (Exception e) {
			fail("Esperaba TarjetaBloqueadaException");
		}
	}

	@Test
	public void comprar_creditoInsuficiente() {

		try {
			this.tcPepe.comprar(1234, 3000);
			fail("Excepción esperada: SaldoInsuficienteException");
		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
			assertEquals(2000.0, tcPepe.getCreditoDisponible());

		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void comprar_importeInvalido() {

		try {
			this.tcPepe.comprar(1234, -1);
			fail("Excepción esperada: ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
			assertEquals(2000.0, tcPepe.getCreditoDisponible());

		} catch (SaldoInsuficienteException e) {
			e.printStackTrace();

		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void comprar_exito() {

		try {
			tcPepe.comprar(1234, 1000);
		} catch (ImporteInvalidoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SaldoInsuficienteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TarjetaBloqueadaException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (PinInvalidoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		assertEquals(1000.0, tcPepe.getCreditoDisponible());
		assertEquals(1000.0, cuentaPepe.getSaldo());

	}

	@Test
	public void comprarPorInternet_bloqueoDeTarjeta() {
		try {
			this.tcPepe.comprarPorInternet(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			this.tcPepe.comprarPorInternet(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			this.tcPepe.comprarPorInternet(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			this.tcPepe.comprarPorInternet(1234, 100);
		} catch (TarjetaBloqueadaException e) {
		} catch (Exception e) {
			fail("Esperaba TarjetaBloqueadaException");
		}
	}

	@Test
	public void comprarPorInternet_creditoInsuficiente() {

		try {
			this.tcPepe.comprarPorInternet(1234, 3000);
			fail("Excepción esperada: SaldoInsuficienteException");
		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
			assertEquals(2000.0, tcPepe.getCreditoDisponible());

		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void comprarPorInternet_importeInvalido() {

		try {
			this.tcPepe.comprarPorInternet(tcPepe.getPin(), -1);
			fail("Excepción esperada: ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
			assertEquals(2000.0, tcPepe.getCreditoDisponible());

		} catch (SaldoInsuficienteException e) {
			e.printStackTrace();

		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void comprarPorInternet_tokenErroneo() {

		int token = 5678;

		try {
			tcPepe.comprarPorInternet(tcPepe.getPin(), 1000);
		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			tcPepe.confirmarCompraPorInternet(token);
			fail("Excepción esperada: (TokenInvalidoException)");

		} catch (TokenInvalidoException e) {
			assertEquals(2000.0, tcPepe.getCreditoDisponible());
			assertEquals(1000.0, cuentaPepe.getSaldo());

		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void comprarPorInternet_exito() {

		int token = 1234;
		try {
			tcPepe.comprarPorInternet(tcPepe.getPin(), 1000);
		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			tcPepe.confirmarCompraPorInternet(token);
		} catch (TokenInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(1000.0, tcPepe.getCreditoDisponible());
		assertEquals(1000.0, cuentaPepe.getSaldo());
	}

	@Test
	public void liquidar_exito() {

		try {
			tcPepe.comprar(1234, 100);
		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tcPepe.liquidar();
		assertEquals(900.0, cuentaPepe.getSaldo());
	}

	@Test
	public void liquidar_duplicado() {

		try {
			tcPepe.comprar(1234, 100);
		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tcPepe.liquidar();
		tcPepe.liquidar();
		tcPepe.liquidar();
		assertEquals(900.0, cuentaPepe.getSaldo());
	}

	@Test
	public void sacarDinero_pinIncorrecto() {

		try {
			tcPepe.sacarDinero(5678, 1000);
			fail("Excepción esperada: (SaldoInsuficienteException)");

		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			assertEquals(2000.0, tcPepe.getCreditoDisponible());
			assertEquals(1000.0, cuentaPepe.getSaldo());

		}
	}

	@Test
	public void sacarDinero_creditoInsuficiente() {

		try {
			tcPepe.sacarDinero(1234, 5000);
			fail("Excepción esperada: (SaldoInsuficienteException)");

		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
			assertEquals(2000.0, tcPepe.getCreditoDisponible());
			assertEquals(1000.0, cuentaPepe.getSaldo());

		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void sacarDinero_importeInvalido() {

		try {
			tcPepe.sacarDinero(1234, -1);
			fail("Excepción esperada: (ImporteInvalidoException)");

		} catch (ImporteInvalidoException e) {
			assertEquals(2000.0, tcPepe.getCreditoDisponible());
		} catch (SaldoInsuficienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void sacarDinero_exito() {

		try {
			tcPepe.sacarDinero(tcPepe.getPin(), 2000.0);
			assertEquals(-3.0, tcPepe.getCreditoDisponible());

		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TarjetaBloqueadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void cambiarPin_error() {
		int pin_nuevo = 3456;

		try {
			tcPepe.cambiarPin(pin_nuevo, pin_nuevo);
			fail("Excepción esperada: (PinInvalidoException)");
		} catch (PinInvalidoException e) {
			assertTrue(1234 == tcPepe.getPin());
		}
	}

	@Test
	public void cambiarPin_exito() {
		int pin_nuevo = 3456;

		try {
			tcPepe.cambiarPin(tcPepe.getPin(), pin_nuevo);
			assertTrue(pin_nuevo == tcPepe.getPin());
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
