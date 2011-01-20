package compilador.analizador_sintactico;

import interprete.InstruccionInterprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;
import interprete.instrucciones.Apilar;
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
import compilador.gestorErrores.GestorErrores;
import compilador.tablaSimbolos.GestorTS;
import compilador.tablaSimbolos.tipos.Tipo;

public class AnalizadorSintactico {

	private final String TIPO_INT = "<t:int>";
	private final String TIPO_REAL = "<t:real>";

	private AnalizadorLexico lexico;
	private Vector<InstruccionInterprete> codigo;
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
		this.lexico.scanner();
		this.error = false;
		this.dir = 0;
		this.finDecs = false;
		declaraciones();
		this.codigo = new Vector<InstruccionInterprete>();
		acciones();
		// TODO: Añadir al codigo la instruccion STOP
	}

	public void declaraciones() throws Exception {
		declaracion();
		declaracionesRE();
	}

	public void declaracionesRE() throws Exception {
		declaracion();
		if (!finDecs)
			declaracionesRE();
		// En caso de no ser ninguno de los 2 pasa a acciones
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

						// Si la variable no existe, la añado a la TS
						if (!GestorTS.getInstancia().existeID(lex)) {
							dir++;
							GestorTS.getInstancia().añadeID(lex, dir, tipo);
						} else {
							error = true;
							GestorErrores.agregaError(11, lexico.getFila(),
									lexico.getColumna(), "Variable " + lex
											+ " duplicada");
						}
					} else {
						error = true;
						GestorErrores.agregaError(11, lexico.getFila(), lexico
								.getColumna(), "Falta un punto y coma");
					}
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(), lexico
							.getColumna(), "Se esperaba un tkid en lugar de"
							+ lexico.getToken_actual());
				}
			} else {
				// Si no reconozco una declaración, o ya he acabado las
				// declaraciones o es un error
				if (!finDecs)
					finDecs = true;
				else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(), lexico
							.getColumna(), "Se esperaba un tipo");
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

	public boolean acciones() throws Exception {
		boolean ok = false;
		if (accion()) {
			if (accionesRE()) {
				ok = true;
			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Se esperaba una acción");
			}
		} else {
			error = true;
			GestorErrores.agregaError(11, lexico.getFila(),
					lexico.getColumna(), "Se esperaba una acción");
		}
		return ok;
	}

	private boolean accionesRE() throws Exception {
		boolean ok = false;
		if (accion()) {
			if (accionesRE()) {
				ok = true;
			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Se esperaba una acción");
			}
		} else {
			error = true;
			GestorErrores.agregaError(11, lexico.getFila(),
					lexico.getColumna(), "Se esperaba una acción");
		}
		return ok;
	}

	private boolean accion() throws Exception {
		boolean ok = false;
		Tipo tipo = expresion();
		if (tipo != null) {
			if (reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA)) {
				ok = true;
			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Falta un punto y coma");
			}
		} else {
			error = true;
			GestorErrores.agregaError(11, lexico.getFila(),
					lexico.getColumna(), "Se esperaba una expresion");
		}
		return ok;
	}

	private Tipo expresion() throws Exception {

		if (reconoce(PalabrasReservadas.TOKEN_IN)) {
			// FIXME Esto seguro que está mal
			String lex = lexico.getLexema();
			error = error || !GestorTS.getInstancia().existeID(lex);
			codigo.add(new Entrada());
			codigo.add(new DesapilarDir(new DatoPila(DatoPila.INT, GestorTS
					.getInstancia().getDir(lex))));
			return GestorTS.getInstancia().getTipo(lex);
		} else if (reconoce(PalabrasReservadas.TOKEN_OUT)) {
			Tipo tipo = expresion1();
			codigo.add(new Salida());
			return tipo;
		} else
			return expresion1();
	}

	private Tipo expresion1() throws Exception {

		String lex = lexico.getLexema();
		Tipo tipo1 = null;

		if (!error) {
			// ¿Es una asignación?
			if (reconoce(PalabrasReservadas.TOKEN_ID)) {
				tipo1 = GestorTS.getInstancia().getTipo(lex);

				if (op1()) {
					Tipo tipo2 = expresion1();
					if (!(tipo1.getTipo().equals(TIPO_INT) && tipo2.getTipo()
							.equals(TIPO_REAL))) {
						if (tipo1.getTipo().equals(TIPO_REAL)) {
							codigo.add(new CastReal());
							codigo.add(new DesapilarDir(new DatoPila(
									DatoPila.INT, GestorTS.getInstancia()
											.getDir(lex))));
						} else {
							codigo.add(new DesapilarDir(new DatoPila(
									DatoPila.INT, GestorTS.getInstancia()
											.getDir(lex))));
						}
					} else {
						error = true;
						GestorErrores.agregaError(11, lexico.getFila(), lexico
								.getColumna(),
								"No se puede asignar un real a un entero");
					}
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(), lexico
							.getColumna(), "Se esperaba una variable");
				}

			}
			// ¿Es una expresion 2?
			else {
				if ((tipo1 = expresion2()) == null) {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(), lexico
							.getColumna(),
							"Se esperaba una expresion de tipo 2");
				}
			}
		}

		return tipo1;
	}

	private Tipo expresion2() throws Exception {

		Tipo tipo = null;
		InstruccionInterprete op;

		if (!error) {
			if ((tipo = expresion3()) != null) {
				if ((op = op2()) != null) {
					tipo = new Tipo(TIPO_INT, 1);
					codigo.add(op);
				}
			} else {
				if ((tipo = expresion3()) == null) {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(), lexico
							.getColumna(),
							"Se esperaba una expresion de tipo 3");
				}
			}
		}

		return tipo;
	}

	private Tipo expresion3() throws Exception {
		Tipo tipo = null;
		if (!error) {
			tipo = expresion4();
			if (tipo != null) {
				tipo = expresion3RE(tipo);
			} else {
				error = true;
				// FIXME
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Error error");
			}
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
					if (!(op instanceof O_Logica && (!tipo2.equals(TIPO_INT) || !tipo1
							.equals(TIPO_INT)))) {
						codigo.add(op);
						if (op instanceof O_Logica)
							tipoRes = new Tipo(TIPO_INT, 1);
						else if (tipo1.equals(TIPO_INT)
								&& tipo3.equals(TIPO_INT))
							tipoRes = new Tipo(TIPO_INT, 1);
						else
							tipoRes = new Tipo(TIPO_REAL, 1);
					} else {
						error = true;
						GestorErrores.agregaError(11, lexico.getFila(), lexico
								.getColumna(),
								"El tipo de uno de los errores es incorrecto");
					}
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(), lexico
							.getColumna(),
							"Se esperaba una expresión de tipo 4");
				}
			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Se esperaba una expresión de tipo 4");
			}
		} else
			tipoRes = tipo1;

		return tipoRes;
	}

	private Tipo expresion4() throws Exception {
		Tipo tipo = null;
		if (!error) {
			tipo = expresion5();
			if (tipo != null) {
				tipo = expresion4RE(tipo);
			} else {
				error = true;
				// FIXME
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Error error");
			}
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
					if (tipo1.getTipo().equals(TIPO_INT)
							&& tipo2.getTipo().equals(TIPO_INT)) {
						codigo.add(op);
						if (op instanceof Y_Logica || op instanceof Modulo)
							tipoRes = new Tipo(TIPO_INT, 1);
						else if (tipo1.equals(TIPO_INT)
								&& tipo3.equals(TIPO_INT)) {
							tipoRes = new Tipo(TIPO_INT, 1);
						} else
							tipoRes = new Tipo(TIPO_REAL, 1);
					}
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(), lexico
							.getColumna(),
							"Se esperaba una expresión de tipo 4");
				}
			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Se esperaba una expresión de tipo 5");
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
				if (!(op instanceof Negacion)
						|| !(tipo.getTipo().equals(TIPO_REAL))) {
					codigo.add(op);
				} else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(), lexico
							.getColumna(),
							"El tipo de la expresión debe ser un entero");
				}
			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Se esperaba una expresión de tipo 5");
			}
		} else if ((op = op5noAsoc()) != null) {
			if ((tipo = expresion6()) != null) {
				if (op instanceof CastInt)
					tipo = new Tipo(TIPO_INT, 1);
				else
					tipo = new Tipo(TIPO_REAL, 1);

			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Se esperaba una expresión de tipo 6");
			}
		} else {
			if ((tipo = expresion6()) == null) {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Se esperaba una expresión de tipo 6");
			}
		}

		return tipo;
	}

	private Tipo expresion6() throws Exception {

		Tipo tipo = null;

		if (reconoce(PalabrasReservadas.TOKEN_PARENTESIS_AP)) {
			if ((tipo = expresion()) != null) {
				lexico.scanner();
				if (!reconoce(PalabrasReservadas.TOKEN_PARENTESIS_CE)) {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(), lexico
							.getColumna(), "Falta paréntesis de cierre");
				}
			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Expresion mal formada");
			}
		} else if (reconoce(PalabrasReservadas.TOKEN_LIT_INT)) {
			if (cast(lexico.getLexema(), tipo)) {
				tipo = new Tipo(TIPO_INT, 1);
				codigo.add(new Apilar(new DatoPila(DatoPila.INT, lexico
						.getLexema())));
			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "No se puede parsear el valor a entero");
			}
		} else if (reconoce(PalabrasReservadas.TOKEN_REAL)) {
			if (cast(lexico.getLexema(), tipo)) {
				tipo = new Tipo(TIPO_INT, 1);
				codigo.add(new Apilar(new DatoPila(DatoPila.REAL, lexico
						.getLexema())));
			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "No se puede parsear el valor a real");
			}
		} else if (reconoce(PalabrasReservadas.TOKEN_ID)) {
			if (GestorTS.getInstancia().existeID(lexico.getLexema())) {
				tipo = GestorTS.getInstancia().getTipo(lexico.getLexema());
				if (tipo.equals(TIPO_INT))
					codigo.add(new Apilar(new DatoPila(DatoPila.INT, lexico
							.getLexema())));
				else if (tipo.equals(TIPO_INT))
					codigo.add(new Apilar(new DatoPila(DatoPila.REAL, lexico
							.getLexema())));
				else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(), lexico
							.getColumna(), "Tipo inválido");
				}
			} else {
				error = true;
				GestorErrores.agregaError(11, lexico.getFila(), lexico
						.getColumna(), "Variable no declarada");
			}
		} else {
			error = true;
			GestorErrores.agregaError(11, lexico.getFila(),
					lexico.getColumna(), "Expresion mal formada");
		}

		return tipo;

	}

	private boolean op1() {
		return reconoce(PalabrasReservadas.TOKEN_ASIGNACION);
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
		else
			return null;
	}

	private InstruccionInterprete op3() throws InterpreteException {

		if (reconoce(PalabrasReservadas.TOKEN_O_LOGICA))
			return new O_Logica();
		else if (reconoce(PalabrasReservadas.TOKEN_SUMA))
			return new Sumar();
		else if (reconoce(PalabrasReservadas.TOKEN_RESTA))
			return new Restar();
		else
			return null;
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
		else
			return null;
	}

	private InstruccionInterprete op5asoc() throws InterpreteException {
		if (reconoce(PalabrasReservadas.TOKEN_RESTA))
			return new CambioSigno();
		else if (reconoce(PalabrasReservadas.TOKEN_NEGACION))
			return new Negacion();
		else
			return null;
	}

	private InstruccionInterprete op5noAsoc() throws InterpreteException {
		if (reconoce(PalabrasReservadas.TOKEN_CAST_INT))
			return new CastInt();
		else if (reconoce(PalabrasReservadas.TOKEN_CAST_REAL))
			return new CastReal();
		else
			return null;
	}

	public boolean reconoce(String token_necesario) {

		boolean reconoce = false;

		if (token_necesario.equals(lexico.getToken_actual())) {
			lexico.scanner();
			reconoce = true;
		}

		return reconoce;
	}

	private boolean cast(String valor, Tipo tipo) {

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

}
