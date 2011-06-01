package es.ucm.plg.compilador.analizadorSintactico;

@SuppressWarnings("serial")
public class SintacticoException extends Exception{
	
	public static final String EXPRESION_INVALIDA = "Expresion invalida";
	public static final String REFERENCIA_ERRONEA = "Referencia erronea";
	public static final String FALTA_PARENTESIS = "Falta parentesis de cierre";
	public static final String FALTA_CORCHETE = "Falta corchete de cierre";
	public static final String FALTA_DESCTIPO = "Se esperaba una descripcion de tipo";
	public static final String FALTA_ID = "Se esperaba un identificador";
	public static final String FALTA_PUNTO_COMA = "Se esperaba un punto y coma";
	public static final String VARIABLE_NO_DECLARADA = "Acceso a una variable no declarada";
	public static final String VARIABLE_DUPLICADA = "Ya existe una variable con ese identificador";
	public static final String TIPO_INCOMPATIBLE = "Los tipos no son compatibles";
	public static final String FUNCION_NO_DECLARADA = "Funcion no declarada";
	
	private String message;
	private String lexema;
	private int linea;
	private int columna;
	
	public SintacticoException(String message, String lexema, int linea, int columna) {
		this.message = message;
		this.lexema = lexema;
		this.linea = linea;
		this.columna = columna;
	}
	
	@Override
	public String getMessage() {
		return "(" + linea + "," + columna + ") - " + lexema + " - " + message ;
	}

}
