package es.ucm.plg.compilador.analizadorSintactico;

import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
import es.ucm.plg.compilador.tablaSimbolos.Detalles.Clase;
import es.ucm.plg.compilador.tablaSimbolos.GestorTS;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;

public class Declaraciones {

	private AnalizadorSintactico sintactico;
	private boolean finDecs;

	public Declaraciones(AnalizadorSintactico sintactico) {
		this.sintactico = sintactico;
		this.finDecs = false;
	}

	/*
	 * declaraciones ≡ declaracion declaracionesRE
	 */
	public void declaraciones() throws SintacticoException {
		declaracion();
		declaracionesRE();
	}

	/*
	 * declaracionesRE ≡ declaracion declaracionesRE | vacio
	 */
	public void declaracionesRE() throws SintacticoException {
		declaracion();
		if (!finDecs)
			declaracionesRE();
	}

	/*
	 * declaracion ≡ declaracionVar | declaracionFun | declaracionTipo
	 */
	public void declaracion() throws SintacticoException {

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
					if (GestorTS.getInstancia().ts().existeID(id)) { // FIXME Añadir
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
					GestorTS.getInstancia().ts().annadeID(id, sintactico.getDir(),
							tipo, Clase.var, sintactico.getNivel());

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
	 * @throws SintacticoException
	 */
	private boolean declaracionFun() throws SintacticoException {

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
				
				

			}

			return false;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/*
	 * declaraciontipo ≡ tipo deftipo id ;
	 */
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
				if (GestorTS.getInstancia().ts().existeID(id)) { // FIXME Añadir la
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
				GestorTS.getInstancia().ts().annadeID(id, sintactico.getDir(), tipo,
						Clase.type, sintactico.getNivel());
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
