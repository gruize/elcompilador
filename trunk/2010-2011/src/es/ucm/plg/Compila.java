package es.ucm.plg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import es.ucm.plg.compilador.analizadorLexico.AnalizadorLexico;
import es.ucm.plg.compilador.analizadorSintactico.AnalizadorSintactico;
import es.ucm.plg.interprete.EscritorPila;

public class Compila {

	public static void main(String[] args) {
		String programa = "";
		String path = "";
		
		try {
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
			
			EscritorPila ep = new EscritorPila();
			File f = new File(args[1]);
			
			if (!f.exists()) {
				f.createNewFile();
			}
			
			ep.escribirPrograma(sintactico.getCodigo(), f);

		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

}
