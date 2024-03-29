package compilador.analizador_sintactico;

import interprete.InstruccionInterprete;
import interprete.datoPila.DatoPila;
import interprete.instrucciones.Apilar;
import interprete.instrucciones.ApilarDir;
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

import java.util.ArrayList;

import compilador.analizador_lexico.AnalizadorLexico;
import compilador.analizador_lexico.PalabrasReservadas;
import compilador.gestorErrores.GestorErrores;
import compilador.tablaSimbolos.GestorTS;
import compilador.tablaSimbolos.tipos.Tipo;

public class AnalizadorSintactico {

	private final String TIPO_INT = "<t:int>";
	private final String TIPO_REAL = "<t:real>";

	private AnalizadorLexico lexico;
	private ArrayList<InstruccionInterprete> codigo;
	private int dir;
	private boolean error;
	private boolean finDecs;

	public AnalizadorSintactico(AnalizadorLexico lexico) {
		this.lexico = lexico;
	}

	public AnalizadorLexico getLexico() {
		return lexico;
	}

	public void setLexico(AnalizadorLexico aLexico) {
		this.lexico = aLexico;
	}

	public ArrayList<InstruccionInterprete> getCodigo() {
		return codigo;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public boolean isError() {
		return error;
	}

	public void iniciaSintactico(){
		programa();
	}

	public void programa(){
		try {
			this.lexico.scanner();
			this.error = false;
			this.dir = 0;
			this.finDecs = false;
			declaraciones();
			this.codigo = new ArrayList<InstruccionInterprete>();
			acciones();
		} catch (Exception ex) {
			error = true;
			GestorErrores.agregaError(11, lexico.getFila(),
					lexico.getColumna(), "Error!!!");
		}
		if (error) {
			System.out.println("　OMPILADO CON ERRORES!!!");
			System.out.println(GestorErrores.getErrores_propios().toString());
		}
	}

	public void declaraciones(){
		declaracion();
		declaracionesRE();
	}

	public void declaracionesRE(){
		if (!error) {
			declaracion();
			if (!finDecs)
				declaracionesRE();
		}
	}

	public void declaracion() {

		if (!error) {

			// Reconozco el tipo
			Tipo tipo = desctipo();
			if (tipo != null) {
				String lex = lexico.getLexema();

				// Reconozco el id
				if (reconoce(PalabrasReservadas.TOKEN_ID)) {

					// Reconozco el punto y coma
					if (reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA)) {

						// Si la variable no existe, la a帽ado a la TS
						if (!GestorTS.getInstancia().existeID(lex)) {
							dir = GestorTS.getInstancia().annadeID(lex, dir,
									tipo);
						} else {
							error = true;
							GestorErrores.agregaError(11, lexico.getFila(),
									lexico.getColumna(), "Variable " + lex
											+ " duplicada");
						}
					} else {
						error = true;
						GestorErrores.agregaError(11, lexico.getFila(),
								lexico.getColumna(), "Falta un punto y coma");
					}
				} else {
					error = true;
					GestorErrores.agregaError(
							11,
							lexico.getFila(),
							lexico.getColumna(),
							"Se esperaba un tkid en lugar de"
									+ lexico.getToken_actual());
				}
			} else {
				// Si no reconozco una declaraci贸n, o ya he acabado las
				// declaraciones o es un error
				if (!finDecs)
					finDecs = true;
				else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(),
							lexico.getColumna(), "Se esperaba un tipo");
				}
			}
		}
	}

	private Tipo desctipo() {

		Tipo tipo = null;

		if (!error) {
			if (reconoce(PalabrasReservadas.TOKEN_INT))
				tipo = new Tipo(TIPO_INT, 1);
			else if (reconoce(PalabrasReservadas.TOKEN_REAL))
				tipo = new Tipo(TIPO_REAL, 1);
		}

		return tipo;

	}

	public boolean acciones(){

		boolean ok = false;

		if (!error) {
			if (accion()) {
				if (accionesRE()) {
					ok = true;
				} else {
					ok = error;
				}
			} else {
				ok = error;
			}
		}

		return ok;
	}

	private boolean accionesRE(){

		boolean ok = false;

		if (!error && !lexico.isFin_programa()) {
			if (accion()) {
				if (accionesRE()) {
					ok = true;
				} else {
					ok = false;
				}
			} else {
				ok = false;
			}
		}

		return ok;
	}

	private boolean accion(){

		boolean ok = false;

		if (!error) {
			Tipo tipo = expresion();
			if (tipo != null) {
				if (reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA)) {
					ok = true;
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(),
							lexico.getColumna(), "Falta un punto y coma");
				}
			} else if (lexico.isFin_programa()) {
				ok = true;
			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(),
						lexico.getColumna(), "Se esperaba una expresion");
			}

		}

		return ok;
	}

	private Tipo expresion(){

		if (!error) {

			if (reconoce(PalabrasReservadas.TOKEN_IN)) {
				String lex = lexico.getLexema();
				error = error || !GestorTS.getInstancia().existeID(lex);
				codigo.add(new Entrada());
				codigo.add(new DesapilarDir(new DatoPila(DatoPila.INT, GestorTS
						.getInstancia().getDir(lex))));
				lexico.scanner();
				return GestorTS.getInstancia().getTipo(lex);
			} else if (reconoce(PalabrasReservadas.TOKEN_OUT)) {
				Tipo tipo = expresion1();
				codigo.add(new Salida());
				return tipo;
			} else
				return expresion1();
		} else
			return null;
	}

	private Tipo expresion1(){

		String lex = lexico.getLexema();
		Tipo tipo1 = null;

		if (!error) {

			if (reconoceAsignacion()) {

				tipo1 = GestorTS.getInstancia().getTipo(lex);

				// reconoce(PalabrasReservadas.TOKEN_ID);
				Tipo tipo2 = expresion2();
				if (!(tipo1.getTipo().equals(TIPO_INT) && tipo2.getTipo()
						.equals(TIPO_REAL))) {
					if (tipo1.getTipo().equals(TIPO_REAL)) {
						codigo.add(new CastReal());
						codigo.add(new DesapilarDir(new DatoPila(DatoPila.INT,
								GestorTS.getInstancia().getDir(lex))));
					} else {
						codigo.add(new DesapilarDir(new DatoPila(DatoPila.INT,
								GestorTS.getInstancia().getDir(lex))));
					}
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(),
							lexico.getColumna(),
							"No se puede asignar un real a un entero");
				}
			}
			// 驴Es una expresion 2?
			else {
				tipo1 = expresion2();
			}
		}

		return tipo1;
	}

	private Tipo expresion2(){

		Tipo tipo = null;
		InstruccionInterprete op;

		if (!error) {
			if ((tipo = expresion3()) != null) {
				if ((op = op2()) != null) {
					if ((tipo = expresion3()) != null) {
						tipo = new Tipo(TIPO_INT, 1);
						codigo.add(op);
					}
				}
			}
		}

		return tipo;
	}

	private Tipo expresion3(){
		Tipo tipo = null;
		if (!error) {
			tipo = expresion4();
			if (tipo != null) {
				tipo = expresion3RE(tipo);
			} else {
				error = true;
				// FIXME
				GestorErrores.agregaError(11, lexico.getFila(),
						lexico.getColumna(), "Error error");
			}
		}
		return tipo;
	}

	private Tipo expresion3RE(Tipo tipo1){

		InstruccionInterprete op;
		Tipo tipoRes = null;
		Tipo tipo2, tipo3;

		if (!error) {

			if ((op = op3()) != null) {
				if ((tipo2 = expresion4()) != null) {
					if ((tipo3 = expresion3RE(tipo2)) != null) {
						if ((op instanceof O_Logica)
								&& (!tipo2.equals(TIPO_INT) || !tipo1
										.equals(TIPO_INT))) {
							if (op instanceof O_Logica)
								tipoRes = new Tipo(TIPO_INT, 1);
							else if (tipo1.equals(TIPO_INT)
									&& tipo3.equals(TIPO_INT))
								tipoRes = new Tipo(TIPO_INT, 1);
							else
								tipoRes = new Tipo(TIPO_REAL, 1);
						}
						codigo.add(op);
					} else {
						error = true;
						GestorErrores.agregaError(11, lexico.getFila(),
								lexico.getColumna(),
								"Se esperaba una expresi贸n de tipo 4");
					}
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(),
							lexico.getColumna(),
							"Se esperaba una expresi贸n de tipo 4");
				}
			} else
				tipoRes = tipo1;
		}

		return tipoRes;
	}

	private Tipo expresion4(){

		Tipo tipo = null;

		if (!error) {
			tipo = expresion5();
			if (tipo != null) {
				tipo = expresion4RE(tipo);
			} else {
				error = true;
				// FIXME
				GestorErrores.agregaError(11, lexico.getFila(),
						lexico.getColumna(), "Error error");
			}
		}

		return tipo;
	}

	private Tipo expresion4RE(Tipo tipo1){

		InstruccionInterprete op;
		Tipo tipoRes = null;
		Tipo tipo2, tipo3;

		if (!error) {
			if ((op = op4()) != null) {
				if ((tipo2 = expresion5()) != null) {
					if ((tipo3 = expresion4RE(tipo2)) != null) {
						// if (tipo1.getTipo().equals(TIPO_INT)
						// && tipo2.getTipo().equals(TIPO_INT)) {
						codigo.add(op);
						if (op instanceof Y_Logica || op instanceof Modulo)
							tipoRes = new Tipo(TIPO_INT, 1);
						else if (tipo1.equals(TIPO_INT)
								&& tipo3.equals(TIPO_INT)) {
							tipoRes = new Tipo(TIPO_INT, 1);
						} else
							tipoRes = tipo3;
						// }
					}
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(),
							lexico.getColumna(),
							"Se esperaba una expresi贸n de tipo 5");
				}
			} else
				tipoRes = tipo1;
		}

		return tipoRes;
	}

	private Tipo expresion5(){

		InstruccionInterprete op;
		Tipo tipo = null;

		if (!error) {
			if ((op = op5asoc()) != null) {

				if ((tipo = expresion5()) != null) {
					if (!(op instanceof Negacion)
							|| !(tipo.getTipo().equals(TIPO_REAL))) {
						codigo.add(new CambioSigno());
					} else {
						error = true;
						GestorErrores.agregaError(11, lexico.getFila(),
								lexico.getColumna(),
								"El tipo de la expresi贸n debe ser un entero");
					}
				}
			} else if ((op = op5noAsoc()) != null) {
				if ((tipo = expresion6()) != null) {
					if (op instanceof CastInt) {
						this.codigo.add(new CastInt());
						tipo = new Tipo(TIPO_INT, 1);
					} else {
						this.codigo.add(new CastInt());
						tipo = new Tipo(TIPO_REAL, 1);
					}

				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(),
							lexico.getColumna(),
							"Se esperaba una expresi贸n de tipo 6");
				}
			} else
				tipo = expresion6();
		}

		return tipo;
	}

	private Tipo expresion6(){

		Tipo tipo = null;
		String lex = lexico.getLexema();

		if (!error) {
			if (reconoce(PalabrasReservadas.TOKEN_PARENTESIS_AP)) {
				if ((tipo = expresion()) != null) {
					if (!reconoce(PalabrasReservadas.TOKEN_PARENTESIS_CE)) {
						error = true;
						GestorErrores.agregaError(11, lexico.getFila(),
								lexico.getColumna(),
								"Falta par茅ntesis de cierre");
					}
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(),
							lexico.getColumna(), "Expresion mal formada");
				}
			} else if (reconoce(PalabrasReservadas.TOKEN_INT)) {
				if (cast(lex, new Tipo(TIPO_INT, 1))) {
					tipo = new Tipo(TIPO_INT, 1);
					codigo.add(new Apilar(new DatoPila(DatoPila.INT, lex)));
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(),
							lexico.getColumna(),
							"No se puede parsear el valor a entero");
				}
			} else if (reconoce(PalabrasReservadas.TOKEN_REAL)) {
				if (cast(lex, new Tipo(TIPO_REAL, 1))) {
					tipo = new Tipo(TIPO_REAL, 1);
					codigo.add(new Apilar(new DatoPila(DatoPila.REAL, lex)));
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(),
							lexico.getColumna(),
							"No se puede parsear el valor a real");
				}
			} else if (reconoce(PalabrasReservadas.TOKEN_ID)) {
				if (GestorTS.getInstancia().existeID(lex)) {
					tipo = GestorTS.getInstancia().getTipo(lex);
					this.codigo.add(new ApilarDir(new DatoPila(DatoPila.INT,
							GestorTS.getInstancia().getDir(lex))));
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(),
							lexico.getColumna(), "Variable no declarada");
				}
			}
		}

		return tipo;

	}

	private InstruccionInterprete op2() {
		if (!error) {
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
			else
				return null;
		}
		return null;
	}

	private InstruccionInterprete op3() {
		if (!error) {
			if (reconoce(PalabrasReservadas.TOKEN_O_LOGICA))
				return new O_Logica();
			else if (reconoce(PalabrasReservadas.TOKEN_SUMA))
				return new Sumar();
			else if (reconoce(PalabrasReservadas.TOKEN_RESTA))
				return new Restar();
			else
				return null;
		}
		return null;
	}

	private InstruccionInterprete op4() {
		if (!error) {
			if (reconoce(PalabrasReservadas.TOKEN_MULT))
				return new Multiplicar();
			else if (reconoce(PalabrasReservadas.TOKEN_DIV))
				return new Dividir();
			else if (reconoce(PalabrasReservadas.TOKEN_MODULO))
				return new Modulo();
			else if (reconoce(PalabrasReservadas.TOKEN_Y_LOGICA))
				return new Y_Logica();
			else
				return null;
		}
		return null;
	}

	private InstruccionInterprete op5asoc() {

		if (!error) {
			if (reconoce(PalabrasReservadas.TOKEN_RESTA))
				return new CambioSigno();
			else if (reconoce(PalabrasReservadas.TOKEN_NEGACION))
				return new Negacion();
			else
				return null;
		}
		return null;
	}

	private InstruccionInterprete op5noAsoc() {

		if (!error) {
			if (reconoce(PalabrasReservadas.TOKEN_CAST_INT))
				return new CastInt();
			else if (reconoce(PalabrasReservadas.TOKEN_CAST_REAL))
				return new CastReal();
			else
				return null;
		}
		return null;
	}

	public boolean reconoce(String token_necesario) {

		boolean reconoce = false;

		if (!error) {
			if (token_necesario.equals(lexico.getToken_actual())) {
				lexico.scanner();
				reconoce = true;
			}
		}

		return reconoce;
	}

	private boolean reconoceAsignacion() {

		boolean reconoce = false;

		// Saco una foto del estado del l茅xico por si tengo que retroceder.
		int fila = lexico.getFila();
		int columna = lexico.getColumna();
		int indice = lexico.getIndice();
		String lexema = lexico.getLexema();
		int estado = lexico.getEstado();
		char next_char = lexico.getNext_char();
		String token_actual = lexico.getToken_actual();
		int parentesis_indice = lexico.getParentesis_indice();
		int parentesis_fila = lexico.getParentesis_fila();
		int parentesis_columna = lexico.getParentesis_columna();

		if (lexico.getToken_actual().equals(PalabrasReservadas.TOKEN_ID)) {
			lexico.scanner();
			if (lexico.getToken_actual().equals(
					PalabrasReservadas.TOKEN_ASIGNACION)) {
				lexico.scanner();
				reconoce = true;
			} else {
				lexico.setFila(fila);
				lexico.setColumna(columna);
				lexico.setIndice(indice);
				lexico.setLexema(lexema);
				lexico.setEstado(estado);
				lexico.setNext_char(next_char);
				lexico.setToken_actual(token_actual);
				lexico.setParentesis_indice(parentesis_indice);
				lexico.setParentesis_fila(parentesis_fila);
				lexico.setParentesis_columna(parentesis_columna);
			}
		}
		return reconoce;
	}

	private boolean cast(String valor, Tipo tipo) {
		if (!error) {
			try {
				if (tipo.getTipo().equals(TIPO_INT)) {
					Integer.parseInt(valor);
					return true;
				} else if (tipo.getTipo().equals(TIPO_REAL)) {
					Float.parseFloat(valor);
					return true;
				} else {
					return false;
				}
			} catch (Exception ex) {
				return false;
			}
		}
		return false;
	}

}
