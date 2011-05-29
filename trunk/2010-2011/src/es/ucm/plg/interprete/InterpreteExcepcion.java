package es.ucm.plg.interprete;

@SuppressWarnings("serial")
public class InterpreteExcepcion extends Exception{
	
	public static final String LECTURA_ESCRITURA = "Error en la lectura / escritura";
	public static final String FALTA_PARAMETRO = "La instruccion necesita un parametro";
	public static final String SOBRA_PARAMETRO = "La instruccion no acepta parametros";
	public static final String TIPO_INCORRECTO = "El tipo del parámetro es incorrecto";
	public static final String DIRECCION_INVALIDA = "Dirección de memoria inválida";
	
	private String message;
	private String instruccion;
	
	public InterpreteExcepcion(String instruccion, String message) {
		this.message = message;
		this.instruccion = instruccion;
	}
	
	@Override
	public String getMessage() {
		return "(" + instruccion + ") - " + message ;
	}

}
