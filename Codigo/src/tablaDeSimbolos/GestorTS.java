package tablaDeSimbolos;

public class GestorTS {

	/**
	 * La utilidad de este gestor es tener una unica instancia de la pila tabla 
	 * de simbolos, por lo que se requiere el uso del patron Singlenton
	 */
	
	private static PilaTS ts;
	
	public static PilaTS getInstancia(){
		if(ts == null)
			ts = new PilaTS();
		return ts;
	}
}
