package errores;

import java.util.Hashtable;

public class Errores {

	private Hashtable<Integer,String> errores = new Hashtable<Integer,String>();
	
	public Errores(){
		//Errores lexicos
		errores.put(1,"Caracter erroneo (General)" );
		errores.put(2,"Caracter desconocido");
		errores.put(3, "Caracter erroneo. Se esperaba un =");
		errores.put(4, "Caracter erroneo. Se esperaba un digito entre 1..9");
		errores.put(5, "Caracter erroneo. Se esperaba un >");
		errores.put(6, "Caracter erroneo. Se esperaba un digito");
		errores.put(7, "Caracter erroneo. Se esperaba un digito o un simbolo -");
		errores.put(8, "Caracter erroneo. Se esperaba un digito o una letra");
		errores.put(9, "Caracter erroneo. Se esperaba una comilla simple");
		errores.put(10, "Caracter erroneo. No se admiten 0s a la izquierda");
		//Errores sintacticos
		errores.put(11, "Token inesperado (General)");
		errores.put(12, "Token inesperado. Sección de declaracion");
		//Errores semanticos
		errores.put(13, "Error semantico (General)");
		
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
