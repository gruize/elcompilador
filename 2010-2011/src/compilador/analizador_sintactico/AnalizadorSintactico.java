package compilador.analizador_sintactico;

import interprete.InstruccionInterprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;
import interprete.instrucciones.CambioSigno;
import interprete.instrucciones.CastInt;
import interprete.instrucciones.CastReal;
import interprete.instrucciones.DesapilarDir;
import interprete.instrucciones.Distinto;
import interprete.instrucciones.Dividir;
import interprete.instrucciones.Entrada;
import interprete.instrucciones.Igual;
import interprete.instrucciones.Mayor;
import interprete.instrucciones.MayorIg;
import interprete.instrucciones.Menor;
import interprete.instrucciones.MenorIg;
import interprete.instrucciones.Modulo;
import interprete.instrucciones.Multiplicar;
import interprete.instrucciones.Negacion;
import interprete.instrucciones.O_Logica;
import interprete.instrucciones.Restar;
import interprete.instrucciones.Salida;
import interprete.instrucciones.Sumar;
import interprete.instrucciones.Y_Logica;

import java.util.Vector;

import compilador.analizador_lexico.AnalizadorLexico;
import compilador.analizador_lexico.PalabrasReservadas;
import compilador.tablaSimbolos.GestorTS;

import errores.GestorErrores;

public class AnalizadorSintactico {

	private final String TIPO_INT = "<t:int>";
	private final String TIPO_REAL = "<t:real>";

	private AnalizadorLexico a_lexico;
	private Vector<InstruccionInterprete> codigo;
	private int dir;
	private boolean error;
//	private boolean finDecs;

	public AnalizadorSintactico(AnalizadorLexico lexico) {
		this.a_lexico = lexico;
	}

	public AnalizadorLexico getA_lexico() {
		return a_lexico;
	}

	public void setA_lexico(AnalizadorLexico a_lexico) {
		this.a_lexico = a_lexico;
	}

	public Vector<InstruccionInterprete> getCodigo() {
		return codigo;
	}

	public void setCodigo(Vector<InstruccionInterprete> codigo) {
		this.codigo = codigo;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public void iniciaSintactico() throws Exception {
		// TODO
		programa();
	}

	public void programa() throws Exception {
		this.error = false;
		this.dir = 0;
		declaraciones();
		this.codigo = new Vector<InstruccionInterprete>();
		acciones();
		// TODO: Añadir al codigo la instruccion STOP
	}

	public void declaraciones() {
		// TODO: Reconocer si es un token tkint o tkreal o no
		declaracion();
		declaracionesRE();
		// En caso de no ser ninguno de los 2 pasa a acciones
	}

	public void declaracionesRE() {
		// TODO: Reconocer si es un token tkint o tkreal o no
		declaracion();
		declaracionesRE();
		// En caso de no ser ninguno de los 2 pasa a acciones
	}

	public void declaracion() {
		// TODO: Reconocer el token (tkint o tkreal)
		desctipo();
		reconoce(PalabrasReservadas.TOKEN_ID);
		reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA);

	}

	private void desctipo() {
		reconoce(PalabrasReservadas.TOKEN_INT);

		reconoce(PalabrasReservadas.TOKEN_REAL);
	}

	public void acciones() throws Exception {
		accion();
		accionesRE();
	}

	private void accionesRE() throws Exception {
		accion();
		accionesRE();
	}

	private void accion() throws Exception {
		expresion();
		reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA);
	}

	private String expresion() throws Exception {
		
		if (reconoce(PalabrasReservadas.TOKEN_IN)) {
			//FIXME Esto seguro que está mal
			String lex = a_lexico.getLexema();
			error = error || !GestorTS.getInstancia().existeID(lex);
			codigo.add(new Entrada());
			codigo.add(new DesapilarDir(new DatoPila(DatoPila.INT, GestorTS.getInstancia().getDir(lex))));
			return null;
		}
		else if (reconoce(PalabrasReservadas.TOKEN_OUT)) {
			String tipo = expresion1();
			codigo.add(new Salida());
			return tipo;
		}
		else
			return expresion1();
	}

	private String expresion1() throws Exception {
		
		String tipo1 = expresion2();
		InstruccionInterprete op;

		if ((op = op1()) != null) {
			String tipo2 = expresion1();
			error = error || (!tipo2.equals(TIPO_INT) && tipo1.equals(TIPO_INT));
			codigo.add(op);
			if (tipo1.equals(TIPO_INT)) {
				codigo.add(new CastReal());
				//FIXME al desapiladir hay que pasarle la direccion de la TS
				codigo.add(new DesapilarDir());
			}
		}

		return tipo1;
	}

	private String expresion2() throws Exception {

		String tipo = expresion3();
		InstruccionInterprete op;

		if ((op = op2()) != null) {
			tipo = TIPO_INT;
			codigo.add(op);
		}
		
		return tipo;
	}

	private String expresion3() throws Exception {
		String tipo = expresion4();
		return expresion3RE(tipo);
	}

	private String expresion3RE(String tipo1) throws Exception {

		InstruccionInterprete op;

		if ((op = op3()) != null) {
			String tipo2 = expresion4();
			String tipo3 = expresion3RE(tipo2);
			error = error
					|| (op instanceof O_Logica && (!tipo2.equals(TIPO_INT) || !tipo1
							.equals(TIPO_INT)));
			codigo.add(op);
			if (op instanceof O_Logica)
				return TIPO_INT;
			else if (tipo1.equals(TIPO_INT) && tipo3.equals(TIPO_INT))
				return TIPO_INT;
			else
				return TIPO_REAL;

		} else
			return tipo1;
	}

	private String expresion4() throws Exception {
		String tipo = expresion5();
		return expresion4RE(tipo);
	}

	private String expresion4RE(String tipo1) throws Exception {

		InstruccionInterprete op;

		if ((op = op4()) != null) {
			String tipo2 = expresion5();
			String tipo3 = expresion4RE(tipo2);
			error = error || !tipo2.equals(TIPO_INT) || !tipo1.equals(TIPO_INT);
			codigo.add(op);
			if (op instanceof Y_Logica || op instanceof Modulo)
				return TIPO_INT;
			else if (tipo1.equals(TIPO_INT) && tipo3.equals(TIPO_INT)) {
				return TIPO_INT;
			} else
				return TIPO_REAL;
		} else
			return tipo1;
	}

	private String expresion5() throws Exception {

		InstruccionInterprete op;

		if ((op = op5asoc()) != null) {
			String tipo = expresion5();
			error = error
					|| (op instanceof Negacion ? tipo.equals(TIPO_INT) : false);
			codigo.add(op);
			return (op instanceof Negacion ? TIPO_INT : tipo);
		} else if ((op = op5noAsoc()) != null) {
			codigo.add(op);
			return (op instanceof CastInt ? TIPO_INT : TIPO_REAL);
		} else {
			String tipo = expresion6();
			if (tipo == null)
				error = true;
			return tipo;
		}
	}

	private String expresion6() throws Exception {

		String tipo = null;

		if (reconoce(PalabrasReservadas.TOKEN_PARENTESIS_AP)) {
			tipo = expresion();
			error = error || reconoce(PalabrasReservadas.TOKEN_PARENTESIS_CE);
		} else if (reconoce(PalabrasReservadas.TOKEN_LIT_INT)) {
			tipo = TIPO_INT;
			error = error || cast(a_lexico.getToken_actual(), tipo);
		} else if (reconoce(PalabrasReservadas.TOKEN_REAL)) {
			tipo = TIPO_REAL;
			error = error || cast(a_lexico.getToken_actual(), tipo);
		} else {
			// TODO Error
			error = true;
		}

		return tipo;

	}

	private InstruccionInterprete op1() {
		if (reconoce(PalabrasReservadas.TOKEN_ASIGNACION)) {
		}
		// TODO Asignacion: obtener la dirección, apilar el valor y desapiladir
		// a la dirección.
		else {
			// TODO Error
			error = true;
			return null;
		}
		return null;
	}

	private InstruccionInterprete op2() throws InterpreteException {
		if (reconoce(PalabrasReservadas.TOKEN_MENOR))
			return new Menor();
		else if (reconoce(PalabrasReservadas.TOKEN_MAYOR))
			return new Mayor();
		else if (reconoce(PalabrasReservadas.TOKEN_MENOR_IGUAL))
			return new MenorIg();
		else if (reconoce(PalabrasReservadas.TOKEN_MAYOR_IGUAL))
			return new MayorIg();
		else if (reconoce(PalabrasReservadas.TOKEN_IGUAL))
			return new Igual();
		else if (reconoce(PalabrasReservadas.TOKEN_DISTINTO))
			return new Distinto();
		else {
			// TODO Error
			error = true;
			return null;
		}
	}

	private InstruccionInterprete op3() throws InterpreteException {

		if (reconoce(PalabrasReservadas.TOKEN_O_LOGICA))
			return new O_Logica();
		else if (reconoce(PalabrasReservadas.TOKEN_SUMA))
			return new Sumar();
		else if (reconoce(PalabrasReservadas.TOKEN_RESTA))
			return new Restar();
		else {
			// TODO Error
			error = true;
			return null;
		}
	}

	private InstruccionInterprete op4() throws InterpreteException {

		if (reconoce(PalabrasReservadas.TOKEN_MULT))
			return new Multiplicar();
		else if (reconoce(PalabrasReservadas.TOKEN_DIV))
			return new Dividir();
		else if (reconoce(PalabrasReservadas.TOKEN_MODULO))
			return new Modulo();
		else if (reconoce(PalabrasReservadas.TOKEN_Y_LOGICA))
			return new Y_Logica();
		else {
			// TODO Error
			error = true;
			return null;
		}
	}

	private InstruccionInterprete op5asoc() throws InterpreteException {
		if (reconoce(PalabrasReservadas.TOKEN_RESTA))
			return new CambioSigno();
		else if (reconoce(PalabrasReservadas.TOKEN_NEGACION))
			return new Negacion();
		else {
			// TODO Error
			error = true;
			return null;
		}
	}

	private InstruccionInterprete op5noAsoc() throws InterpreteException {
		if (reconoce(PalabrasReservadas.TOKEN_CAST_INT))
			return new CastInt();
		else if (reconoce(PalabrasReservadas.TOKEN_CAST_REAL))
			return new CastReal();
		else {
			// TODO Error
			error = true;
			return null;
		}

	}

	public boolean reconoce(String token_necesario) {
		boolean reconoce = false;
		if (token_necesario.equals(a_lexico.getToken_actual())) {
			a_lexico.scanner();
			reconoce = true;
		} else {
			GestorErrores.agregaError(11, a_lexico.getFila(), a_lexico
					.getColumna(), "Se esperaba un token " + token_necesario
					+ " en lugar de " + a_lexico.getToken_actual());
			// throw new Exception("Token inesperado");
		}
		return reconoce;
	}

	private boolean cast(String valor, String tipo) {

		try {
			if (tipo.equals(TIPO_INT)) {
				Integer.parseInt(valor);
				return true;
			} else if (tipo.equals(TIPO_REAL)) {
				Float.parseFloat(valor);
				return true;
			} else {
				// TODO Error
				return false;
			}
		} catch (Exception ex) {
			// TODO Error
			return false;
		}
	}

}
