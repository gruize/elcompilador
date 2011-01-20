package compilador.analizador_lexico;

import java.util.Vector;

import compilador.gestorErrores.GestorErrores;

/**
 * @author Alicia Pérez Jiménez, Gabriela Ruíz Escobar
 * 
 */

public class AnalizadorLexico {

	private static final int VACIO = 0;
	private static final int PUNTO = 1;
	private static final int MENOR = 2;
	private static final int MAYOR = 3;
	private static final int MENOR_IGUAL = 4;
	private static final int MAYOR_IGUAL = 5;
	private static final int IGUAL = 6;
	private static final int IGUAL_IGUAL = 7;
	private static final int DISTINTO = 8;
	private static final int SUMA = 9;
	private static final int RESTA = 10;
	private static final int MULTIPLICACION = 11;
	private static final int DIVISION = 12;
	private static final int MODULO = 13;
	private static final int Y_LOGICA = 14;
	private static final int O_LOGICA = 15;
	private static final int NEGACION = 16;
	private static final int PARENTESIS_ABIERTO = 17;
	private static final int PARENTESIS_CERRADO = 18;
	private static final int COMENTARIO = 26;
	private static final int PUNTO_Y_COMA = 24;
	private static final int LIT_INT = 25;
	private static final int LIT_REAL = 27;
	private static final int LIT_REAL1 = 28;
	private static final int LIT_REAL2 = 29;
	private static final int LIT_REAL3 = 30;
	private static final int LIT_REAL4 = 31;
	private static final int LIT_REAL5 = 32;
	private static final int LIT_REAL6 = 33;
	private static final int LIT_REAL7 = 34;
	private static final int LIT_REALF = 35;
	private static final int CADENA = 36;
	private static final int ARROBA = 38;
	private static final int COMENTARIO_INC = 39;
	private static final int CAST_I = 19;
	private static final int CAST_IN = 20;
	private static final int CAST_INT = 21;
	private static final int CAST_INT_FIN = 40;
	private static final int CAST_R = 44;
	private static final int CAST_RE = 41;
	private static final int CAST_REA = 42;
	private static final int CAST_REAL = 43;
	private static final int CAST_REAL_FIN = 45;
	private static final int AMPERSAND = 46;
	private static final int BARRA = 47;

	private PalabrasReservadas palabrasReserva = new PalabrasReservadas();
	private String lexema;
	private int estado;
	private String programa;
	private boolean fin_programa;
	private int fila;
	private int columna;
	private char next_char;
	private Vector<DatosToken> tokens;
	private String token_actual;
	private int indice;
	private int parentesis_indice;
	private int parentesis_fila;
	private int parentesis_columna;

	/**
	 * Convierte el programa de entrada (cadena de símbolos alfanuméricos) en
	 * una secuencia de componentes léxicos o Tokens
	 * 
	 * @param programa
	 *            Cadena con los caracteres alfanuméricos que componen el
	 *            programa
	 */
	public AnalizadorLexico(String programa) {
		super();
		this.programa = programa;
		this.fin_programa = false;
		this.fila = 0;
		this.columna = 0;
		this.tokens = new Vector<DatosToken>();
		this.indice = 0;
		this.next_char = siguienteCaracter();
		this.token_actual = null;
		this.lexema = "";
		this.estado = VACIO;
	}

	public String getLexema() {
		return lexema;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}

	public int getColumna() {
		return columna;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}

	public String getToken_actual() {
		return token_actual;
	}

	public void setToken_actual(String tokenActual) {
		token_actual = tokenActual;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	/**
	 * Devuelve un vector con los componentes léxicos procesados
	 * 
	 * @return Tokens procesados
	 */
	public Vector<DatosToken> getTokens() {
		return tokens;
	}

	/**
	 * Procesa el programa de entrada
	 */
	public void scanner() {
		boolean encontrado = false;

		this.estado = VACIO;
		this.lexema = "";

		if (!fin_programa) {
			while (!encontrado) {

				if (programa.length() == indice + 1)
					fin_programa = true;

				switch (estado) {

				case VACIO:

					if (sigDigito()) {
						if (next_char == '0')
							transita(LIT_REAL);
						else
							transita(LIT_INT);
					} else {
						if (sigLetra())
							transita(CADENA);
						else {
							switch (next_char) {
							case '\n':
								transitaSinLexema(VACIO);
								this.fila++;
								this.columna = 0;
								break;
							case ' ':
								transitaSinLexema(VACIO);
								break;
							case '\t':
								transitaSinLexema(VACIO);
								break;
							case ';':
								transita(PUNTO_Y_COMA);
								break;
							case '.':
								transita(PUNTO);
								break;
							case '<':
								transita(MENOR);
								break;
							case '>':
								transita(MAYOR);
								break;
							case '=':
								transita(IGUAL);
								break;
							case '+':
								transita(SUMA);
								break;
							case '-':
								transita(RESTA);
								break;
							case '*':
								transita(MULTIPLICACION);
								break;
							case '/':
								transita(DIVISION);
								break;
							case '%':
								transita(MODULO);
								break;
							case '|':
								transita(BARRA);
								break;
							case '(':
								transita(PARENTESIS_ABIERTO);
								break;
							case ')':
								transita(PARENTESIS_CERRADO);
								break;
							case '@':
								transita(ARROBA);
								break;
							case '&':
								transita(AMPERSAND);
								break;
							default:
								GestorErrores.agregaError(2, fila, columna,
										next_char + "");
								transitaSinLexema(VACIO);
								break;
							}
						}
					}
					break;

				case PUNTO_Y_COMA:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_PUNTO_COMA);
					break;

				case PUNTO:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_PUNTO);
					break;

				case MENOR:

					switch (next_char) {

					case '=':
						transita(MENOR_IGUAL);
						break;

					default:
						encontrado = true;
						encontrado(PalabrasReservadas.TOKEN_MENOR);
						break;
					}

					break;

				case MAYOR:

					switch (next_char) {

					case '=':
						transita(MAYOR_IGUAL);
						break;

					default:
						encontrado = true;
						encontrado(PalabrasReservadas.TOKEN_MAYOR);
						break;
					}

					break;

				case MENOR_IGUAL:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_MENOR_IGUAL);
					break;

				case MAYOR_IGUAL:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_MAYOR_IGUAL);
					break;

				case IGUAL:

					switch (next_char) {

					case '=':
						transita(IGUAL_IGUAL);
						break;

					default:
						encontrado = true;
						encontrado(PalabrasReservadas.TOKEN_ASIGNACION);
					}

					break;

				case IGUAL_IGUAL:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_IGUAL);
					break;

				case NEGACION:

					switch (next_char) {

					case '=':
						transita(DISTINTO);
						break;

					default:
						encontrado = true;
						encontrado(PalabrasReservadas.TOKEN_NEGACION);
					}

					break;

				case DISTINTO:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_DISTINTO);
					break;

				case SUMA:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_SUMA);
					break;

				case RESTA:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_MENOR);
					break;

				case MULTIPLICACION:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_MULT);
					break;

				case DIVISION:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_DIV);
					break;

				case MODULO:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_MODULO);
					break;

				case BARRA:

					if (next_char == '|')
						transita(O_LOGICA);
					else {
						encontrado = true;
						encontrado(PalabrasReservadas.TOKEN_BARRA);
					}
					break;

				case PARENTESIS_ABIERTO:

					this.parentesis_indice = indice;
					this.parentesis_columna = columna;
					this.parentesis_fila = fila;

					switch (next_char) {
					case ' ':
						transitaSinLexema(PARENTESIS_ABIERTO);
						break;
					case '\t':
						transitaSinLexema(PARENTESIS_ABIERTO);
						break;
					case '\n':
						transitaSinLexema(PARENTESIS_ABIERTO);
						break;
					case 'i':
					case 'I':
						transita(CAST_I);
						break;
					case 'r':
					case 'R':
						transita(CAST_R);
						break;
					default:
						encontrado = true;
						encontrado(PalabrasReservadas.TOKEN_PARENTESIS_AP);
						break;
					}
					break;

				case PARENTESIS_CERRADO:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_PARENTESIS_CE);
					break;

				case ARROBA:

					if (next_char != '\n')
						transita(COMENTARIO_INC);
					else {
						encontrado = true;
						this.fila++;
						this.columna = 0;
						this.estado = VACIO;
					}
					break;

				case LIT_INT:

					if (sigDigito())
						transita(LIT_INT);
					else {
						switch (next_char) {
						case '.':
							transita(LIT_REAL);
							break;
						case 'e':
						case 'E':
							transita(LIT_REAL4);
							break;
						default:
							encontrado = true;
							encontrado(PalabrasReservadas.TOKEN_LIT_INT);
							break;
						}
					}
					break;

				case LIT_REAL:

					if (sigDigito()) {
						if (next_char == '0')
							transita(LIT_REAL1);
						else
							transita(LIT_REAL3);
					} else {
						GestorErrores.agregaError(6, fila, columna, next_char
								+ "");
						transitaSinLexema(VACIO);
						this.lexema = "";
					}
					break;

				case LIT_REAL1:

					if (sigDigito()) {
						if (next_char == '0')
							transita(LIT_REAL2);
						else
							transita(LIT_REAL3);

					} else {
						if (next_char == 'e' || next_char == 'E') {
							transita(LIT_REAL4);
						} else {
							encontrado = true;
							this.token_actual = PalabrasReservadas.TOKEN_LIT_REAL;
							encontrado(PalabrasReservadas.TOKEN_LIT_REAL);
						}
					}
					break;

				case LIT_REAL2:

					if (sigDigito())
						if (next_char == '0')
							transita(LIT_REAL2);
						else
							transita(LIT_REAL3);
					else {
						GestorErrores.agregaError(6, fila, columna, next_char
								+ "");
						transitaSinLexema(VACIO);
						this.lexema = "";
					}
					break;

				case LIT_REAL3:

					if (sigDigito()) {
						if (next_char == '0')
							transita(LIT_REAL2);
						else
							transita(LIT_REAL3);
					} else {
						if (next_char == 'e' || next_char == 'E')
							transita(LIT_REAL4);
						else {
							encontrado = true;
							encontrado(PalabrasReservadas.TOKEN_LIT_REAL);
						}
					}
					break;

				case LIT_REAL4:

					if (sigDigito()) {
						if (next_char == '0')
							transita(LIT_REAL5);
						else
							transita(LIT_REALF);
					} else {
						if (next_char == '-')
							transita(LIT_REAL6);
						else {
							GestorErrores.agregaError(7, fila, columna,
									next_char + "");
							transitaSinLexema(VACIO);
							this.lexema = "";
						}
					}
					break;

				case LIT_REAL5:

					if (sigDigito()) {
						GestorErrores.agregaError(10, fila, columna, next_char
								+ "");
						transitaSinLexema(VACIO);
						this.lexema = "";
					} else {
						encontrado = true;
						encontrado(PalabrasReservadas.TOKEN_LIT_REAL);
					}
					break;

				case LIT_REAL6:

					if (sigDigito())
						if (next_char == '0')
							transita(LIT_REAL7);
						else
							transita(LIT_REALF);
					else {
						GestorErrores.agregaError(6, fila, columna, next_char
								+ "");
						transitaSinLexema(VACIO);
						this.lexema = "";
					}
					break;

				case LIT_REAL7:

					if (sigDigito())
						if (next_char == '0')
							transita(LIT_REAL7);
						else
							transita(LIT_REALF);
					else {
						GestorErrores.agregaError(6, fila, columna, next_char
								+ "");
						transitaSinLexema(VACIO);
						this.lexema = "";
					}
					break;

				case LIT_REALF:

					if (sigDigito()) {
						if (next_char == '0')
							transita(LIT_REAL7);
						else
							transita(LIT_REALF);
					} else {
						encontrado = true;
						encontrado(PalabrasReservadas.TOKEN_LIT_REAL);
					}
					break;

				case COMENTARIO_INC:

					if (next_char == '\n') {
						transita(COMENTARIO);
					} else
						transita(COMENTARIO_INC);
					break;

				case COMENTARIO:

					this.estado = VACIO;
					this.lexema = "";
					this.fila++;
					this.columna = 0;
					break;

				case CADENA:

					if (sigDigito() || sigLetra())
						transita(CADENA);
					else {
						encontrado = true;
						this.token_actual = this.palabrasReserva
								.obtenerToken(lexema);
						encontrado(token_actual);
					}
					break;

				case CAST_I:

					if ((next_char == 'n') || (next_char == 'N'))
						transita(CAST_IN);
					else {
						encontrado = true;
						this.indice = this.parentesis_indice;
						this.fila = this.parentesis_fila;
						this.columna = this.parentesis_columna;
						this.token_actual = PalabrasReservadas.TOKEN_PARENTESIS_AP;
						this.tokens.add(new DatosToken(token_actual, fila,
								columna, indice));
						this.lexema = "(";
						this.estado = VACIO;
						this.next_char = programa.charAt(this.indice);
					}
					break;

				case CAST_IN:
					if ((next_char == 't') || (next_char == 'T'))
						transita(CAST_INT);
					else {
						encontrado = true;
						this.indice = this.parentesis_indice;
						this.fila = this.parentesis_fila;
						this.columna = this.parentesis_columna;
						this.token_actual = PalabrasReservadas.TOKEN_PARENTESIS_AP;
						encontrado(PalabrasReservadas.TOKEN_PARENTESIS_AP);
						this.next_char = programa.charAt(this.indice);
					}
					break;

				case CAST_INT:
					if (next_char == ')')
						transita(CAST_INT_FIN);
					else {
						encontrado = true;
						this.indice = this.parentesis_indice;
						this.fila = this.parentesis_fila;
						this.columna = this.parentesis_columna;
						this.token_actual = PalabrasReservadas.TOKEN_PARENTESIS_AP;
						this.tokens.add(new DatosToken(token_actual, fila,
								columna, indice));
						this.lexema = "(";
						this.estado = VACIO;
						this.next_char = programa.charAt(this.indice);
					}
					break;

				case CAST_INT_FIN:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_CAST_INT);
					break;

				case CAST_R:

					if ((next_char == 'e') || (next_char == 'E'))
						transita(CAST_RE);
					else {
						encontrado = true;
						this.indice = this.parentesis_indice;
						this.fila = this.parentesis_fila;
						this.columna = this.parentesis_columna;
						this.token_actual = PalabrasReservadas.TOKEN_PARENTESIS_AP;
						this.tokens.add(new DatosToken(token_actual, fila,
								columna, indice));
						this.lexema = "(";
						this.estado = VACIO;
						this.next_char = programa.charAt(this.indice);
					}
					break;

				case CAST_RE:

					if ((next_char == 'a') || (next_char == 'A'))
						transita(CAST_REA);
					else {
						encontrado = true;
						this.indice = this.parentesis_indice;
						this.fila = this.parentesis_fila;
						this.columna = this.parentesis_columna;
						this.token_actual = PalabrasReservadas.TOKEN_PARENTESIS_AP;
						this.tokens.add(new DatosToken(token_actual, fila,
								columna, indice));
						this.lexema = "(";
						this.estado = VACIO;
						this.next_char = programa.charAt(this.indice);
					}
					break;

				case CAST_REA:

					if ((next_char == 'l') || (next_char == 'L'))
						transita(CAST_REAL);
					else {
						encontrado = true;
						this.indice = this.parentesis_indice;
						this.fila = this.parentesis_fila;
						this.columna = this.parentesis_columna;
						this.token_actual = PalabrasReservadas.TOKEN_PARENTESIS_AP;
						this.tokens.add(new DatosToken(token_actual, fila,
								columna, indice));
						this.lexema = "(";
						this.estado = VACIO;
						this.next_char = programa.charAt(this.indice);
					}
					break;

				case CAST_REAL:

					if (next_char == ')')
						transita(CAST_REAL_FIN);
					else {
						encontrado = true;
						this.indice = this.parentesis_indice;
						this.fila = this.parentesis_fila;
						this.columna = this.parentesis_columna;
						this.token_actual = PalabrasReservadas.TOKEN_PARENTESIS_AP;
						this.tokens.add(new DatosToken(token_actual, fila,
								columna, indice));
						this.lexema = "(";
						this.estado = VACIO;
						this.next_char = programa.charAt(this.indice);
					}
					break;

				case CAST_REAL_FIN:

					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_CAST_REAL);
					break;

				case AMPERSAND:

					if (next_char == '&')
						transita(Y_LOGICA);
					else {
						GestorErrores.agregaError(3, fila, columna, next_char
								+ "");
						transitaSinLexema(VACIO);
					}
					break;

				case Y_LOGICA:
					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_Y_LOGICA);
					break;

				case O_LOGICA:
					encontrado = true;
					encontrado(PalabrasReservadas.TOKEN_O_LOGICA);
					break;

				default:
					GestorErrores.agregaError(2, fila, columna, next_char + "");
					transitaSinLexema(VACIO);
					break;
				}
			}
		}
	}

	/**
	 * @return Indica si el siguiente caracter se encentra dentro de la
	 *         categoría léxica "digito"
	 */
	public boolean sigDigito() {
		return (next_char >= '0' && next_char <= '9');
	}

	/**
	 * @return Indica si el siguiente caracter se encentra dentro de la
	 *         categoría léxica "letra"
	 */
	public boolean sigLetra() {
		return (next_char >= 'a' && next_char <= 'z')
				|| (next_char >= 'A' && next_char <= 'Z');
	}

	/**
	 * Cambia al siguiente estado, incrementando las variables oportunas
	 * 
	 * @param nuevo_estado
	 *            Estado de destino
	 */
	public void transita(int nuevo_estado) {
		this.estado = nuevo_estado;
		this.lexema = this.lexema + this.next_char;
		this.indice++;
		this.columna++;
		this.next_char = siguienteCaracter();
	}

	/**
	 * Cambia al siguiente estado, incrementando las variables oportunas sin
	 * cambiar el lexema actual
	 * 
	 * @param nuevo_estado
	 *            Estado de destino
	 */
	public void transitaSinLexema(int nuevo_estado) {
		this.estado = nuevo_estado;
		this.indice++;
		this.columna++;
		this.next_char = siguienteCaracter();
	}

	/**
	 * @return Siguiente caracter a procesar
	 */
	public char siguienteCaracter() {
		char next = ' ';

		if (indice <= programa.length() - 1)
			next = programa.charAt(indice);
		else
			fin_programa = true;

		return next;
	}

	/**
	 * Realiza las operaciones pertinentes cuando se ha encontrado un token
	 * válido
	 * 
	 * @param token
	 *            Nombre del token de entrada
	 */
	public void encontrado(String token) {
		this.token_actual = token;
		this.tokens.add(new DatosToken(token_actual, fila, columna, indice));
		this.estado = VACIO;
//		this.lexema = "";
	}

}
