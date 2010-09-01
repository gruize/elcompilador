package analizadorLexico;

import java.util.Vector;

import errores.GestorErrores;

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
	private static final int COMA = 24;
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
	//comillasimple es ', que se usa como \'
	private static final int COMILLASIMPLE = 36;
	private static final int LITCHAR = 37;
	private static final int LITCHARF = 38;
	private static final int COMENT = 39;
	private static final int COMENTARIO = 40;
	private static final int CADENA = 41;
	private static final int CASTI = 42;
	private static final int CASTIN = 43;
	private static final int CASTINT = 44;
	private static final int CAST_INT = 45;
	private static final int CASTN = 46;
	private static final int CASTNA = 47;
	private static final int CASTNAT = 48;
	private static final int CAST_NAT = 49;
	private static final int CASTC = 50;
	private static final int CASTCH = 51;
	private static final int CASTCHA = 52;
	private static final int CASTCHAR = 53;
	private static final int CAST_CHAR = 54;
	private static final int CASTF = 55;
	private static final int CASTFL = 56;
	private static final int CASTFLO = 57;
	private static final int CASTFLOA = 58;
	private static final int CASTFLOAT = 59;
	private static final int CAST_FLOAT = 60;
	private static final int LLAVE1 = 61;
	private static final int LLAVE2 = 62;
	private static final int CORCHETE1 = 63;
	private static final int CORCHETE2 = 64;
	private static final int FLECHA = 65;
	
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
		
	
	public AnalizadorLexico(String program){
		this.programa = program;
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
	
	public String getToken_actual() {
		return token_actual;
	}

	public void setToken_actual(String token_actual) {
		this.token_actual = token_actual;
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

	public Vector<DatosToken> getTokens() {
		return tokens;
	}

	public void setTokens(Vector<DatosToken> tokens) {
		this.tokens = tokens;
	}	
	
	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
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
	
	// Lee el siguiente caracter del programa
	public char siguienteCaracter(){
		char next = ' ';
		if(indice <= programa.length() - 1){
			next = programa.charAt(indice);
		}else{
			fin_programa = true;
		}
		return next;
	}
	
	public void estado(int nuevo_estado){
		this.estado = nuevo_estado;
		this.indice++;
		this.next_char = siguienteCaracter();
		this.columna++;
	}
	
	public String ultimoToken(){
		String last_token = null;
		if(!this.tokens.isEmpty())
			last_token = this.tokens.elementAt(this.tokens.size() - 1).getToken();
		return last_token;
	}
	
	public void transita(int nuevo_estado){
		this.estado = nuevo_estado;	
		this.lexema = this.lexema + this.next_char;
		this.indice++;
		this.columna++;
		this.next_char = siguienteCaracter();
	}
	
	public void transitaSinLexema(int nuevo_estado){
		this.estado = nuevo_estado;	
		this.indice++;
		this.columna++;
		this.next_char = siguienteCaracter();
	}
	
	public void scanner(){
		this.estado = VACIO;
		this.lexema = "";
		boolean encontrado = false;
		while(!encontrado && !fin_programa){
			if(programa.length() == indice + 1)
				fin_programa = true;
			switch(estado){
			case VACIO:	
				if(sigDigito()){
					if(next_char == '0')
						transita(LITNATINT);
					else
						transita(LITNAT);
				}else{
					if(sigLetra()){
						transita(CADENA);
					}else{
						switch(next_char){
						case '\n':	transitaSinLexema(VACIO);
									this.fila++;
									this.columna = 0;
									break;
						case ' ':	transitaSinLexema(VACIO);
									break;
						case '\t':	transitaSinLexema(VACIO);
									break;						
						case '&':	transita(ET);
									break;
						case ';':	transita(PUNTOYCOMA);
									break;
						case ':':	transita(DOSPUNTOS);
									break;
						case '.':	transita(PUNTO);
									break;
						case '<':	transita(MENOR);
									break;
						case '>':	transita(MAYOR);
									break;
						case '=':	transita(IGUAL);
									break;
						case '+':	transita(SUMA);
									break;
						case '-':	transita(RESTA);
									break;
						case '*':	transita(MULTIPLICACION);
									break;
						case '/':	transita(DIVISION);
									break;
						case '%':	transita(MODULO);
									break;
						case '|':	transita(ABSOLUTO);
									break;
						case '(':	transita(PARENTESIS1);
									break;
						case ')':	transita(PARENTESIS2);
									break;
						case '#':	transita(NUMERAL);
									break;
						case ',':	transita(COMA);
									break;
						case '\'':	transita(COMILLASIMPLE);
									break;
						case '{':	transita(LLAVE1);
									break;
						case '}':	transita(LLAVE2);
									break;
						case '[':	transita(CORCHETE1);
									break;
						case ']':	transita(CORCHETE2);
									break;
						default:	GestorErrores.agregaError(2,getFila(),getColumna(),getNext_char() + "");
									transitaSinLexema(VACIO);
									break;
						}
					}
				}
				break;
			case ET:
				encontrado = true;
				this.token_actual = "tk&";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			case PUNTOYCOMA:
				encontrado = true;
				this.token_actual = "tk;";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
	//			this.lexema = "";
				this.estado = VACIO;
				break;
			case DOSPUNTOS:
				if(next_char == '=')
					transita(DOSPUNTOSIGUAL);
				else{
					encontrado = true;
					this.token_actual = "tk:";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
		//			this.lexema = "";
					this.estado = VACIO;
				}
				break;
			case PUNTO:
				encontrado = true;
				this.token_actual = "tk.";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
			//	this.lexema = "";
				this.estado = VACIO;
				break;
			case DOSPUNTOSIGUAL:
				encontrado = true;
				this.token_actual = "tk:=";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
				this.estado = VACIO;
				break;
			case MENOR:
				switch(next_char){
				case '=':	transita(MENORIGUAL);
							break;
				case '<':	transita(DESP_IZQ);
							break;
				default:	encontrado = true;
							this.token_actual = "tk<";
							this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
							this.estado = VACIO;
							break;
				}
				break;
			case MAYOR:
				switch(next_char){
				case '=':	transita(MAYORIGUAL);
							break;
				case '>':	transita(DESP_DER);
							break;
				default:	encontrado = true;
							this.token_actual = "tk>";
							this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
							this.estado = VACIO;
							break;
				}
				break;
			case MENORIGUAL:
				encontrado = true;
				this.token_actual = "tk<=";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
				this.estado = VACIO;
				break;
			case MAYORIGUAL:
				encontrado = true;
				this.token_actual = "tk>=";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
			//	this.lexema = "";
				this.estado = VACIO;
				break;
			case IGUAL:
				if(next_char == '/')
					transita(IGUALBARRA);
				else{
					encontrado = true;
					this.token_actual = "tk=";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
				//	this.lexema = "";
					this.estado = VACIO;
				}
				break;
			case IGUALBARRA:
				if(next_char == '=')
					transita(DISTINTO);
				else{
					GestorErrores.agregaError(3, getFila(), getColumna(), getNext_char() + "");
					transitaSinLexema(VACIO);
					this.lexema = "";
				}
				break;
			case DISTINTO:
				encontrado = true;
				this.token_actual = "tk=/=";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
			//	this.lexema = "";
				this.estado = VACIO;
				break;
			case SUMA:
				encontrado = true;
				this.token_actual = "tk+";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
				this.estado = VACIO;
				break;
			case RESTA:
				if(next_char == '>')
					transita(FLECHA);
				else{
					encontrado = true;
					this.token_actual = "tk-";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.estado = VACIO;
				}
				break;
			case MULTIPLICACION:
				encontrado = true;
				this.token_actual = "tk*";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
				this.estado = VACIO;
				break;
			case DIVISION:
				encontrado = true;
				this.token_actual = "tk/";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
				this.estado = VACIO;
				break;
			case MODULO:
				encontrado = true;
				this.token_actual = "tk%";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
				this.estado = VACIO;
				break;
			case DESP_IZQ:
				encontrado = true;
				this.token_actual = "tk<<";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
				this.estado = VACIO;
				break;
			case DESP_DER:
				encontrado = true;
				this.token_actual = "tk>>";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
				this.estado = VACIO;
				break;
			case ABSOLUTO:
				encontrado = true;
				this.token_actual = "tk|";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			case PARENTESIS1:
				this.parentesis_indice = getIndice();
				this.parentesis_columna = getColumna();
				this.parentesis_fila = getFila();
				switch(next_char){
				case ' ':	transitaSinLexema(PARENTESIS1);
							break;
				case '\t':	transitaSinLexema(PARENTESIS1);
							break;
				case '\n':	transitaSinLexema(PARENTESIS1);
							break;
				case 'i':	transita(CASTI);
							break;
				case 'n':	transita(CASTN);
							break;
				case 'c':	transita(CASTC);
							break;
				case 'f':	transita(CASTF);
							break;
				default:	encontrado = true;
							this.token_actual = "tk(";
							this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//							this.lexema = "";
							this.estado = VACIO;
							break;
				}
				break;
			case PARENTESIS2:
				encontrado = true;
				this.token_actual = "tk)";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
	//			this.lexema = "";
				this.estado = VACIO;
				break;
			case NUMERAL:
				if(next_char != '\n')	
					transita(COMENT);
				else{
					encontrado = true;
		//			this.lexema = "";
					this.fila++;
					this.columna = 0;
					this.estado = VACIO;
				}
				break;
			case COMA:
				encontrado = true;
				this.token_actual = "tk,";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			case LITNAT:	
				if(sigDigito())
					transita(LITNAT);
				else{
					switch(next_char){
					case '.':	transita(LITFLOAT);
								break;
					case 'e':	transita(LITFLOAT4);
								break;
					case 'E':	transita(LITFLOAT4);
								break;
					default:	encontrado = true;
								this.token_actual = "tknat";
								this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
	//							this.lexema = "";
								this.estado = VACIO;
								break;
					}
				}
				break;
			case LITNATINT:	
				switch(next_char){
				case '.':	transita(LITFLOAT);
							break;
				case 'e':	transita(LITFLOAT4);
							break;
				case 'E':	transita(LITFLOAT4);
							break;
				default:	encontrado = true;
							this.token_actual = "tknatint";
							this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//							this.lexema = "";
							this.estado = VACIO;
							break;
				}
				break;
			case LITFLOAT:
				if(sigDigito()){
					if(next_char == '0')
						transita(LITFLOAT1);
					else
					// [1..9]
						transita(LITFLOAT3);	
				}else{
					GestorErrores.agregaError(6, getFila(), getColumna(), getNext_char() + "");
					transitaSinLexema(VACIO);
					this.lexema = "";
				}
				break;
			case LITFLOAT1:
				if(sigDigito()){
					if(next_char == '0'){
						transita(LITFLOAT2);
					}else{
						transita(LITFLOAT3);
					}
				}else{
					if(next_char == 'e' || next_char == 'E'){
						transita(LITFLOAT4);
					}else{
						encontrado = true;
						this.token_actual = "tkreal";
						this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
	//					this.lexema = "";
						this.estado = VACIO;
					}
				}
				break;
			case LITFLOAT2:	
				if(sigDigito())
					if(next_char == '0')
						transita(LITFLOAT2);
					else
						transita(LITFLOAT3);
				else{
					GestorErrores.agregaError(6, getFila(), getColumna(), getNext_char() + "");
					transitaSinLexema(VACIO);
					this.lexema="";
				}
				break;					
			case LITFLOAT3:
				if(sigDigito()){
					if(next_char == '0')
						transita(LITFLOAT2);
					else
						transita(LITFLOAT3);
				}else{
					if(next_char == 'e' || next_char == 'E')
						transita(LITFLOAT4);
					else{
						encontrado = true;
						this.token_actual = "tkreal";
						this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
		//				this.lexema = "";
						this.estado = VACIO;
					}
				}
				break;
			case LITFLOAT4:
				if(sigDigito()){
					if(next_char == '0')
						transita(LITFLOAT5);
					else
						transita(LITFLOATF);
				}else{
					if(next_char == '-')
						transita(LITFLOAT6);
					else{
						GestorErrores.agregaError(7, getFila(), getColumna(), getNext_char() + "");
						transitaSinLexema(VACIO);
						this.lexema = "";
					}
				}
				break;
			case LITFLOAT5:
				if(sigDigito()){
					GestorErrores.agregaError(10, getFila(), getColumna(), getNext_char() + "");
					transitaSinLexema(VACIO);
					this.lexema = "";
				}else{
					encontrado = true;
					this.token_actual = "tkreal";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//					this.lexema = "";
					this.estado = VACIO;
				}
				break;
			case LITFLOAT6:
				if(sigDigito())
					if(next_char == '0')
						transita(LITFLOAT7);
					else
						transita(LITFLOATF);
				else{
					GestorErrores.agregaError(6, getFila(), getColumna(), getNext_char() + "");
					transitaSinLexema(VACIO);
					this.lexema = "";
				}
				break;
			case LITFLOAT7:
				if(sigDigito())
					if(next_char == '0')
						transita(LITFLOAT7);
					else
						transita(LITFLOATF);
				else{
					GestorErrores.agregaError(6, getFila(), getColumna(), getNext_char() + "");
					transitaSinLexema(VACIO);
					this.lexema = "";
				}
				break;
			case LITFLOATF:
				if(sigDigito()){
					if(next_char == '0')
						transita(LITFLOAT7);
					else
						transita(LITFLOATF);
				}else{
					encontrado = true;
					this.token_actual = "tkreal";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//					this.lexema = "";
					this.estado = VACIO;
				}
				break;
			case COMILLASIMPLE:
				if(sigDigito() || sigLetra())
					transita(LITCHAR);
				else{
					GestorErrores.agregaError(8, getFila(), getColumna(), getNext_char() + "");
					transitaSinLexema(VACIO);
					this.lexema = "";
				}
				break;
			case LITCHAR:
				if(next_char == '\'')
					transita(LITCHARF);
				else{
					GestorErrores.agregaError(9, getFila(), getColumna(), getNext_char() + "");
					transitaSinLexema(VACIO);
					this.lexema = "";
				}
				break;
			case LITCHARF:
				encontrado = true;
				this.token_actual = "tkchar";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			case COMENT:
				if(next_char == '\n'){
					transita(COMENTARIO);
				}else
					transita(COMENT);
				break;
			case COMENTARIO:
				this.estado = VACIO;
				this.lexema = "";
				this.fila++;
				this.columna = 0;				
				break;
			case CADENA:
				if(sigDigito() || sigLetra())
					transita(CADENA);
				else{
					encontrado = true;
					this.token_actual = this.palabrasReserva.obtenerToken(getLexema());
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
	//				this.lexema = "";
					this.estado = VACIO;
				}
				break;
			case CASTI:
				if(next_char == 'n')
					transita(CASTIN);
				else{
					encontrado = true;
					this.indice = this.parentesis_indice;
					this.fila = this.parentesis_fila;
					this.columna = this.parentesis_columna;
					this.token_actual = "tk(";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.lexema = "(";
					this.estado = VACIO;
					this.next_char = programa.charAt(this.indice);
				}
				break;
			case CASTIN:
				if(next_char == 't')
					transita(CASTINT);
				else{
					encontrado = true;
					this.indice = this.parentesis_indice;
					this.fila = this.parentesis_fila;
					this.columna = this.parentesis_columna;
					this.token_actual = "tk(";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.lexema = "(";
					this.estado = VACIO;
					this.next_char = programa.charAt(this.indice);
				}
				break;
			case CASTINT:
				switch(next_char){
				//TODO: No estoy muy segura de la validez del espaciado
				case ' ':	transitaSinLexema(CASTINT);
							break;
				case '\n':	transitaSinLexema(CASTINT);
							break;
				case '\t':	transitaSinLexema(CASTINT);
							break;
				case ')':	transita(CAST_INT);
							break;
				default:	encontrado = true;
							this.indice = this.parentesis_indice;
							this.fila = this.parentesis_fila;
							this.columna = this.parentesis_columna;
							this.token_actual = "tk(";
							this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
							this.lexema = "(";
							this.estado = VACIO;
							this.next_char = programa.charAt(this.indice);
							break;
				}
				break;
			case CAST_INT:
				encontrado = true;
				this.token_actual = "tkcastint";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			case CASTN:
				if(next_char == 'a')
					transita(CASTNA);
				else{
					encontrado = true;
					this.indice = this.parentesis_indice;
					this.fila = this.parentesis_fila;
					this.columna = this.parentesis_columna;
					this.token_actual = "tk(";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.lexema = "(";
					this.estado = VACIO;
					this.next_char = programa.charAt(this.indice);
				}
				break;
			case CASTNA:
				if(next_char == 't')
					transita(CASTNAT);
				else{
					encontrado = true;
					this.indice = this.parentesis_indice;
					this.fila = this.parentesis_fila;
					this.columna = this.parentesis_columna;
					this.token_actual = "tk(";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.lexema = "(";
					this.estado = VACIO;
					this.next_char = programa.charAt(this.indice);
				}
				break;
			case CASTNAT:
				switch(next_char){
				//TODO: No estoy muy segura de la validez del espaciado
				case ' ':	transitaSinLexema(CASTNAT);
							break;
				case '\n':	transitaSinLexema(CASTNAT);
							break;
				case '\t':	transitaSinLexema(CASTNAT);
							break;
				case ')':	transita(CAST_NAT);
							break;
				default:	encontrado = true;
							this.indice = this.parentesis_indice;
							this.fila = this.parentesis_fila;
							this.columna = this.parentesis_columna;
							this.token_actual = "tk(";
							this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
							this.lexema = "(";
							this.estado = VACIO;
							this.next_char = programa.charAt(this.indice);
							break;
				}
				break;
			case CAST_NAT:
				encontrado = true;
				this.token_actual = "tkcastnat";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			case CASTC:
				if(next_char == 'h')
					transita(CASTCH);
				else{
					encontrado = true;
					this.indice = this.parentesis_indice;
					this.fila = this.parentesis_fila;
					this.columna = this.parentesis_columna;
					this.token_actual = "tk(";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.lexema = "(";
					this.estado = VACIO;
					this.next_char = programa.charAt(this.indice);
				}
				break;
			case CASTCH:
				if(next_char == 'a')
					transita(CASTCHA);
				else{
					encontrado = true;
					this.indice = this.parentesis_indice;
					this.fila = this.parentesis_fila;
					this.columna = this.parentesis_columna;
					this.token_actual = "tk(";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.lexema = "(";
					this.estado = VACIO;
					this.next_char = programa.charAt(this.indice);
				}
				break;
			case CASTCHA:
				if(next_char == 'r')
					transita(CASTCHAR);
				else{
					encontrado = true;
					this.indice = this.parentesis_indice;
					this.fila = this.parentesis_fila;
					this.columna = this.parentesis_columna;
					this.token_actual = "tk(";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.lexema = "(";
					this.estado = VACIO;
					this.next_char = programa.charAt(this.indice);
				}
				break;
			case CASTCHAR:
				switch(next_char){
				//TODO: No estoy muy segura de la validez del espaciado
				case ' ':	transitaSinLexema(CASTCHAR);
							break;
				case '\n':	transitaSinLexema(CASTCHAR);
							break;
				case '\t':	transitaSinLexema(CASTCHAR);
							break;
				case ')':	transita(CAST_CHAR);
							break;
				default:	encontrado = true;
							this.indice = this.parentesis_indice;
							this.fila = this.parentesis_fila;
							this.columna = this.parentesis_columna;
							this.token_actual = "tk(";
							this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
							this.lexema = "(";
							this.estado = VACIO;
							this.next_char = programa.charAt(this.indice);
							break;
				}
				break;
			case CAST_CHAR:
				encontrado = true;
				this.token_actual = "tkcastchar";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
	//			this.lexema = "";
				this.estado = VACIO;
				break;
			case CASTF:
				if(next_char == 'l')
					transita(CASTFL);
				else{
					encontrado = true;
					this.indice = this.parentesis_indice;
					this.fila = this.parentesis_fila;
					this.columna = this.parentesis_columna;
					this.token_actual = "tk(";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.lexema = "(";
					this.estado = VACIO;
					this.next_char = programa.charAt(this.indice);
				}
				break;
			case CASTFL:
				if(next_char == 'o')
					transita(CASTFLO);
				else{
					encontrado = true;
					this.indice = this.parentesis_indice;
					this.fila = this.parentesis_fila;
					this.columna = this.parentesis_columna;
					this.token_actual = "tk(";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.lexema = "(";
					this.estado = VACIO;
					this.next_char = programa.charAt(this.indice);
				}
				break;
			case CASTFLO:
				if(next_char == 'a')
					transita(CASTFLOA);
				else{
					encontrado = true;
					this.indice = this.parentesis_indice;
					this.fila = this.parentesis_fila;
					this.columna = this.parentesis_columna;
					this.token_actual = "tk(";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.lexema = "(";
					this.estado = VACIO;
					this.next_char = programa.charAt(this.indice);
				}
				break;
			case CASTFLOA:
				if(next_char == 't')
					transita(CASTFLOAT);
				else{
					encontrado = true;
					this.indice = this.parentesis_indice;
					this.fila = this.parentesis_fila;
					this.columna = this.parentesis_columna;
					this.token_actual = "tk(";
					this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
					this.lexema = "(";
					this.estado = VACIO;
					this.next_char = programa.charAt(this.indice);
				}
				break;
			case CASTFLOAT:
				switch(next_char){
				//TODO: No estoy muy segura de que el espaciado sea valido
				case ' ':	transitaSinLexema(CASTFLOAT);
							break;
				case '\n':	transitaSinLexema(CASTFLOAT);
							break;
				case '\t':	transitaSinLexema(CASTFLOAT);
							break;
				case ')':	transita(CAST_FLOAT);
							break;
				default:	encontrado = true;
							this.indice = this.parentesis_indice;
							this.fila = this.parentesis_fila;
							this.columna = this.parentesis_columna;
							this.token_actual = "tk(";
							this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
							this.lexema = "(";
							this.estado = VACIO;
							this.next_char = programa.charAt(this.indice);
							break;
				}
				break;
			case CAST_FLOAT:
				encontrado = true;
				this.token_actual = "tkcastfloat";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			case LLAVE1:
				encontrado = true;
				this.token_actual = "tk{";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			case LLAVE2:
				encontrado = true;
				this.token_actual = "tk}";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			case CORCHETE1:
				encontrado = true;
				this.token_actual = "tk[";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			case CORCHETE2:
				encontrado = true;
				this.token_actual = "tk]";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			case FLECHA:
				encontrado = true;
				this.token_actual = "tk->";
				this.tokens.add(new DatosToken(getToken_actual(), getIndice()));
//				this.lexema = "";
				this.estado = VACIO;
				break;
			default:
				GestorErrores.agregaError(2,getFila(),getColumna(),getNext_char() + "");
				transitaSinLexema(VACIO);
				break;
			}
		}
	}
}
