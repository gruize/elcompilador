package es.ucm.plg.interprete.test;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;

public class MaquinaPTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MaquinaPTest.class);

	}

	public void test1() throws IOException, InterpreteExcepcion {
		File f = new File("test/test1/codigoP.bc");

		Interprete interprete = new Interprete(false);

		interprete.leerPrograma(f);
		interprete.ejecutarPrograma();

	}
}
