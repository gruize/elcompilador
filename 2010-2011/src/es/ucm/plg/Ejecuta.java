package es.ucm.plg;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import es.ucm.plg.interprete.Interprete;

public class Ejecuta {

	public static void main(String[] args) {

		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(isr);
		try {
			System.out.println();
			System.out.print("Desea ejecutar el programa en modo depuracion? (s/n)");
			System.out.println();
			
			String sn = in.readLine();
			boolean depuracion = (sn.equals("s"));

			Interprete interprete = new Interprete(depuracion);

			interprete.leerPrograma(new File(args[0]));
			interprete.ejecutarPrograma();

		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

}
