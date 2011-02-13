package es.ucm.plg.compilador.gestorErrores;

import java.util.Hashtable;

public class Errores {

	private Hashtable<Integer,String> errores = new Hashtable<Integer,String>();
	
	public Errores(){
		//Errores lexicos
		errores.put(1,"Caracter erroneo (General)" );
		errores.put(2,"Caracter desconocido");
		//Errores sintacticos
		errores.put(3, "Token inesperado (General)");
		errores.put(4, "Token inesperado. Secci�n de declaracion");
		//Errores semanticos
		errores.put(5, "Error semantico (General)");
		errores.put(6, "Identificador duplicado");
		errores.put(7, "Variable no declarada");
		errores.put(8, "Operador inadecuado para el nivel de la expresion");
		errores.put(9, "Tipos incompatibles");
	}

	public Hashtable<Integer, String> getErrores() {
		return errores;
	}

	public void setErrores(Hashtable<Integer, String> errores) {
		this.errores = errores;
	}
	
	public boolean existeError(int obj){
		return errores.contains(new Integer(obj));
	}
	
	public String obtenerDescripcion(int obj){
		String temp = null;
		if(existeError(obj))
			temp = errores.get(new Integer(obj));
		return temp;
	}
}
