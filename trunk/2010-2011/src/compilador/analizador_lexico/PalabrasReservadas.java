package compilador.analizador_lexico;

import java.util.Hashtable;

/**
 * @author Alicia Pérez Jiménez, Gabriela Ruíz Escobar
 */

public class PalabrasReservadas {

	private Hashtable<String, String> palabrasReservadas = new Hashtable<String, String>();

	/**
	 * Gestiona las palabras reservadas del programa
	 */
	public PalabrasReservadas() {
		palabrasReservadas.put("int", "tkint");
		palabrasReservadas.put("real", "tkreal");
		palabrasReservadas.put("in", "tkin");
		palabrasReservadas.put("out", "tkout");
	}

	/**
	 * @param palabra
	 *            Identificador del token leido
	 * @return Identificador específico del token si es una palabra reservada y
	 *         "tkid" en caso contrario
	 */
	public String obtenerToken(String palabra) {
		String token = "tkid";

		if (getToken(palabra))
			token = palabrasReservadas.get(palabra);

		return token;
	}

	
	/**
	 * @param palabra Identificador del que deseamos conocer si es palabra reservada
	 * @return true si es una palabra reservada y false en caso contrario
	 */
	public boolean getToken(String palabra) {
		return palabrasReservadas.containsKey(palabra);
	}

}
