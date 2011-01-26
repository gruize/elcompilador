import interprete.EscritorPila;
import interprete.Interprete;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

import compilador.analizador_lexico.AnalizadorLexico;
import compilador.analizador_sintactico.AnalizadorSintactico;

public class Practica {

	public static void main(String[] args) {
		String programa = "";
		String path = "";
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(isr);
		System.out.print("Introduzca la ruta del código: ");
		try {
			path = in.readLine();
			File input = new File(path);

			BufferedReader br = new BufferedReader(new FileReader(input));
			String linea = br.readLine();
			if (linea != null) {
				programa += linea;
				linea = br.readLine();
				while (linea != null) {
					// Aquí lo que tengamos que hacer con la línea puede ser
					// esto
					programa += System.getProperty("line.separator");
					programa += linea;
					linea = br.readLine();
				}
			}

			AnalizadorLexico lexico = new AnalizadorLexico(programa);
			AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);

			sintactico.iniciaSintactico();

			EscritorPila ep = new EscritorPila();
			File f = new File(input.getParent() + "\\codigoP.bc");
			if (!f.exists()) {
				f.createNewFile();
			}
			ep.escribirPrograma(sintactico.getCodigo(), f);

			Interprete interprete = new Interprete(true);

			interprete.leerPrograma(f);
			interprete.ejecutarPrograma();

		} catch (Exception e2) {
			System.out.println("Error");
		}
	}

}
