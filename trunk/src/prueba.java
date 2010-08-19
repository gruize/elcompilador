import java.io.BufferedReader;
import java.io.FileReader;

import errores.GestorErrores;
import analizadorLexico.AnalizadorLexico;


public class prueba {
	
	public static void main(String[] args) throws Exception {
		String programa = null;
		String codigo = "";
		int i = 0;
		//Lectura de archivo
		BufferedReader in = new BufferedReader(new FileReader("C://Documents and Settings//Gabi//Escritorio//II 2009-2010//PLG//PLGconAli//Pruebas//PruebaDeLiterales.txt"));
		StringBuffer sb = new StringBuffer();
		String st;
		while((st = in.readLine()) != null){
			sb.append(st+"\n");
		}
		in.close();
		programa = sb.toString();
		System.out.println(programa);
		System.out.println();
		int tam = programa.length() - 1;
		System.out.println("--------" + String.valueOf(tam) + "------------");
		//Fin de lectura de archivo
		AnalizadorLexico alexico = new AnalizadorLexico(programa);
		while(!alexico.isFin_programa()){
			alexico.scanner();
			codigo = codigo + "[" + alexico.getToken_actual() + "]\n";
			i++;
		}
		System.out.println(codigo);
		//System.out.println(GestorErrores.escribe());
	}

}
