package es.ucm.plg.compilador.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;
import es.ucm.plg.compilador.analizador_lexico.AnalizadorLexico;
import es.ucm.plg.compilador.gestorErrores.GestorErrores;

public class AnalizadorLexicoTest extends TestCase {

	public static void main(String[] args) {

		junit.textui.TestRunner.run(AnalizadorLexicoTest.class);

	}

//	public void test1() throws IOException {
//
//		String programa = "";
//		BufferedReader br = new BufferedReader(new FileReader(new File(
//				"test/test1/code.txt")));
//		String linea = br.readLine();
//		if (linea != null) {
//			programa += linea;
//			linea = br.readLine();
//			while (linea != null) {
//				programa += System.getProperty("line.separator");
//				programa += linea;
//				linea = br.readLine();
//			}
//		}
//
//		System.out.println("TEST 1");
//		System.out.println();
//		System.out.println("----código:");
//		System.out.println();
//		System.out.println(programa);
//
//		AnalizadorLexico lexico = new AnalizadorLexico(programa);
//		ArrayList<String> tokens = new ArrayList<String>();
//
//		while (!lexico.isFin_programa()) {
//			lexico.scanner();
//			tokens.add(lexico.getToken_actual());
//		}
//
//		System.out.println();
//		System.out.println("----tokens generados:");
//		System.out.println();
//		System.out.println(tokens);
//		System.out.println();
//		System.out.println();
//	}
//
	public void test2() throws IOException {

		String programa = "";
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"test/test2/prueba_bien_1.txt")));
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

		if (GestorErrores.getErrores_propios().size() > 0) {
			System.out.println("¡¡¡ERROR!!!");
			System.out.println(GestorErrores.getErrores_propios().toString());
		} else {
			System.out.println();
			System.out.println("----tokens generados:");
			System.out.println();
			System.out.println(tokens);
			System.out.println();
			System.out.println();
		}
	}
//	
//	public void test4() throws IOException {
//	
//			String programa = "";
//			BufferedReader br = new BufferedReader(new FileReader(new File(
//					"test/test4/code.txt")));
//			String linea = br.readLine();
//			if (linea != null) {
//				programa += linea;
//				linea = br.readLine();
//				while (linea != null) {
//					programa += System.getProperty("line.separator");
//					programa += linea;
//					linea = br.readLine();
//				}
//			}
//	
//			System.out.println("TEST 4");
//			System.out.println();
//			System.out.println("----código:");
//			System.out.println();
//			System.out.println(programa);
//	
//			AnalizadorLexico lexico = new AnalizadorLexico(programa);
//			ArrayList<String> tokens = new ArrayList<String>();
//	
//			while (!lexico.isFin_programa()) {
//				lexico.scanner();
//				tokens.add(lexico.getToken_actual());
//			}
//	
//			System.out.println();
//			System.out.println("----tokens generados:");
//			System.out.println();
//			System.out.println(tokens);
//			System.out.println();
//			System.out.println();
//		}

}
