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
		errores.put(14, "Identificador duplicado");
		errores.put(15, "Procedimiento no especificado");
		errores.put(16, "Se esperaba un puntero");
		errores.put(17, "Variable no declarada");
		errores.put(18, "Operador inadecuado para el nivel de la expresion");
		errores.put(19, "Procedimiento no declarado");
		errores.put(20, "Invocacion de procedimiento sin sus correspondientes parametros");
		errores.put(21, "Tipos incompatibles");
		errores.put(22, "Tipos incompatibles. Deben ser naturales o enteros");
		errores.put(23, "Tipo incompatible. Debe ser un booleano");
		errores.put(24, "Referencia erronea");
		errores.put(25, "Tipos incompatibles. Debe ser un tipo referencia");
		errores.put(26, "Campo duplicado");
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
