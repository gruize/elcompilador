package es.ucm.plg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

import es.ucm.plg.compilador.analizador_lexico.AnalizadorLexico;
import es.ucm.plg.compilador.analizador_sintactico.AnalizadorSintactico;
import es.ucm.plg.interprete.EscritorPila;
import es.ucm.plg.interprete.Interprete;

public class Practica {

	public static void main(String[] args) {
		String programa = "";
		String path = "";
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(isr);
		System.out.print("Introduzca la ruta del codigo: ");
		try {
			path = in.readLine();
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
			
			System.out.println();
			System.out.println(programa);
			System.out.println();
			System.out.print("¿Desea ejecutar el programa en modo depuracion? (s/n)");
			System.out.println();
			
			String sn = in.readLine();
			boolean depuracion = (sn.equals("s"));

			AnalizadorLexico lexico = new AnalizadorLexico(programa);
			AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);

			sintactico.iniciaSintactico();
			System.out.println();
			System.out.println();
			if (depuracion) {
				System.out.println("INSTRUCCIONES LEIDAS POR EL ANALIZADOR SINTACTICO: ");
				System.out.print(sintactico.getCodigo().toString());
			}
			System.out.println();
			System.out.println();

			EscritorPila ep = new EscritorPila();
			File f = new File(input.getParent() + "\\codigoP.bc");
			if (!f.exists()) {
				f.createNewFile();
			}
			ep.escribirPrograma(sintactico.getCodigo(), f);

			Interprete interprete = new Interprete(depuracion);

			interprete.leerPrograma(f);
			interprete.ejecutarPrograma();

		} catch (Exception e2) {
			System.out.println("Error");
		}
	}

}
