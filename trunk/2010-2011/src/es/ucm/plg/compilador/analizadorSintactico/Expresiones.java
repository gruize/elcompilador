package es.ucm.plg.compilador.analizadorSintactico;

import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
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

	/*
	 * expresion ≡ expresionMem || expresionIn || expresionOut || expresion2
	 */
	private Tipo expresion() throws SintacticoException {

		Tipo tipo = null;

		// expresionMem
		tipo = expresionMem();

		if (tipo == null) {
			tipo = expresionIn();
		}

		if (tipo == null) {
			tipo = expresionOut();
		}

		if (tipo == null) {
			tipo = expresion2();
		}

		return tipo;

	}

	/*
	 * expresionMem ≡ mem = expresion2
	 */
	private Tipo expresionMem() throws SintacticoException {

		Tipo tipo1 = null;
		Tipo tipo2 = null;

		try {

			// mem
			tipo1 = sintactico.getTipos().mem();

			if (tipo1 != null) {

				// =
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_IGUAL)) {
					throw new MiExcepcion(
							SintacticoException.EXPRESION_INVALIDA);
				}

				// expresion2
				tipo2 = expresion2();

				// error = !compatibles(expresion2.ts,mem.id,expresion2.tipo)
				if (!sintactico.getTipos().compatibles(tipo1, tipo2)) {
					throw new MiExcepcion(SintacticoException.TIPO_INCOMPATIBLE);
				}

				// TODO METER LA PARTE DEL CÓDIGO

			}

			return tipo1;

			// error = !compatibles(expresion2.ts,mem.id,expresion2.tipo) &&
			// expresion_invalida
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/*
	 * expresionIn ≡ in id
	 */
	private Tipo expresionIn() throws SintacticoException {

		Tipo tipo = null;

		try {

			if (sintactico.reconoce(PalabrasReservadas.TOKEN_IN)) {

				// id
				String id = sintactico.getLexico().getLexema();

				// error = falta_expresion
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
					throw new MiExcepcion(SintacticoException.FALTA_ID);
				}

				// error = ¬existeID(op0in.ts, id.lex)
				if (!GestorTS.getInstancia().existeID(id)) {
					throw new MiExcepcion(
							SintacticoException.VARIABLE_NO_DECLARADA);
				}

				// cod = entrada ++ desapila_dir(expresion.tsh[id.lex].dir)
				sintactico.getCodigo().add(
						new Entrada(new DatoPila(DatoPila.INT, GestorTS
								.getInstancia().getDir(id))));

				// tipo = dameTipo(op0in.ts,id.lex)
				tipo = GestorTS.getInstancia().getTipo(id);

			}

			return tipo;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/*
	 * expresionOut ≡ out expresion2
	 */
	private Tipo expresionOut() throws SintacticoException {

		Tipo tipo = null;

		try {
			// out
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_OUT)) {

				// expresion2
				tipo = expresion2();

				if (tipo == null) {
					throw new MiExcepcion(
							SintacticoException.EXPRESION_INVALIDA);
				}

				// cod = expresion2.cod ++ salida
				sintactico.getCodigo().add(new Salida());
			}

			return tipo;

			// error = falta_expresion && ¬existeID(op0in.ts, id.lex)
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	/*
	 * expresion2 ≡ expresion3 op2 expresion3 | expresion3
	 */
	public Tipo expresion2() throws SintacticoException {

		Tipo tipo1 = null;
		Tipo tipo2 = null;
		Tipo tipo = null;
		InstruccionInterprete op;

		try {
			// expresion3
			tipo1 = expresion3();

			if (tipo1 != null) {

				// op2
				op = op2();

				if (op != null) {

					// expresion3
					tipo2 = expresion3();

					// error = expresion inválida
					if (tipo2 == null) {
						throw new MiExcepcion(
								SintacticoException.EXPRESION_INVALIDA);
					}

					// error = validoOperacion(expresion31.ts,
					// expresion30.tipo,op2.op,expresion31.tipo)
					if (!validoOperacion(tipo1, tipo2)) {
						throw new MiExcepcion(
								SintacticoException.TIPO_INCOMPATIBLE);
					}

					// tipo = <t:int>
					tipo = new TipoEntero();

				}
			}

			return tipo;

			// error = expresion31.error && validoOperacion(expresion31.ts,
			// expresion30.tipo,op2.op,expresion31.tipo)
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	/*
	 * expresion3 = expresion4 expresion3RE
	 */
	private Tipo expresion3() throws SintacticoException {

		Tipo tipo1 = null;
		Tipo tipo2 = null;
		Tipo tipo = null;
		
		try {
			// expresion4
			tipo = expresion4();

			if (tipo != null) {
				tipo = expresion3RE(tipo);
			}
			
			// error = validoOperacion (expresion4.ts, expresion3RE0.tipo, op3.op, expresion4.tipo)
			if (!validoOperacion(tipo1, tipo2)) {
				throw new MiExcepcion(
						SintacticoException.TIPO_INCOMPATIBLE);
			}

			return tipo;

			// error = falta_expresion && ¬existeID(op0in.ts, id.lex)
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	private Tipo expresion3RE(Tipo tipo1) throws SintacticoException {

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
					// sintactico.setError(true);
					// GestorErrores.agregaError(11, sintactico.getLexico()
					// .getFila(), sintactico.getLexico().getColumna(),
					// "Se esperaba una expresión de tipo 4");
				}
			} else {
				// sintactico.setError(true);
				// GestorErrores.agregaError(11,
				// sintactico.getLexico().getFila(),
				// sintactico.getLexico().getColumna(),
				// "Se esperaba una expresión de tipo 4");
			}
		} else
			tipoRes = tipo1;

		return tipoRes;
	}

	private Tipo expresion4() throws SintacticoException {

		Tipo tipo = null;

		tipo = expresion5();
		if (tipo != null) {
			tipo = expresion4RE(tipo);
		} else {
			// sintactico.setError(true);
			// // FIXME
			// GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
			// sintactico.getLexico().getColumna(), "Error error");
		}

		return tipo;
	}

	private Tipo expresion4RE(Tipo tipo1) throws SintacticoException {

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
				// sintactico.setError(true);
				// GestorErrores.agregaError(11,
				// sintactico.getLexico().getFila(),
				// sintactico.getLexico().getColumna(),
				// "Se esperaba una expresion de tipo 5");
			}
		} else
			tipoRes = tipo1;

		return tipoRes;
	}

	private Tipo expresion5() throws SintacticoException {

		InstruccionInterprete op;
		Tipo tipo = null;

		if ((op = op5asoc()) != null) {

			if ((tipo = expresion5()) != null) {
				if (!(op instanceof Negacion) || !(tipo instanceof TipoReal)) {
					sintactico.getCodigo().add(new CambioSigno());
				} else {
					// sintactico.setError(true);
					// GestorErrores.agregaError(11, sintactico.getLexico()
					// .getFila(), sintactico.getLexico().getColumna(),
					// "El tipo de la expresion debe ser un entero");
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
				// sintactico.setError(true);
				// GestorErrores.agregaError(11,
				// sintactico.getLexico().getFila(),
				// sintactico.getLexico().getColumna(),
				// "Se esperaba una expresion de tipo 6");
			}
		} else
			tipo = expresion6();

		return tipo;
	}

	private Tipo expresion6() throws SintacticoException {

		Tipo tipo = null;
		String lex = sintactico.getLexico().getLexema();

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_PARENTESIS_AP)) {
			if ((tipo = expresion()) != null) {
				if (!sintactico
						.reconoce(PalabrasReservadas.TOKEN_PARENTESIS_CE)) {
					// sintactico.setError(true);
					// GestorErrores.agregaError(11, sintactico.getLexico()
					// .getFila(), sintactico.getLexico().getColumna(),
					// "Falta parentesis de cierre");
				}
			} else {
				// sintactico.setError(true);
				// GestorErrores.agregaError(11,
				// sintactico.getLexico().getFila(),
				// sintactico.getLexico().getColumna(),
				// "Expresion mal formada");
			}
		} else if (sintactico.reconoce(PalabrasReservadas.TOKEN_INT)) {
			if (cast(lex, new TipoEntero())) {
				tipo = new TipoEntero();
				sintactico.getCodigo().add(
						new Apilar(new DatoPila(DatoPila.INT, lex)));
			} else {
				// sintactico.setError(true);
				// GestorErrores.agregaError(11,
				// sintactico.getLexico().getFila(),
				// sintactico.getLexico().getColumna(),
				// "No se puede parsear el valor a entero");
			}
		} else if (sintactico.reconoce(PalabrasReservadas.TOKEN_REAL)) {
			if (cast(lex, new TipoReal())) {
				tipo = new TipoReal();
				sintactico.getCodigo().add(
						new Apilar(new DatoPila(DatoPila.REAL, lex)));
			} else {
				// sintactico.setError(true);
				// GestorErrores.agregaError(11,
				// sintactico.getLexico().getFila(),
				// sintactico.getLexico().getColumna(),
				// "No se puede parsear el valor a real");
			}
		} else if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
			if (GestorTS.getInstancia().existeID(lex)) {
				tipo = GestorTS.getInstancia().getTipo(lex);
				sintactico.getCodigo().add(
						new ApilarDir(new DatoPila(DatoPila.INT, GestorTS
								.getInstancia().getDir(lex))));
			} else {
				// sintactico.setError(true);
				// GestorErrores.agregaError(11,
				// sintactico.getLexico().getFila(),
				// sintactico.getLexico().getColumna(),
				// "Variable no declarada");
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

	/*************** FUNCIONES SEMANTICAS *********************/

	private boolean validoOperacion(Tipo tipo1, Tipo tipo2) {
		// FIXME HACER!!!!
		return true;
	}

	@SuppressWarnings("serial")
	private class MiExcepcion extends Exception {

		public MiExcepcion(String mensaje) {
			super(mensaje);
		}

	}

}
