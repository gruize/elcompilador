import java.io.BufferedReader;
import java.io.FileReader;

import errores.GestorErrores;
import analizadorLexico.AnalizadorLexico;


public class Compilador {
	
	public static void main(String[] args) throws Exception {
		String programa = null;
		String codigo = "";
		//Lectura de archivo
		BufferedReader in = new BufferedReader(new FileReader("C://Documents and Settings//Gabi//Escritorio//II 2009-2010//PLG//ultramegacompiladordelamuerte//pruebas//pruebaProcTiposComplejos.txt"));
		StringBuffer sb = new StringBuffer();
		String st;
		boolean fin = false;
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
		while(!fin){
			alexico.scanner();
			codigo = codigo + "[" + alexico.getToken_actual() + "]" + "  Lexema: " + alexico.getLexema() + "\n";
			fin = alexico.isFin_programa();
		}
		codigo = codigo + "[tkStop]";
		System.out.println(codigo);
		System.out.println(GestorErrores.escribe());
	}

}
