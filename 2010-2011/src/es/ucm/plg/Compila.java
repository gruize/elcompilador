package es.ucm.plg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

import es.ucm.plg.compilador.analizadorLexico.AnalizadorLexico;
import es.ucm.plg.compilador.analizadorLexico.DatosToken;
import es.ucm.plg.compilador.analizadorSintactico.AnalizadorSintactico;
import es.ucm.plg.compilador.tablaSimbolos.GestorTS;
import es.ucm.plg.interprete.EscritorPila;
import es.ucm.plg.interprete.InstruccionInterprete;

public class Compila {

	public static void main(String[] args) {

		String programa = "";
		String path = "";

		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(isr);

		try {
			System.out.println();
			System.out
					.print("Desea ejecutar el programa en modo depuracion? (s/n)");
			System.out.println();

			String sn = in.readLine();
			boolean depuracion = (sn.equals("s"));

			path = args[0];
			File input = new File(path);

			BufferedReader br = new BufferedReader(new FileReader(input));
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

			AnalizadorLexico lexico = new AnalizadorLexico(programa);
			AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);

			sintactico.iniciaSintactico();

			if (depuracion) {

				System.out.println();
				System.out.println("***TOKENS RECONOCIDOS***");

				for (DatosToken tk : lexico.getTokens()) {
					System.out.println(tk.toString());
				}

				System.out.println();
				System.out.println("***CODIGO PILA GENERADO***");
				
				for (InstruccionInterprete ins : sintactico.getCodigo()) {
					System.out.println(ins.toString());
				}

				System.out.println();
				System.out.println("***TABLA DE SIMBOLOS***");

				GestorTS.getInstancia().ts().salidaTS();
			}

			EscritorPila ep = new EscritorPila();
			File f = new File(args[1]);

			if (!f.exists()) {
				f.createNewFile();
			}

			ep.escribirPrograma(sintactico.getCodigo(), f);
			
			System.out.println();
			System.out.println("**********************************");
			System.out.println("***COMPILADO SATISFACTORIAMENTE***");
			System.out.println("**********************************");

		} catch (Exception e2) {
			System.out.println("***************************");
			System.out.println("***COMPILADO CON ERRORES***");
			System.out.println("***************************");
			e2.printStackTrace();
			System.out.println("***************************");
		}
	}

}
