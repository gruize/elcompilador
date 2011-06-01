package es.ucm.plg.compilador.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import junit.framework.TestCase;
import es.ucm.plg.compilador.analizadorLexico.AnalizadorLexico;
import es.ucm.plg.compilador.analizadorLexico.DatosToken;

public class AnalizadorLexicoTest extends TestCase {

	public static void main(String[] args) {

		junit.textui.TestRunner.run(AnalizadorLexicoTest.class);

	}

	public void test1() throws Throwable {

		String programa = "";
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"test/test1/code.txt")));
		String linea = br.readLine();
		if (linea != null) {
			programa += linea;
			linea = br.readLine();
			while (linea != null) {
				programa += System.getProperty("line.separator");
				programa += linea;
				linea = br.readLine();
			}
		}

		System.out.println("TEST 1");
		System.out.println();
		System.out.println("----código:");
		System.out.println();
		System.out.println(programa);

		AnalizadorLexico lexico = new AnalizadorLexico(programa);
		ArrayList<String> tokens = new ArrayList<String>();

		while (!lexico.isFin_programa()) {
			lexico.scanner();
			tokens.add(lexico.getToken_actual());
		}
		
		for (DatosToken t : lexico.getTokens()) {
			System.out.println(t.getToken());
		}
	}

}
