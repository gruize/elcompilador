package maquinaP;

import interprete.EscritorPila;
import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;
import interprete.instrucciones.Apilar;
import interprete.instrucciones.ApilarDir;
import interprete.instrucciones.DesapilarDir;
import interprete.instrucciones.Restar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

public class MaquinaPTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MaquinaPTest.class);

	}

	public void test1() throws IOException {
		File f = new File("prueba1");
		try {
			escribirPrograma(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void escribirPrograma(File f) throws Exception {

		ArrayList<InstruccionInterprete> ai = new ArrayList<InstruccionInterprete>();
		ai.add(new Apilar(new DatoPila(DatoPila.INT, 15)));
		ai.add(new DesapilarDir(new DatoPila(DatoPila.INT, 0)));
		ai.add(new ApilarDir(new DatoPila(DatoPila.INT, 0)));
		
		ai.add(new Apilar(new DatoPila(DatoPila.INT, 5)));
		ai.add(new Restar());

		EscritorPila ep = new EscritorPila();
		ep.escribirPrograma(ai, f);

		Interprete interprete = new Interprete(true);

		interprete.leerPrograma(f);
		interprete.ejecutarPrograma();
		System.out.print(interprete.mostrarEstado());

	}

}
