package es.ucm.plg.compilador.analizadorSintactico;

import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.compilador.tablaSimbolos.GestorTS;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoEntero;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoReal;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.datoPila.DatoPila;
import es.ucm.plg.interprete.instrucciones.Apilar;
import es.ucm.plg.interprete.instrucciones.ApilarDir;
import es.ucm.plg.interprete.instrucciones.CambioSigno;
import es.ucm.plg.interprete.instrucciones.CastInt;
import es.ucm.plg.interprete.instrucciones.CastReal;
import es.ucm.plg.interprete.instrucciones.DesapilarDir;
import es.ucm.plg.interprete.instrucciones.Distinto;
import es.ucm.plg.interprete.instrucciones.Dividir;
import es.ucm.plg.interprete.instrucciones.Entrada;
import es.ucm.plg.interprete.instrucciones.Igual;
import es.ucm.plg.interprete.instrucciones.Mayor;
import es.ucm.plg.interprete.instrucciones.MayorIg;
import es.ucm.plg.interprete.instrucciones.Menor;
import es.ucm.plg.interprete.instrucciones.MenorIg;
import es.ucm.plg.interprete.instrucciones.Modulo;
import es.ucm.plg.interprete.instrucciones.Multiplicar;
import es.ucm.plg.interprete.instrucciones.Negacion;
import es.ucm.plg.interprete.instrucciones.O_Logica;
import es.ucm.plg.interprete.instrucciones.Restar;
import es.ucm.plg.interprete.instrucciones.Salida;
import es.ucm.plg.interprete.instrucciones.Sumar;
import es.ucm.plg.interprete.instrucciones.Y_Logica;

public class Expresiones {

	private AnalizadorSintactico sintactico;

	public Expresiones(AnalizadorSintactico sintactico) {
		this.sintactico = sintactico;
	}

	private Tipo expresion() throws Exception {

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_IN)) {
			String lex = sintactico.getLexico().getLexema();
			sintactico.getCodigo().add(
					new Entrada(new DatoPila(DatoPila.INT, GestorTS
							.getInstancia().getDir(lex))));
			// sintactico.getCodigo().add(new DesapilarDir(new
			// DatoPila(DatoPila.INT,
			// GestorTS
			// .getInstancia().getDir(lex))));
			sintactico.getLexico().scanner();
			return GestorTS.getInstancia().getTipo(lex);
		} else if (sintactico.reconoce(PalabrasReservadas.TOKEN_OUT)) {
			Tipo tipo = expresion1();
			sintactico.getCodigo().add(new Salida());
			return tipo;
		} else
			return expresion1();
	}

	private Tipo expresion1() throws Exception {

		String lex = sintactico.getLexico().getLexema();
		Tipo tipo1 = null;

		if (reconoceAsignacion()) {

			tipo1 = GestorTS.getInstancia().getTipo(lex);

			// reconoce(PalabrasReservadas.TOKEN_ID);
			Tipo tipo2 = expresion2();
			if (!(tipo1 instanceof TipoEntero && tipo2 instanceof TipoReal)) {
				if (tipo1 instanceof TipoReal) {
					sintactico.getCodigo().add(new CastReal());
				}
				sintactico.getCodigo().add(
						new DesapilarDir(new DatoPila(DatoPila.INT, GestorTS
								.getInstancia().getDir(lex))));
				sintactico.getCodigo().add(
						new ApilarDir(new DatoPila(DatoPila.INT, GestorTS
								.getInstancia().getDir(lex))));
			} else {
				sintactico.setError(true);
				GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
						sintactico.getLexico().getColumna(),
						"No se puede asignar un real a un entero");
			}
		}
		// �Es una expresion 2?
		else {
			tipo1 = expresion2();
		}

		return tipo1;
	}

	public Tipo expresion2() throws Exception {

		Tipo tipo = null;
		InstruccionInterprete op;

		if ((tipo = expresion3()) != null) {
			if ((op = op2()) != null) {
				if ((tipo = expresion3()) != null) {
					tipo = new TipoEntero();
					sintactico.getCodigo().add(op);
				}
			}
		}

		return tipo;
	}

	private Tipo expresion3() throws Exception {
		Tipo tipo = null;

		tipo = expresion4();
		if (tipo != null) {
			tipo = expresion3RE(tipo);
		} else {
			sintactico.setError(true);
			// FIXME
			GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna(), "Error error");
		}
		return tipo;
	}

	private Tipo expresion3RE(Tipo tipo1) throws Exception {

		InstruccionInterprete op;
		Tipo tipoRes = null;
		Tipo tipo2, tipo3;

		if ((op = op3()) != null) {
			if ((tipo2 = expresion4()) != null) {
				if ((tipo3 = expresion3RE(tipo2)) != null) {
					if ((op instanceof O_Logica)
							&& (!(tipo2 instanceof TipoEntero) || !(tipo1 instanceof TipoEntero))) {
						if (op instanceof O_Logica)
							tipoRes = new TipoEntero();
						else if (tipo1 instanceof TipoEntero
								&& tipo3 instanceof TipoEntero)
							tipoRes = new TipoEntero();
						else
							tipoRes = new TipoReal();
					}
					sintactico.getCodigo().add(op);
				} else {
					sintactico.setError(true);
					GestorErrores.agregaError(11, sintactico.getLexico()
							.getFila(), sintactico.getLexico().getColumna(),
							"Se esperaba una expresión de tipo 4");
				}
			} else {
				sintactico.setError(true);
				GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
						sintactico.getLexico().getColumna(),
						"Se esperaba una expresión de tipo 4");
			}
		} else
			tipoRes = tipo1;

		return tipoRes;
	}

	private Tipo expresion4() throws Exception {

		Tipo tipo = null;

		tipo = expresion5();
		if (tipo != null) {
			tipo = expresion4RE(tipo);
		} else {
			sintactico.setError(true);
			// FIXME
			GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna(), "Error error");
		}

		return tipo;
	}

	private Tipo expresion4RE(Tipo tipo1) throws Exception {

		InstruccionInterprete op;
		Tipo tipoRes = null;
		Tipo tipo2, tipo3;

		if ((op = op4()) != null) {
			if ((tipo2 = expresion5()) != null) {
				if ((tipo3 = expresion4RE(tipo2)) != null) {
					// if (tipo1.getTipo().equals(TIPO_INT)
					// && tipo2.getTipo().equals(TIPO_INT)) {
					sintactico.getCodigo().add(op);
					if (op instanceof Y_Logica || op instanceof Modulo)
						tipoRes = new TipoEntero();
					else if (tipo1 instanceof TipoEntero
							&& tipo3 instanceof TipoEntero) {
						tipoRes = new TipoEntero();
					} else
						tipoRes = tipo3;
					// }
				}
			} else {
				sintactico.setError(true);
				GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
						sintactico.getLexico().getColumna(),
						"Se esperaba una expresion de tipo 5");
			}
		} else
			tipoRes = tipo1;

		return tipoRes;
	}

	private Tipo expresion5() throws Exception {

		InstruccionInterprete op;
		Tipo tipo = null;

		if ((op = op5asoc()) != null) {

			if ((tipo = expresion5()) != null) {
				if (!(op instanceof Negacion) || !(tipo instanceof TipoReal)) {
					sintactico.getCodigo().add(new CambioSigno());
				} else {
					sintactico.setError(true);
					GestorErrores.agregaError(11, sintactico.getLexico()
							.getFila(), sintactico.getLexico().getColumna(),
							"El tipo de la expresion debe ser un entero");
				}
			}
		} else if ((op = op5noAsoc()) != null) {
			if ((tipo = expresion6()) != null) {
				if (op instanceof CastInt) {
					sintactico.getCodigo().add(new CastInt());
					tipo = new TipoEntero();
				} else {
					sintactico.getCodigo().add(new CastInt());
					tipo = new TipoReal();
				}

			} else {
				sintactico.setError(true);
				GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
						sintactico.getLexico().getColumna(),
						"Se esperaba una expresion de tipo 6");
			}
		} else
			tipo = expresion6();

		return tipo;
	}

	private Tipo expresion6() throws Exception {

		Tipo tipo = null;
		String lex = sintactico.getLexico().getLexema();

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_PARENTESIS_AP)) {
			if ((tipo = expresion()) != null) {
				if (!sintactico
						.reconoce(PalabrasReservadas.TOKEN_PARENTESIS_CE)) {
					sintactico.setError(true);
					GestorErrores.agregaError(11, sintactico.getLexico()
							.getFila(), sintactico.getLexico().getColumna(),
							"Falta parentesis de cierre");
				}
			} else {
				sintactico.setError(true);
				GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
						sintactico.getLexico().getColumna(),
						"Expresion mal formada");
			}
		} else if (sintactico.reconoce(PalabrasReservadas.TOKEN_INT)) {
			if (cast(lex, new TipoEntero())) {
				tipo = new TipoEntero();
				sintactico.getCodigo().add(
						new Apilar(new DatoPila(DatoPila.INT, lex)));
			} else {
				sintactico.setError(true);
				GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
						sintactico.getLexico().getColumna(),
						"No se puede parsear el valor a entero");
			}
		} else if (sintactico.reconoce(PalabrasReservadas.TOKEN_REAL)) {
			if (cast(lex, new TipoReal())) {
				tipo = new TipoReal();
				sintactico.getCodigo().add(
						new Apilar(new DatoPila(DatoPila.REAL, lex)));
			} else {
				sintactico.setError(true);
				GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
						sintactico.getLexico().getColumna(),
						"No se puede parsear el valor a real");
			}
		} else if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
			if (GestorTS.getInstancia().existeID(lex)) {
				tipo = GestorTS.getInstancia().getTipo(lex);
				sintactico.getCodigo().add(
						new ApilarDir(new DatoPila(DatoPila.INT, GestorTS
								.getInstancia().getDir(lex))));
			} else {
				sintactico.setError(true);
				GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
						sintactico.getLexico().getColumna(),
						"Variable no declarada");
			}
		}

		return tipo;

	}

	private boolean cast(String valor, Tipo tipo) {

		try {
			if (tipo instanceof TipoEntero) {
				Integer.parseInt(valor);
				return true;
			} else if (tipo instanceof TipoReal) {
				Float.parseFloat(valor);
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	private InstruccionInterprete op2() {
		if (sintactico.reconoce(PalabrasReservadas.TOKEN_MENOR))
			return new Menor();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_MAYOR))
			return new Mayor();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_MENOR_IGUAL))
			return new MenorIg();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_MAYOR_IGUAL))
			return new MayorIg();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_IGUAL))
			return new Igual();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_DISTINTO))
			return new Distinto();
		else
			return null;
	}

	private InstruccionInterprete op3() {
		if (sintactico.reconoce(PalabrasReservadas.TOKEN_O_LOGICA))
			return new O_Logica();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_SUMA))
			return new Sumar();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_RESTA))
			return new Restar();
		else
			return null;
	}

	private InstruccionInterprete op4() {
		if (sintactico.reconoce(PalabrasReservadas.TOKEN_MULT))
			return new Multiplicar();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_DIV))
			return new Dividir();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_MODULO))
			return new Modulo();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_Y_LOGICA))
			return new Y_Logica();
		else
			return null;
	}

	private InstruccionInterprete op5asoc() {

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_RESTA))
			return new CambioSigno();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_NEGACION))
			return new Negacion();
		else
			return null;
	}

	private InstruccionInterprete op5noAsoc() {

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_CAST_INT))
			return new CastInt();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_CAST_REAL))
			return new CastReal();
		else
			return null;
	}

	private boolean reconoceAsignacion() {

		boolean reconoce = false;

		// Saco una foto del estado del léxico por si tengo que retroceder.
		int fila = sintactico.getLexico().getFila();
		int columna = sintactico.getLexico().getColumna();
		int indice = sintactico.getLexico().getIndice();
		String lexema = sintactico.getLexico().getLexema();
		int estado = sintactico.getLexico().getEstado();
		char next_char = sintactico.getLexico().getNext_char();
		String token_actual = sintactico.getLexico().getToken_actual();
		int parentesis_indice = sintactico.getLexico().getParentesis_indice();
		int parentesis_fila = sintactico.getLexico().getParentesis_fila();
		int parentesis_columna = sintactico.getLexico().getParentesis_columna();

		if (sintactico.getLexico().getToken_actual()
				.equals(PalabrasReservadas.TOKEN_ID)) {
			sintactico.getLexico().scanner();
			if (sintactico.getLexico().getToken_actual()
					.equals(PalabrasReservadas.TOKEN_ASIGNACION)) {
				sintactico.getLexico().scanner();
				reconoce = true;
			} else {
				sintactico.getLexico().setFila(fila);
				sintactico.getLexico().setColumna(columna);
				sintactico.getLexico().setIndice(indice);
				sintactico.getLexico().setLexema(lexema);
				sintactico.getLexico().setEstado(estado);
				sintactico.getLexico().setNext_char(next_char);
				sintactico.getLexico().setToken_actual(token_actual);
				sintactico.getLexico().setParentesis_indice(parentesis_indice);
				sintactico.getLexico().setParentesis_fila(parentesis_fila);
				sintactico.getLexico()
						.setParentesis_columna(parentesis_columna);
			}
		}
		return reconoce;
	}

}
