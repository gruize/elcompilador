package analizadorLexico;

import java.util.Vector;

public class AnalizadorLexico {

	private static final int VACIO = 0;
	//et es &
	private static final int ET = 1;
	private static final int PUNTOYCOMA = 2;
	private static final int DOSPUNTOS = 3;
	private static final int PUNTO = 4;
	private static final int DOSPUNTOSIGUAL = 5;
	private static final int MENOR = 6;
	private static final int MAYOR = 7;
	private static final int MENORIGUAL = 8;
	private static final int MAYORIGUAL = 9;
	private static final int IGUAL = 10;
	// =/ es igualbarra
	private static final int IGUALBARRA = 11;
	private static final int DISTINTO = 12;
	private static final int SUMA = 13;
	private static final int RESTA = 14;
	private static final int MULTIPLICACION = 15;
	private static final int DIVISION = 16;
	private static final int MODULO = 17;
	// << es desp_izq
	private static final int DESP_IZQ = 18;
	private static final int DESP_DER = 19;
	private static final int ABSOLUTO = 20;
	//parentesis1 es la apertura del parentesis
	private static final int PARENTESIS1 = 21;
	//parentesis2 es el cierre del parentesis
	private static final int PARENTESIS2 = 22;
	private static final int NUMERAL = 23;
	private static final int BARRAINCLINADA = 24;
	private static final int LITNAT = 25;
	private static final int LITNATINT = 26;
	private static final int LITFLOAT = 27;
	private static final int LITFLOAT1 = 28;
	private static final int LITFLOAT2 = 29;
	private static final int LITFLOAT3 = 30;
	private static final int LITFLOAT4 = 31;
	private static final int LITFLOAT5 = 32;
	private static final int LITFLOAT6 = 33;
	private static final int LITFLOAT7 = 34;
	private static final int LITFLOATF = 35;
	//comillasimple es '
	private static final int COMILLASIMPLE = 36;
	private static final int LITCHAR = 37;
	private static final int LITCHARF = 38;
	private static final int IDENTIFICADOR = 39;
	private static final int COMENT = 40;
	private static final int COMENTARIO = 41;
	private static final int CADENA = 42;
	private static final int CASTI = 43;
	private static final int CASTIN = 44;
	private static final int CASTINT = 45;
	private static final int CAST_INT = 46;
	private static final int CASTN = 47;
	private static final int CASTNA = 48;
	private static final int CASTNAT = 49;
	private static final int CAST_NAT = 50;
	private static final int CASTC = 51;
	private static final int CASTCH = 52;
	private static final int CASTCHA = 53;
	private static final int CASTCHAR = 54;
	private static final int CAST_CHAR = 55;
	private static final int CASTF = 56;
	private static final int CASTFL = 57;
	private static final int CASTFLO = 58;
	private static final int CASTFLOA = 59;
	private static final int CASTFLOAT = 60;
	private static final int CAST_FLOAT = 61;
	private static final int LLAVE1 = 62;
	private static final int LLAVE2 = 63;
	private static final int CORCHETE1 = 64;
	private static final int CORCHETE2 = 65;
	private static final int FLECHA = 66;
	
	private PalabrasReservadas palabrasReserva = new PalabrasReservadas();
	private String lexema; 
	private int estado;
	private boolean error_lexico;
	private String programa;
	private boolean fin_programa;
	private int fila;
	private int columna;
	private char next_char;
	private Vector<String> tokens;
	private int indice;
	
	public AnalizadorLexico(String program){
		this.error_lexico = false;
		this.programa = program;
		this.fin_programa = false;
		this.fila = 0;
		this.columna = 0;
		this.tokens = new Vector<String>();
		this.indice = 0;
	}

	public PalabrasReservadas getPalabrasReserva() {
		return palabrasReserva;
	}

	public void setPalabrasReserva(PalabrasReservadas palabrasReserva) {
		this.palabrasReserva = palabrasReserva;
	}

	public String getLexema() {
		return lexema;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public boolean isError_lexico() {
		return error_lexico;
	}

	public void setError_lexico(boolean error_lexico) {
		this.error_lexico = error_lexico;
	}

	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

	public boolean isFin_programa() {
		return fin_programa;
	}

	public void setFin_programa(boolean fin_programa) {
		this.fin_programa = fin_programa;
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

	public char getNext_char() {
		return next_char;
	}

	public void setNext_char(char next_char) {
		this.next_char = next_char;
	}

	public Vector<String> getTokens() {
		return tokens;
	}

	public void setTokens(Vector<String> tokens) {
		this.tokens = tokens;
	}	
	
	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public void inicializarScanner(){
		this.lexema = "";
		this.estado = 0;
		this.next_char = siguienteCaracter();
	}
	
	//Indica si el siguiente caracter es un digito
	public boolean sigDigito(){
		boolean digito = false;
		if(next_char >= '0' && next_char <= '9')
			digito = true;
		return digito;
	}
	
	//Indica si el siguiente caracter es una letra
	public boolean sigLetra(){
		boolean letra = false;
		if((next_char >= 'a' && next_char <= 'z') || (next_char >= 'A' && next_char <= 'Z'))
			letra = true;
		return letra;
	}
	
	public char siguienteCaracter(){
		char next = ' ';
		if(indice <= programa.length() - 1)
			next = programa.charAt(indice);
		else
			fin_programa = true;
		return next;
	}
	
	public void estado(int nuevo_estado){
		this.estado = nuevo_estado;
		
		this.next_char = siguienteCaracter();
		indice++;
	}
	
	public void scanner(){
		inicializarScanner();
		boolean encontrado = false;
		while(!encotrado){
			switch(estado){
			case 0:	
				if(n)
			}
		}
	}
}
