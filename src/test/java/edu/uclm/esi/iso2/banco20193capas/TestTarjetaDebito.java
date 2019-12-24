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
import edu.uclm.esi.iso2.banco20193capas.model.ManagerHelper;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaDebito;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTarjetaDebito extends TestCase {
	private Cuenta cuentaPepe, cuentaAna;
	private Cliente pepe, ana;
	private TarjetaDebito tdPepe, tdAna;
	
	@Before
	public void setUp() {
		ManagerHelper.getMovimientoDAO().deleteAll();
		ManagerHelper.getMovimientoTarjetaCreditoDAO().deleteAll();
		ManagerHelper.getTarjetaCreditoDAO().deleteAll();
		ManagerHelper.getTarjetaDebitoDAO().deleteAll();
		ManagerHelper.getCuentaDAO().deleteAll();
		ManagerHelper.getClienteDAO().deleteAll();
		
		this.pepe = new Cliente("12345X", "Pepe", "Pérez");
		this.pepe.insert();
		
		this.ana = new Cliente("98765F", "Ana", "López");
		this.ana.insert();
		
		this.cuentaPepe = new Cuenta(1L);
		this.cuentaAna = new Cuenta(2L);
		
		try {
			this.cuentaPepe.addTitular(pepe);
			this.cuentaPepe.insert();
			this.cuentaPepe.ingresar(1000);
			
			this.cuentaAna.addTitular(ana);
			this.cuentaAna.insert();
			this.cuentaAna.ingresar(5000);
			
			this.tdPepe = this.cuentaPepe.emitirTarjetaDebito(pepe.getNif());
			this.tdAna = this.cuentaAna.emitirTarjetaDebito(ana.getNif());
			
			this.tdPepe.cambiarPin(tdPepe.getPin(), 1234);
			this.tdAna.cambiarPin(tdAna.getPin(), 1234);
		}
		catch (Exception e) {
			fail("Excepción inesperada en setUp(): " + e);
		}
	}
	
	@Test
	public void comprarPorInternetBloqueoDeTarjeta() {
		try {
			tdPepe.comprarPorInternet(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			tdPepe.comprar(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			tdPepe.comprar(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			tdPepe.comprar(1234, 100);
		} catch (TarjetaBloqueadaException e) {
			assertFalse(tdPepe.isActiva());
		} catch (Exception e) {
			fail("Esperaba TarjetaBloqueadaException");
		}
	}
	
	@Test
	public void comprarPorInternet_tokenErroneo() {
		
		int token = 5678;
		
		try {
			tdPepe.comprarPorInternet(tdPepe.getPin(), 1000);
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
			tdPepe.confirmarCompraPorInternet(token);
			fail("Excepción esperada: (TokenInvalidoException)");

		} catch (TokenInvalidoException e) {
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
			tdPepe.comprarPorInternet(tdPepe.getPin(), 1000);
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
			tdPepe.confirmarCompraPorInternet(token);
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
		assertEquals(0.0, cuentaPepe.getSaldo());
	}	
	
	@Test
	public void comprar_bloqueoDeTarjeta() {
		try {
			tdPepe.comprar(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			tdPepe.comprar(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			tdPepe.comprar(5678, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			tdPepe.comprar(1234, 100);
		} catch (TarjetaBloqueadaException e) {
			assertFalse(tdPepe.isActiva());
		} catch (Exception e) {
			fail("Esperaba TarjetaBloqueadaException");
		}
	}
	
	@Test
	public void comprar_saldoInsuficiente() {
		
		try {
			this.tdPepe.comprar(1234, 3000);
			fail("Excepción esperada: SaldoInsuficienteException");
		} catch (ImporteInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaldoInsuficienteException e) {
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
	public void comprar_importeInvalido() {
		
		try {
			this.tdPepe.comprar(1234, -1);
			fail("Excepción esperada: ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
			assertEquals(1000.0,  cuentaPepe.getSaldo());

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
			tdPepe.comprar(1234, 1000);
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

		assertEquals(0.0, cuentaPepe.getSaldo());
	}
	
	@Test
	public void sacarDinero_importeInvalido() {
		
		try {
			tdPepe.sacarDinero(1234, -1);
			fail("Excepción esperada: (ImporteInvalidoException)");

		} catch (ImporteInvalidoException e) {
			assertEquals(1000.0, cuentaPepe.getSaldo());
			
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
			tdPepe.sacarDinero(tdPepe.getPin(), 1000.0);
			assertEquals(0.0, cuentaPepe.getSaldo());

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
		int pin_nuevo=3456;
		
		try {
			tdPepe.cambiarPin(pin_nuevo, pin_nuevo);
			fail("Excepción esperada: (PinInvalidoException)");
		} catch (PinInvalidoException e) {
			assertTrue(1234 == tdPepe.getPin());
		}
	}	
	
	@Test
	public void cambiarPin_exito() {
		int pin_nuevo=3456;
		
		try {
			tdPepe.cambiarPin(tdPepe.getPin(), pin_nuevo);
			assertTrue(pin_nuevo == tdPepe.getPin());
		} catch (PinInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
}
