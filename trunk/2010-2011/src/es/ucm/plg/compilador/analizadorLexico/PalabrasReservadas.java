package es.ucm.plg.compilador.analizadorLexico;

import java.util.Hashtable;

/**
 * @author Alicia Pérez y Gabriela Ruíz
 *
 */
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
        public static String TOKEN_ELSIF = "tkelsif";
        public static String TOKEN_THEN = "tkthen";
        public static String TOKEN_END_IF = "tkendif";
        public static String TOKEN_WHILE = "tkwhile";
        public static String TOKEN_DO = "tkdo";
        public static String TOKEN_END_WHILE = "tkendwhile";
        public static String TOKEN_RESERVA = "tkalloc";
        public static String TOKEN_LIBERA = "tkfree";
        public static String TOKEN_TIPO = "tkTipo";
        public static String TOKEN_NULL = "tknull";
        public static String TOKEN_END = "tkend";
        public static String TOKEN_RETURN = "tkreturn";
        public static String TOKEN_RETURNS = "tkreturns";
        public static String TOKEN_FUN = "tkfun";
        public static String TOKEN_ELSE = "tkelse";

        private Hashtable<String, String> palabrasReservadas = new Hashtable<String, String>();

        /**
         * Gestiona las palabras reservadas del programa
         */
        public PalabrasReservadas() {
                palabrasReservadas.put("int", TOKEN_INT);
                palabrasReservadas.put("real", TOKEN_REAL);
                palabrasReservadas.put("in", TOKEN_IN);
                palabrasReservadas.put("out", TOKEN_OUT);
                palabrasReservadas.put("if", TOKEN_IF);
                palabrasReservadas.put("elsif", TOKEN_ELSIF);
                palabrasReservadas.put("else", TOKEN_ELSE);
                palabrasReservadas.put("endif", TOKEN_END_IF);
                palabrasReservadas.put("then", TOKEN_THEN);
                palabrasReservadas.put("while", TOKEN_WHILE);
                palabrasReservadas.put("do", TOKEN_DO);
                palabrasReservadas.put("endwhile", TOKEN_END_WHILE);
                palabrasReservadas.put("tipo", TOKEN_TIPO);
                palabrasReservadas.put("rec", TOKEN_REC);
                palabrasReservadas.put("endrec", TOKEN_ENDREC);
                palabrasReservadas.put("pointer", TOKEN_POINTER);
                palabrasReservadas.put("null", TOKEN_NULL);
                palabrasReservadas.put("alloc", TOKEN_RESERVA);
                palabrasReservadas.put("free", TOKEN_LIBERA);
                palabrasReservadas.put("end", TOKEN_END);
                palabrasReservadas.put("return", TOKEN_RETURN);
                palabrasReservadas.put("fun", TOKEN_FUN);
                palabrasReservadas.put("returns", TOKEN_RETURNS);
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