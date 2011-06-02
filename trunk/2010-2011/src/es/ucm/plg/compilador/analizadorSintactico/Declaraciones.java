package es.ucm.plg.compilador.analizadorSintactico;

import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
import es.ucm.plg.compilador.tablaSimbolos.Detalles.Clase;
import es.ucm.plg.compilador.tablaSimbolos.GestorTS;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoNull;
import es.ucm.plg.interprete.InterpreteExcepcion;

public class Declaraciones {

	private AnalizadorSintactico sintactico;
	private boolean finDecs;

	public Declaraciones(AnalizadorSintactico sintactico) {
		this.sintactico = sintactico;
		this.finDecs = false;
	}

	public boolean isFinDecs() {
		return finDecs;
	}

	public void setFinDecs(boolean finDecs) {
		this.finDecs = finDecs;
	}

	/*
	 * declaraciones ≡ declaracion declaracionesRE
	 */
	public void declaraciones() throws Exception {
		declaracion();
		declaracionesRE();
	}

	/*
	 * declaracionesRE ≡ declaracion declaracionesRE | vacio
	 */
	public void declaracionesRE() throws Exception {
		declaracion();
		if (!finDecs)
			declaracionesRE();
	}

	/*
	 * declaracion ≡ declaracionVar | declaracionFun | declaracionTipo
	 */
	public void declaracion() throws Exception {

		if (!declaracionVar() && !declaracionFun() && !declaracionTipo())
			finDecs = true;

	}

	/*
	 * declaracionvar ≡ desctipo id ;
	 */
	private boolean declaracionVar() throws SintacticoException {

		try {
			sintactico.getLexico().copiaEstado();

			// desctipo
			Tipo tipo = sintactico.getTipos().desctipo();

			if (tipo != null) {

				// id
				String id = sintactico.getLexico().getLexema();

				// error = falta_expresion
				if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {

					// error = existeID(desctipo.ts, id.lex) &&
					// desctipo.ts[id.lex].n == desctipo.n
					if (GestorTS.getInstancia().ts().existeID(id)) { // FIXME
																		// Añadir
						// la
						// comprobación
						// de niveles
						throw new MiExcepcion(
								SintacticoException.VARIABLE_DUPLICADA);
					}

					// error = falta_expresion
					if (!sintactico
							.reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA)) {
						throw new MiExcepcion(
								SintacticoException.FALTA_PUNTO_COMA);
					}

					// ts = añadeID(desctipo.ts, id.lex, <clase:var, dir:
					// desctipo.dirh, tipo: desctipo.tipo, nivel: desctipo.n>)
					GestorTS.getInstancia()
							.ts()
							.annadeID(id, sintactico.getDir(), tipo, Clase.var,
									GestorTS.getInstancia().getN());

					// dir = dir + desctipo.tipo.tam
					sintactico.setDir(sintactico.getDir() + tipo.getTamanyo());

					return true;

				} else {
					sintactico.getLexico().volverEstadoAnterior();
				}
			}

			return false;

			// error = falta_expresion && (existeID(desctipo.ts, id.lex) &&
			// desctipo.ts[id.lex].n == desctipo.n)
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/**
	 * declaracionfun := fun id ( listaparametros ) tiporeturn cuerpo end id ;
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean declaracionFun() throws Exception {

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_FUN)) {

				String id = sintactico.getLexico().getLexema();

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
					throw new MiExcepcion(SintacticoException.FALTA_ID);
				}

				// FIXME Añadir la comprobación de niveles
				if (GestorTS.getInstancia().existe(id)) {
					throw new MiExcepcion(
							SintacticoException.VARIABLE_DUPLICADA);
				}

				GestorTS.getInstancia().nuevoAmbito();

				if (!sintactico
						.reconoce(PalabrasReservadas.TOKEN_PARENTESIS_AP)) {
					throw new MiExcepcion(SintacticoException.FALTA_PARENTESIS);
				}

				parametros();

				if (!sintactico
						.reconoce(PalabrasReservadas.TOKEN_PARENTESIS_CE)) {
					throw new MiExcepcion(SintacticoException.FALTA_PARENTESIS);
				}

				Tipo tipo = tipoReturns();

				sintactico.getAcciones().cuerpo();

				if (tipo == null) {
					throw new MiExcepcion("Error en la sintaxis del return");
				}

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_END)) {
					throw new MiExcepcion(
							"Falta un end para terminar la funcion");
				}

				String id2 = sintactico.getLexico().getLexema();
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ID)
						|| !id.equals(id2)) {
					throw new MiExcepcion("Error al terminar la funcion");
				}

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA))
					throw new MiExcepcion(SintacticoException.FALTA_PUNTO_COMA);

				return true;

			}

			return false;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/**
	 * tiporeturn := returns desctipo | vacio
	 * 
	 * @throws SintacticoException
	 * 
	 * 
	 */

	public Tipo tipoReturns() throws SintacticoException {

		Tipo tipo = new TipoNull();

		try {

			if (sintactico.reconoce(PalabrasReservadas.TOKEN_RETURNS)) {

				tipo = sintactico.getTipos().desctipo();

				if (tipo == null) {
					throw new MiExcepcion("Falta un tipo tras el returns");
				}

			}

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipo;
	}

	/**
	 * parametros := parametro parametrosRE
	 * 
	 * @throws SintacticoException
	 * @throws InterpreteExcepcion
	 */
	public void parametros() throws SintacticoException, InterpreteExcepcion {

		Tipo tipo = parametro();

		if (tipo != null) {
			parametrosRE();
		}

	}

	/**
	 * parametrosRE := , parametro parametrosRE | vacio
	 * 
	 * @throws InterpreteExcepcion
	 * @throws SintacticoException
	 */
	public void parametrosRE() throws SintacticoException, InterpreteExcepcion {

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_COMA)) {

				Tipo tipo = parametro();

				if (tipo == null) {
					throw new MiExcepcion(SintacticoException.FALTA_PARAMETRO);
				}
				parametrosRE();

			}

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/**
	 * parametro := desctipo id | desctipo & id
	 * 
	 * @throws SintacticoException
	 * @throws InterpreteExcepcion
	 */
	public Tipo parametro() throws SintacticoException, InterpreteExcepcion {

		Tipo tipo = null;

		try {
			tipo = sintactico.getTipos().desctipo();
			Clase clase;

			if (tipo != null) {

				if (sintactico
						.reconoce(PalabrasReservadas.TOKEN_AMSPERSAND_VALOR)) {
					clase = Clase.ref;
				} else {
					clase = Clase.val;
				}

				String id = sintactico.getLexico().getLexema();
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
					throw new MiExcepcion(SintacticoException.FALTA_ID);
				}

				if (GestorTS.getInstancia().equals(id)) {
					throw new MiExcepcion(
							SintacticoException.VARIABLE_DUPLICADA);
				}

				GestorTS.getInstancia()
						.ts()
						.annadeID(id, sintactico.getDir(), tipo, clase,
								GestorTS.getInstancia().getN());
				sintactico.setDir(sintactico.getDir() + tipo.getTamanyo());

			}

			return tipo;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	private boolean declaracionTipo() throws SintacticoException {

		try {

			// tipo
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_TIPO)) {

				// deftipo
				Tipo tipo = sintactico.getTipos().defTipo();

				if (tipo == null) {
					throw new MiExcepcion(SintacticoException.FALTA_DESCTIPO);
				}

				// id
				String id = sintactico.getLexico().getLexema();

				// error = falta_expresion
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
					throw new MiExcepcion(SintacticoException.FALTA_ID);
				}

				// error = existeID(deftipo.ts, id.lex) deftipo.ts[id.lex].n ==
				// deftipo.n
				if (GestorTS.getInstancia().ts().existeID(id)) { // FIXME Añadir
																	// la
					// comprobación de
					// niveles
					throw new MiExcepcion(
							SintacticoException.VARIABLE_DUPLICADA);
				}

				// error = falta_expresion
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA)) {
					throw new MiExcepcion(SintacticoException.FALTA_PUNTO_COMA);
				}

				// ts = añadeID(deftipo.ts, id.lex, <clase:tipo, tipo:
				// deftipo.tipo, nivel: deftipo.n>)
				// FIXME Cambiar cuando se cambie la TS
				GestorTS.getInstancia()
						.ts()
						.annadeID(id, sintactico.getDir(), tipo, Clase.type,
								GestorTS.getInstancia().getN());
				if (sintactico.getPend().contains(id)) {
					sintactico.getPend().remove(id);
				}

				return true;
			} else {
				return false;
			}

			// error = falta_expresion && (existeID(deftipo.ts, id.lex)
			// deftipo.ts[id.lex].n == deftipo.n)
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	@SuppressWarnings("serial")
	private class MiExcepcion extends Exception {

		public MiExcepcion(String mensaje) {
			super(mensaje);
		}

	}

}
