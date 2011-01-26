package compilador.test;

import interprete.EscritorPila;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import compilador.analizador_lexico.AnalizadorLexico;
import compilador.analizador_sintactico.AnalizadorSintactico;

public class AnalizadorSintacticoTest extends TestCase {

//	public final String test1Result = "[ in , desapiladir 1, castint, desapiladir 2,  out , apiladir 1, apiladir 2, restar, castreal, desapiladir 3,  out ]";

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AnalizadorSintacticoTest.class);
	}

	public void test1() throws IOException {

		String programa = "";
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"test/test1/code.txt")));
		String linea = br.readLine();
		if (linea != null) {
			programa += linea;
			linea = br.readLine();
			while (linea != null) {
				// Aquí lo que tengamos que hacer con la línea puede ser esto
				programa += System.getProperty("line.separator");
				programa += linea;
				linea = br.readLine();
			}
		}

		System.out.println();
		System.out.println();
		System.out.println("TEST 1");
		System.out.println();
		System.out.println("----código:");
		System.out.println();
		System.out.println(programa);
		System.out.println();

		AnalizadorLexico lexico = new AnalizadorLexico(programa);
		AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);

		try {
			sintactico.iniciaSintactico();
		} catch (Exception e) {
			e.printStackTrace();
		}

//		assertEquals(test1Result, sintactico.getCodigo().toString());
//		assertEquals(false, sintactico.isError());

		System.out.println();
		System.out.println("----código pila generado:");
		System.out.println();
		System.out.println(sintactico.getCodigo().toString());

		EscritorPila ep = new EscritorPila();
		File f = new File("test/test1/codigoP.bc");
		if(!f.exists()){
			f.createNewFile();
		}
		try {
			ep.escribirPrograma(sintactico.getCodigo(), f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
