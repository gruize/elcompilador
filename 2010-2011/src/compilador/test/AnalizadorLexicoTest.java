package compilador.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

import compilador.analizador_lexico.AnalizadorLexico;
import compilador.gestorErrores.GestorErrores;

public class AnalizadorLexicoTest extends TestCase {

	// public final String prueba1Result = "[DatosToken [token=tkreal, fila=0,
	// columna=4, indice=4], DatosToken [token=tkid, fila=0, columna=13,
	// indice=13], DatosToken [token=tk;, fila=0, columna=14, indice=14],
	// DatosToken [token=tkint, fila=1, columna=3, indice=18], DatosToken
	// [token=tkid, fila=1, columna=9, indice=24], DatosToken [token=tk;,
	// fila=1, columna=10, indice=25], DatosToken [token=tkreal, fila=2,
	// columna=4, indice=30], DatosToken [token=tkid, fila=2, columna=13,
	// indice=39], DatosToken [token=tk;, fila=2, columna=14, indice=40],
	// DatosToken [token=tkout, fila=3, columna=3, indice=44], DatosToken
	// [token=tkid, fila=3, columna=9, indice=50], DatosToken [token=tk=,
	// fila=3, columna=11, indice=52], DatosToken [token=tkcastint, fila=3,
	// columna=17, indice=58], DatosToken [token=tk(, fila=3, columna=18,
	// indice=59], DatosToken [token=tkin, fila=3, columna=20, indice=61],
	// DatosToken [token=tkid, fila=3, columna=29, indice=70], DatosToken
	// [token=tk), fila=3, columna=30, indice=71], DatosToken [token=tk;,
	// fila=3, columna=31, indice=72], DatosToken [token=tkout, fila=4,
	// columna=3, indice=76], DatosToken [token=tkid, fila=4, columna=12,
	// indice=85], DatosToken [token=tk=, fila=4, columna=14, indice=87],
	// DatosToken [token=tkid, fila=4, columna=23, indice=96], DatosToken
	// [token=tk-, fila=4, columna=25, indice=98], DatosToken [token=tkid,
	// fila=4, columna=31, indice=104], DatosToken [token=tk;, fila=4,
	// columna=32, indice=105]]";

	public static void main(String[] args) {

		junit.textui.TestRunner.run(AnalizadorLexicoTest.class);

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

		System.out.println();
		System.out.println("----tokens generados:");
		System.out.println();
		System.out.println(tokens);
		System.out.println();
		System.out.println();

		// assertEquals(prueba1Result, lexico.getTokens().toString());
	}

	public void test2() throws IOException {

		String programa = "";
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"test/test2/prueba_bien_1.txt")));
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
			System.out.println("���ERROR!!!");
			System.out.println(GestorErrores.getErrores_propios().toString());
		} else {
			System.out.println();
			System.out.println("----tokens generados:");
			System.out.println();
			System.out.println(tokens);
			System.out.println();
			System.out.println();
		}

		// assertEquals(prueba1Result, lexico.getTokens().toString());
	}

}
