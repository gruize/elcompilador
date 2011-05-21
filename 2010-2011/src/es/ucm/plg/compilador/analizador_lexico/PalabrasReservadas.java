package es.ucm.plg.compilador.analizador_lexico;

import java.util.Hashtable;

public class PalabrasReservadas {
		
	public static String TOKEN_INT = "tkint";
	public static String TOKEN_REAL = "tkreal";
	public static String TOKEN_IN = "tkin";
	public static String TOKEN_OUT = "tkout";
	public static String TOKEN_ID = "tkid";
	public static String TOKEN_PUNTO_COMA = "tk;";
	public static String TOKEN_PUNTO = "tk.";
	public static String TOKEN_MENOR = "tk<";
	public static String TOKEN_MAYOR = "tk>";
	public static String TOKEN_MENOR_IGUAL = "tk<=";
	public static String TOKEN_MAYOR_IGUAL = "tk>=";
	public static String TOKEN_ASIGNACION = "tk=";
	public static String TOKEN_IGUAL = "tk==";
	public static String TOKEN_DISTINTO = "tk!=";
	public static String TOKEN_NEGACION = "tk!";
	public static String TOKEN_SUMA = "tk+";
	public static String TOKEN_RESTA = "tk-";
	public static String TOKEN_MULT = "tk*";
	public static String TOKEN_DIV = "tk/";
	public static String TOKEN_MODULO = "tk%";
	public static String TOKEN_BARRA = "tk|";
	public static String TOKEN_PARENTESIS_AP = "tk(";
	public static String TOKEN_PARENTESIS_CE = "tk)";
	public static String TOKEN_CAST_INT = "tkcastint";
	public static String TOKEN_CAST_REAL = "tkcastreal";
	public static String TOKEN_Y_LOGICA = "tk&&";
	public static String TOKEN_O_LOGICA = "tk||";
	public static String TOKEN_POINTER = "tkpointer";
	public static String TOKEN_COMA = "tkcoma";
	public static String TOKEN_AMSPERSAND_VALOR = "tk&";
	public static String TOKEN_PUNTERO_FLECHA = "tk^";
	public static String TOKEN_CORCHETE_AB= "tk[";
	public static String TOKEN_CORCHETE_CE = "tk]";
	public static String TOKEN_REC = "tkrec";
	public static String TOKEN_ENDREC = "tkendrec";
	public static String TOKEN_IF = "tkif";
	public static String TOKEN_ELSE = "tkelse";
	public static String TOKEN_THEN = "tkthen";
	public static String TOKEN_END_IF = "tkendif";
	public static String TOKEN_WHILE = "tkwhile";
	public static String TOKEN_DO = "tkdo";
	public static String TOKEN_END_WHILE = "tkendwhile";
	public static String TOKEN_RESERVA = "tkalloc";
	public static String TOKEN_LIBERA = "tkfree";

	private Hashtable<String, String> palabrasReservadas = new Hashtable<String, String>();

	/**
	 * Gestiona las palabras reservadas del programa
	 */
	public PalabrasReservadas() {
		palabrasReservadas.put("int", "tkint");
		palabrasReservadas.put("real", "tkreal");
		palabrasReservadas.put("in", "tkin");
		palabrasReservadas.put("out", "tkout");
		palabrasReservadas.put("if", "tkif");
		palabrasReservadas.put("else", "tkelse");
		palabrasReservadas.put("endif", "tkendif");
		palabrasReservadas.put("then", "tkthen");
		palabrasReservadas.put("while", "tkwhile");
		palabrasReservadas.put("do", "tkdo");
		palabrasReservadas.put("endwhile", "tkendwhile");
		palabrasReservadas.put("tipo", "tktipo");
		palabrasReservadas.put("rec", "tkrec");
		palabrasReservadas.put("endrec", "tkendrec");
		palabrasReservadas.put("pointer", "tkpointer");
		palabrasReservadas.put("null", "tknull");
		palabrasReservadas.put("alloc", "tkalloc");
		palabrasReservadas.put("free", "tkfree");
		palabrasReservadas.put("end", "tkend");
		palabrasReservadas.put("return", "tkreturn");
		palabrasReservadas.put("fun", "tkfun");
	}

	/**
	 * @param palabra
	 *            Identificador del token leido
	 * @return Identificador especifico del token si es una palabra reservada y
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
