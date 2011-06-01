package es.ucm.plg.compilador.analizadorSintactico;

import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
import es.ucm.plg.compilador.tablaSimbolos.GestorTS;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoEntero;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoNull;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoReal;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;
import es.ucm.plg.interprete.instrucciones.Apilar;
import es.ucm.plg.interprete.instrucciones.ApilarInd;
import es.ucm.plg.interprete.instrucciones.CambioSigno;
import es.ucm.plg.interprete.instrucciones.CastInt;
import es.ucm.plg.interprete.instrucciones.CastReal;
import es.ucm.plg.interprete.instrucciones.Desapilar;
import es.ucm.plg.interprete.instrucciones.DesapilarInd;
import es.ucm.plg.interprete.instrucciones.Distinto;
import es.ucm.plg.interprete.instrucciones.Dividir;
import es.ucm.plg.interprete.instrucciones.Entrada;
import es.ucm.plg.interprete.instrucciones.Igual;
import es.ucm.plg.interprete.instrucciones.IrF;
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

	public AnalizadorSintactico sintactico;

	public Expresiones(AnalizadorSintactico sintactico) {
		this.sintactico = sintactico;
	}

	/**
	 * expresion := expresionMem || expresionIn || expresionOut || expresion1
	 * 
	 * @return Tipo
	 * @throws errorh
	 */
	public Tipo expresion() throws SintacticoException, InterpreteExcepcion {

		Tipo tipo = null;

		// expresionMem
		// tipo = expresionMem();

		// expresionIn
		if (tipo == null) {
			tipo = expresionIn();
		}

		// expresionOut
		if (tipo == null) {
			tipo = expresionOut();
		}

		// expresion1
		if (tipo == null) {
			tipo = expresion1();
		}

		return tipo;

	}

	// /**
	// * expresionMem := mem = expresion2 || expresion2
	// *
	// * @return Tipo
	// * @throws errorh
	// * || !compatibles(expresion2.ts,mem.id,expresion2.tipo) ||
	// * expresion_invalida
	// */
	// public Tipo expresionMem() throws SintacticoException,
	// InterpreteExcepcion {
	//
	// Tipo tipo1 = null;
	// Tipo tipo2 = null;
	//
	// try {
	//
	// // mem
	// String id = sintactico.getLexico().getLexema();
	// tipo1 = sintactico.getTipos().mem();
	//
	// if (tipo1 != null) {
	//
	// // =
	// if (!sintactico.reconoce(PalabrasReservadas.TOKEN_IGUAL)) {
	// throw new MiExcepcion(
	// SintacticoException.EXPRESION_INVALIDA);
	// }
	//
	// // expresion2
	// tipo2 = expresion2();
	//
	// if (!tipo1.equals(tipo2)) {
	// throw new MiExcepcion(SintacticoException.TIPO_INCOMPATIBLE);
	// }
	//
	// sintactico.getCodigo().add(
	// new Mueve(new DatoPila(DatoPila.INT, GestorTS
	// .getInstancia().getDir(id))));
	// sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
	//
	// }
	//
	// return tipo1;
	//
	// } catch (MiExcepcion ex) {
	// throw new SintacticoException(ex.getMessage(), sintactico
	// .getLexico().getLexema(), sintactico.getLexico().getFila(),
	// sintactico.getLexico().getColumna());
	// }
	//
	// }

	/**
	 * expresionIn := in id
	 * 
	 * @return Tipo
	 * @throws errorh
	 *             || expresion_invalida || ¬existeID(op0in.ts, id.lex)
	 */
	public Tipo expresionIn() throws SintacticoException {

		Tipo tipo = null;

		try {

			if (sintactico.reconoce(PalabrasReservadas.TOKEN_IN)) {

				String id = sintactico.getLexico().getLexema();

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
					throw new MiExcepcion(SintacticoException.FALTA_ID);
				}

				if (!GestorTS.getInstancia().existeID(id)) {
					throw new MiExcepcion(
							SintacticoException.VARIABLE_NO_DECLARADA);
				}

				sintactico.getCodigo().add(
						new Entrada(new DatoPila(DatoPila.INT, GestorTS
								.getInstancia().getDir(id))));
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);

				tipo = GestorTS.getInstancia().getTipo(id);

			}

			return tipo;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/**
	 * expresionOut := out expresion1
	 * 
	 * @return Tipo
	 * @throws InterpreteExcepcion
	 * @throws errorh
	 *             || expresion_invalida || ¬existeID(op0in.ts, id.lex)
	 */
	public Tipo expresionOut() throws SintacticoException, InterpreteExcepcion {

		Tipo tipo = null;

		try {
			// out
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_OUT)) {

				// expresion1
				tipo = expresion1();

				if (tipo == null) {
					throw new MiExcepcion(
							SintacticoException.EXPRESION_INVALIDA);
				}

				sintactico.getCodigo().add(new Salida());
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
			}

			return tipo;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	/**
	 * expresion1 := mem = expresion2
	 * 
	 * @return Tipo
	 * @throws InterpreteExcepcion
	 * @throws errorh
	 *             || expresion_invalida || tipo_incompatible
	 */
	public Tipo expresion1() throws SintacticoException, InterpreteExcepcion {

		Tipo tipo1 = null;
		Tipo tipo2 = null;

		try {
			tipo1 = reconoceAsignacion();

			if (tipo1 != null) {

				tipo2 = expresion2();

				if (tipo2 == null) {
					throw new MiExcepcion(
							SintacticoException.EXPRESION_INVALIDA);
				}
				if (!sintactico.getTipos().compatibles(tipo1, tipo2)) {
					throw new MiExcepcion(SintacticoException.TIPO_INCOMPATIBLE);
				}

				sintactico.getCodigo().add(new DesapilarInd());
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);

			} else {
				tipo1 = expresion2();
			}

			return tipo1;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/**
	 * expresion2 := expresion3 op2 expresion3 | expresion3
	 * 
	 * @return Tipo
	 * @throws InterpreteExcepcion
	 * @throws errorh
	 *             || !validoOperacion(expresion31.ts, expresion30.tipo, op2.op,
	 *             expresion31.tipo) || expresion_invalida
	 */
	public Tipo expresion2() throws SintacticoException, InterpreteExcepcion {

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

					if ((tipo1 instanceof TipoReal && tipo2 instanceof TipoEntero)
							|| (tipo2 instanceof TipoReal && tipo1 instanceof TipoEntero)) {
						throw new MiExcepcion(
								SintacticoException.TIPO_INCOMPATIBLE);
					}

					sintactico.getCodigo().add(op);
					sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);

					// tipo = <t:int>
					tipo = new TipoEntero();

				} else {
					tipo = tipo1;
				}
			}

			return tipo;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	/**
	 * expresion3 = expresion4 expresion3RE
	 * 
	 * @return Tipo
	 * @throws InterpreteExcepcion
	 * @throws errorh
	 *             || expresion_incorrecta || ¬existeID(op0in.ts, id.lex)
	 */
	public Tipo expresion3() throws SintacticoException, InterpreteExcepcion {

		Tipo tipo = null;

		// try {
		// expresion4
		tipo = expresion4();

		// expresion3RE
		if ((tipo != null) && !(tipo instanceof TipoNull)) {
			tipo = expresion3RE(tipo);
		}

		return tipo;

	}

	/**
	 * expresion3RE := op3 expresion4 expresion3RE | vacio
	 * 
	 * @param tipo1
	 *            resultante de la parte anterior de la expresion
	 * @return tipo
	 * @throws InterpreteExcepcion
	 * @throws error
	 *             || falta_expresion || !existeID(op0in.ts, id.lex)
	 */
	public Tipo expresion3RE(Tipo tipo1) throws SintacticoException,
			InterpreteExcepcion {

		InstruccionInterprete op;
		Tipo tipo = tipo1;
		Tipo tipo2 = null;

		try {
			// op3
			op = op3();

			if (op != null) {

				// expresion4
				tipo2 = expresion4();
				
				if (tipo2 == null) {
					throw new MiExcepcion(
							SintacticoException.EXPRESION_INVALIDA);
				}

				if (!(tipo2 instanceof TipoReal || tipo2 instanceof TipoEntero)) {
					throw new MiExcepcion(SintacticoException.TIPO_INCOMPATIBLE);
				}

				if ((op instanceof Sumar) || (op instanceof Restar)) {
					if ((tipo1 instanceof TipoEntero)
							&& (tipo2 instanceof TipoEntero)) {
						tipo = new TipoEntero();
					} else {
						tipo = new TipoReal();
					}
				} else if (op instanceof O_Logica) {
					tipo = new TipoEntero();
				}

				// expresion3RE
				tipo = expresion3RE(tipo);

				sintactico.getCodigo().add(op);
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
			}

			return tipo;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	/**
	 * expresion4 := expresion5 expresion4RE
	 * 
	 * @return Tipo
	 * @throws InterpreteExcepcion
	 * @throws errorh
	 */
	public Tipo expresion4() throws SintacticoException, InterpreteExcepcion {

		// expresion5
		Tipo tipo = expresion5();

		// expresion4RE
		if (tipo != null) {
			tipo = expresion4RE(tipo);
		}

		return tipo;

	}

	/**
	 * expresion4RE := op4 expresion5 expresion4RE | vacio
	 * 
	 * @param tipo1
	 *            resultante de la parte anterior de la expresion
	 * @return tipo
	 * @throws InterpreteExcepcion
	 * @throws errorh
	 *             || validoOperacion(expresion5.ts, expresion4RE0.tipo, op4.op,
	 *             expresion5.tipo)
	 */
	public Tipo expresion4RE(Tipo tipo1) throws SintacticoException,
			InterpreteExcepcion {

		InstruccionInterprete op;
		Tipo tipo = tipo1;
		Tipo tipo2, tipo3;

		try {
			// op4
			op = op4();

			if (op != null) {

				int parche = 0;				
				
				if (op instanceof Y_Logica) {
					parche = sintactico.getCodigo().size();
					sintactico.getCodigo().add(null);
					sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
				}
				
				// expresion5
				tipo2 = expresion5();

				if ((tipo2 == null)
						|| !((tipo2 instanceof TipoReal) || (tipo2 instanceof TipoEntero))) {
					throw new MiExcepcion(SintacticoException.TIPO_INCOMPATIBLE);
				}

				// expresion4RE
				tipo3 = expresion4RE(tipo2);

				if (op instanceof Y_Logica || op instanceof Modulo) {
					tipo = new TipoEntero();
				} else if (tipo1 instanceof TipoEntero
						&& tipo3 instanceof TipoEntero) {
					tipo = new TipoEntero();
				} else {
					tipo = tipo2;
				}

				if (op instanceof Y_Logica) {
					sintactico.getCodigo().set(
							parche,
							new IrF(new DatoPila(DatoPila.INT, sintactico
									.getEtiqueta() + 1)));					
				}else{
					sintactico.getCodigo().add(op);
					sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
				}
			}

			return tipo;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/**
	 * expresion5 := op5asoc expresion5 || op5noasoc expresion6 || expresion6
	 * 
	 * @return Tipo
	 * @throws InterpreteExcepcion
	 * @throws errorh
	 *             || !validoOperacion(ts, tipo, op, NULL)
	 */
	public Tipo expresion5() throws SintacticoException, InterpreteExcepcion {

		InstruccionInterprete op;
		Tipo tipo = null;

		try {

			if ((op = op5asoc()) != null) {

				tipo = expresion5();

				if (op instanceof Negacion) {
					if (!(tipo instanceof TipoEntero)) {
						throw new MiExcepcion(
								SintacticoException.TIPO_INCOMPATIBLE);
					}

					sintactico.getCodigo().add(new Negacion());
					sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);

				} else if (op instanceof CambioSigno) {
					if (!(tipo instanceof TipoReal || tipo instanceof TipoEntero)) {
						throw new MiExcepcion(
								SintacticoException.TIPO_INCOMPATIBLE);
					}

					sintactico.getCodigo().add(new CambioSigno());
					sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);

				}

			} else if ((op = op5noAsoc()) != null) {

				tipo = expresion6();

				if (!(tipo instanceof TipoReal || tipo instanceof TipoEntero)) {
					throw new MiExcepcion(SintacticoException.TIPO_INCOMPATIBLE);
				}

				if (op instanceof CastInt) {
					sintactico.getCodigo().add(new CastInt());
					sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
					tipo = new TipoEntero();
				} else {
					sintactico.getCodigo().add(new CastReal());
					sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
					tipo = new TipoReal();
				}

			} else
				tipo = expresion6();

			return tipo;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	/**
	 * expresion6 := (expresion) || litInt || litReal || mem
	 * 
	 * @return Tipo
	 * @throws InterpreteExcepcion
	 * @throws errorh
	 *             || !validoOperacion(ts, tipo, op, NULL)
	 */
	public Tipo expresion6() throws SintacticoException, InterpreteExcepcion {

		Tipo tipo = null;
		String lex = sintactico.getLexico().getLexema();

		try {
			tipo = sintactico.getTipos().mem();

			if (tipo != null) {
				sintactico.getCodigo().add(new ApilarInd());
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
			} else {
				if (sintactico.reconoce(PalabrasReservadas.TOKEN_PARENTESIS_AP)) {
					tipo = expresion2();
					if (tipo == null) {
						throw new MiExcepcion(
								SintacticoException.TIPO_INCOMPATIBLE);
					}

					if (!sintactico
							.reconoce(PalabrasReservadas.TOKEN_PARENTESIS_CE)) {
						throw new MiExcepcion(
								SintacticoException.TIPO_INCOMPATIBLE);
					}

				}
				if (sintactico.reconoce(PalabrasReservadas.TOKEN_INT)) {

					if (cast(lex, new TipoEntero())) {
						tipo = new TipoEntero();
						sintactico.getCodigo().add(
								new Apilar(new DatoPila(DatoPila.INT, lex)));
						sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
					} else {
						throw new MiExcepcion(
								SintacticoException.TIPO_INCOMPATIBLE);
					}
				}
				if (sintactico.reconoce(PalabrasReservadas.TOKEN_REAL)) {
					if (cast(lex, new TipoReal())) {
						tipo = new TipoReal();
						sintactico.getCodigo().add(
								new Apilar(new DatoPila(DatoPila.REAL, lex)));
						sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
					} else {
						throw new MiExcepcion(
								SintacticoException.TIPO_INCOMPATIBLE);
					}

				}
				if (sintactico.reconoce(PalabrasReservadas.TOKEN_NULL)) {
					tipo = new TipoNull();
					sintactico.getCodigo().add(
							new Apilar(new DatoPila(DatoPila.REAL,
									Integer.MIN_VALUE)));
					sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
				}
			}

			return tipo;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/**
	 * Funcion semantica que comprueba si se puede realizar una conversion de un
	 * valor a un tipo
	 * 
	 * @param valor
	 *            que deseo convertir
	 * @param tipo
	 *            al que quiero convertir el valor
	 * @return True si la conversion se puede realizar, false en caso contrario
	 */
	public boolean cast(String valor, Tipo tipo) {

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

	/**
	 * op2 := < || > || <= || >= || == || !=
	 * 
	 * @return Instruccion en codigo maquina resultante
	 */
	public InstruccionInterprete op2() {
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

	/**
	 * op3 := \|\| || + || -
	 * 
	 * @return Instruccion en codigo maquina resultante
	 */
	public InstruccionInterprete op3() {
		if (sintactico.reconoce(PalabrasReservadas.TOKEN_O_LOGICA))
			return new O_Logica();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_SUMA))
			return new Sumar();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_RESTA))
			return new Restar();
		else
			return null;
	}

	/**
	 * op4 := * || / || % || &&
	 * 
	 * @return Instruccion en codigo maquina resultante
	 */
	public InstruccionInterprete op4() {
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

	/**
	 * op5asoc := - || !
	 * 
	 * @return Instruccion en codigo maquina resultante
	 */
	public InstruccionInterprete op5asoc() {

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_RESTA))
			return new CambioSigno();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_NEGACION))
			return new Negacion();
		else
			return null;
	}

	/**
	 * op5noAsoc := (int) || (real)
	 * 
	 * @return Instruccion en codigo maquina resultante
	 */
	public InstruccionInterprete op5noAsoc() {

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_CAST_INT))
			return new CastInt();
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_CAST_REAL))
			return new CastReal();
		else
			return null;
	}

	private Tipo reconoceAsignacion() throws SintacticoException,
			InterpreteExcepcion {

		sintactico.getLexico().copiaEstado();
		Tipo tipo = sintactico.getTipos().mem();

		if (tipo != null) {
			if (sintactico.getLexico().getToken_actual()
					.equals(PalabrasReservadas.TOKEN_ASIGNACION)) {
				sintactico.getLexico().scanner();
			} else {
				sintactico.getLexico().volverEstadoAnterior();
				sintactico.getCodigo().add(new Desapilar());
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
				tipo = null;
			}
		}
		return tipo;
	}

	@SuppressWarnings("serial")
	private class MiExcepcion extends Exception {

		public MiExcepcion(String mensaje) {
			super(mensaje);
		}

	}

}
