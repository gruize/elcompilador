package compilador.test;

import interprete.EscritorPila;
import interprete.Interprete;
import interprete.InterpreteException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import compilador.analizador_lexico.AnalizadorLexico;
import compilador.analizador_sintactico.AnalizadorSintactico;

public class AnalizadorSintacticoTest  extends TestCase {
	
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


		AnalizadorLexico lexico = new AnalizadorLexico(programa);
		AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);
		
		try {
			sintactico.iniciaSintactico();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		EscritorPila ep = new EscritorPila();
		File f = new File("test/test1/codigoP.bc");
		try {
			ep.escribirPrograma(sintactico.getCodigo(), f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Interprete interprete = new Interprete(true);

		try {
			interprete.leerPrograma(f);
			interprete.ejecutarPrograma();
		} catch (InterpreteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
