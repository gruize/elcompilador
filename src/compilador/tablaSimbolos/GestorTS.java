package compilador.tablaSimbolos;

public class GestorTS {
	/**
	 * La utilidad de este gestor es tener una unica instancia de la pila tabla 
	 * de simbolos, por lo que se requiere el uso del patron Singlenton
	 */
	
	private static TS ts;
	
	public static TS getInstancia(){
		if(ts == null)
			ts = new TS();
		return ts;
	}
}
