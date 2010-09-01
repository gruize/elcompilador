import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectOutputStream;

import errores.GestorErrores;
import analizadorLexico.AnalizadorLexico;
import analizadorLexico.DatosToken;
import analizadorSintactico.AnalizadorSintactico;


public class Compilador {
	
	public static void main(String[] args) throws Exception {
		String programa = null;
		String codigo = "";
		if(args.length == 2){
			//Lectura de archivo
			BufferedReader in = new BufferedReader(new FileReader(args[0]));
			StringBuffer sb = new StringBuffer();
			String st;
			boolean fin = false;
			while((st = in.readLine()) != null){
				sb.append(st+"\n");
			}
			in.close();
			programa = sb.toString();
			System.out.println(programa);
			
			int tam = programa.length() - 1;
			System.out.println("--------" + String.valueOf(tam) + "------------");
			//Fin de lectura de archivo
			
			AnalizadorLexico alexico = new AnalizadorLexico(programa);
			AnalizadorSintactico asintactico = new AnalizadorSintactico(alexico);
			
			//TODO: Esto hay que cambiarlo por el ejecutador del sintactico introduciendo el lexico.
			while(!fin){
				alexico.scanner();
				codigo = codigo + "[" + alexico.getToken_actual() + "]" + "  Lexema: " + alexico.getLexema() + "\n";
				fin = alexico.isFin_programa();
			}
			codigo = codigo + "[tkStop]";
			alexico.getTokens().add(new DatosToken("tkStop",alexico.getIndice()));
			System.out.println(codigo);
			System.out.println(GestorErrores.escribe());
			//TODO: Hasta aqui --- Esto hay que cambiarlo por el ejecutador del sintactico introduciendo el lexico.
			
			
			//Escritura 
			FileOutputStream file = new FileOutputStream (args[1]+".obj");
			ObjectOutputStream obj = new ObjectOutputStream (file);
			obj.writeObject(asintactico.getCodigo());
			obj.flush();
			obj.close();
			file.close();
			
			//Escritura temporal
			BufferedWriter escritor = new BufferedWriter(new FileWriter(args[1]+".txt"));
			for(int i = 0; i < asintactico.getCodigo().size(); i++){
				escritor.write(asintactico.getCodigo().get(i).getOp().toString());
				if(asintactico.getCodigo().get(i).getArg() != null){
					escritor.write(asintactico.getCodigo().get(i).getArg().toString() + "\n");
				}else{
					escritor.write("\n");
				}
			}
			escritor.close();
		}else{
			System.out.println("Debe introducir la ruta del archivo que contiene el codigo del programa y la ruta del archivo donde desea guardar el resultado de la compilación");	
		}
	}

	private static void escritura(String ruta) {
			}
}
