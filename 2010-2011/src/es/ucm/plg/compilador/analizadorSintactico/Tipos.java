package es.ucm.plg.compilador.analizadorSintactico;

import java.util.HashMap;

import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
import es.ucm.plg.compilador.tablaSimbolos.Detalles;
import es.ucm.plg.compilador.tablaSimbolos.Detalles.Clase;
import es.ucm.plg.compilador.tablaSimbolos.GestorTS;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Campo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoArray;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoEntero;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoPuntero;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoReal;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoRegistro;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;
import es.ucm.plg.interprete.instrucciones.Apilar;
import es.ucm.plg.interprete.instrucciones.ApilarDir;
import es.ucm.plg.interprete.instrucciones.ApilarInd;
import es.ucm.plg.interprete.instrucciones.DesapilarDir;
import es.ucm.plg.interprete.instrucciones.Multiplicar;
import es.ucm.plg.interprete.instrucciones.Sumar;

public class Tipos {

	private AnalizadorSintactico sintactico;
	private Expresiones expresiones;

	public Tipos(AnalizadorSintactico sintactico) {
		this.sintactico = sintactico;
		this.expresiones = new Expresiones(sintactico);
	}

	/**
	 * desctipo := id | int | real
	 * 
	 * @return
	 * @throws errorh
	 */
	public Tipo desctipo() throws SintacticoException {

		String id = sintactico.getLexico().getLexema();

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID))
			return new TipoPuntero(GestorTS.getInstancia().getTipo(id));

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_INT))
			return new TipoEntero();

		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_REAL))
			return new TipoReal();

		else
			return null;

	}

	/**
	 * deftipo := array | registro | puntero
	 * 
	 * @return
	 * @throws errorh
	 */
	public Tipo defTipo() throws SintacticoException {

		Tipo tipoResult = null;

		tipoResult = array();

		if (tipoResult == null)
			tipoResult = puntero();

		if (tipoResult == null)
			tipoResult = registro();

		return tipoResult;
	}

	/**
	 * registro := rec campos endrec
	 * 
	 * @return
	 * @throws errorh
	 *             | (campos.size = 0) | falta_expresion
	 */
	public Tipo registro() throws SintacticoException {

		Tipo tipoResult = null;

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_REC)) {

				HashMap<String, Campo> campos = campos();

				if (campos.size() == 0) {
					throw new MiExcepcion(
							"Debe definir al menos un campo en el registro");
				}

				if (sintactico.reconoce(PalabrasReservadas.TOKEN_ENDREC)) {
					throw new MiExcepcion(
							"Falta ENDREC al final de la declaración del registro");
				}

				tipoResult = new TipoRegistro(campos);

			}

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipoResult;
	}

	/**
	 * array := desctipo [ litInt ]
	 * 
	 * @return
	 * @throws errorh
	 *             | cast(desctipo.ts, litInt,<t:int,tam:1>) | sintactico.referenciaErronea
	 *             && falta_expresion
	 */
	public Tipo array() throws SintacticoException {

		Tipo tipoResult = null;
		Tipo tipoBase = null;

		tipoBase = desctipo();

		try {

			if (tipoBase != null) {

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_CORCHETE_AB)) {
					throw new MiExcepcion(SintacticoException.FALTA_CORCHETE);
				}

				int num = Integer.parseInt(sintactico.getLexico().getLexema());

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_INT)) {
					throw new MiExcepcion("Se esperaba un entero");
				}

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_CORCHETE_CE)) {
					throw new MiExcepcion(SintacticoException.FALTA_CORCHETE);
				}

				if (sintactico.referenciaErronea()) {
					throw new MiExcepcion(
							SintacticoException.REFERENCIA_ERRONEA);
				}

				tipoResult = new TipoArray(tipoBase, num);

			}

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipoResult;

	}

	/**
	 * puntero := pointer desctipo
	 * 
	 * @return
	 * @throws errorh
	 *             | sintactico.referenciaErronea | falta_expresion
	 */
	public Tipo puntero() throws SintacticoException {

		Tipo tipoResult = null;
		Tipo tipoBase = null;

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_POINTER)) {

				tipoBase = desctipo();

				if (tipoBase == null)
					throw new MiExcepcion(SintacticoException.FALTA_DESCTIPO);

				if (sintactico.referenciaErronea()) {
					throw new MiExcepcion(
							SintacticoException.REFERENCIA_ERRONEA);
				}

				tipoResult = new TipoPuntero(tipoBase);

			}

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipoResult;
	}

	/**
	 * campos := campo camposRE
	 * 
	 * @return
	 * @throws errorh
	 *             | (campos.size = 0)
	 */
	public HashMap<String, Campo> campos() throws SintacticoException {

		HashMap<String, Campo> campos = new HashMap<String, Campo>();
		Campo campo = null;

		try {
			campo = campo();

			if (campo == null) {
				throw new MiExcepcion(
						"No se puede declarar un registro sin campos");
			}

			campos.put(campo.getNombre(), campo);
			camposRE(campos);

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return campos;
	}

	/**
	 * campo := desctipo id
	 * 
	 * @return
	 * @throws errorh
	 *             | falta_expresion
	 */
	private Campo campo() throws SintacticoException {

		Tipo tipoBase = null;
		String id = null;
		Campo campo = null;

		try {

			tipoBase = desctipo();

			if (tipoBase == null) {
				throw new MiExcepcion(SintacticoException.FALTA_DESCTIPO);
			}

			id = sintactico.getLexico().getLexema();

			if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
				throw new MiExcepcion(SintacticoException.FALTA_ID);
			}

			campo = new Campo(tipoBase, id, 0);

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return campo;
	}

	/**
	 * camposRE := , campo camposRE | vacio
	 * 
	 * @param campos
	 * @throws errorh
	 *             | falta_expresion
	 */
	private void camposRE(HashMap<String, Campo> campos)
			throws SintacticoException {

		Campo campo = null;

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_COMA)) {

				campo = campo();

				if (campo == null) {
					throw new MiExcepcion(
							"Se esperaba un campo después de la coma");
				}

				campo.setDesplazamiento(campos.size());
				campos.put(campo.getNombre(), campo);

				camposRE(campos);
			}

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/**
	 * mem := id memRE
	 * 
	 * @return
	 * @throws InterpreteExcepcion 
	 * @throws !existeID(mem.tsh, id.lex) | mem.tsh[id.lex].clase == var |
	 *         sintactico.referenciaErronea
	 */
	public Tipo mem() throws SintacticoException, InterpreteExcepcion {

		String id = sintactico.getLexico().getLexema();
		Tipo tipo = null;

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {

				if (!(GestorTS.getInstancia().existeID(id) && GestorTS
						.getInstancia().getDetalles(id).getClase() == Clase.var)) {
					throw new MiExcepcion(
							SintacticoException.VARIABLE_NO_DECLARADA);
				}

				if (sintactico.referenciaErronea()) {
					throw new MiExcepcion(
							SintacticoException.REFERENCIA_ERRONEA);
				}

				sintactico.accesoVar(id);

				tipo = memRE(GestorTS.getInstancia().getTipo(id));

				sintactico.getCodigo().add(new DesapilarDir());
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
	 * memRE := [ expresion2 ] memRE | .id memRE | ^ memRE | vacio
	 * 
	 * @param tipoh
	 * @return
	 * @throws InterpreteExcepcion 
	 * @throws errorh
	 */
	private Tipo memRE(Tipo tipoh) throws SintacticoException, InterpreteExcepcion {

		// [ expresion2 ] memRE
		if (tipoh instanceof TipoArray) {
			return memREArray(tipoh);
		}
		// .id memRE
		else if (tipoh instanceof TipoRegistro) {
			return memRERecord(tipoh);
		}
		// ^ memRE
		else if (tipoh instanceof TipoPuntero) {
			return memREPuntero(tipoh);
		} else {
			return tipoh;
		}
	}

	/**
	 * memREArray := [ expresion2 ] memRE
	 * 
	 * @param tipoh
	 * @return
	 * @throws InterpreteExcepcion 
	 * @throws errorH
	 *             | (expresion2.tipo = <t:int,tam:1>)
	 */
	private Tipo memREArray(Tipo tipoh) throws SintacticoException, InterpreteExcepcion {
		Tipo tipo = null;

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_CORCHETE_AB)) {

				Tipo tipoNum = expresiones.expresion2();

				if ((tipoNum == null) | !(tipoNum instanceof TipoEntero))
					throw new MiExcepcion(
							SintacticoException.EXPRESION_INVALIDA);

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_CORCHETE_CE)) {
					throw new MiExcepcion(SintacticoException.FALTA_CORCHETE);
				}

				tipo = ((TipoArray) tipoh).getTipoBase();

				sintactico.getCodigo().add(
						new Apilar(
								new DatoPila(DatoPila.INT, tipo.getTamanyo())));
				sintactico.getCodigo().add(new Multiplicar());
				sintactico.getCodigo().add(new Sumar());
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 3);

			}

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipo;
	}

	/**
	 * memRERecord := .id memRE
	 * 
	 * @param tipoh
	 * @return
	 * @throws InterpreteExcepcion 
	 * @throws errorh
	 *             | !existeCampo(memRE1.camposh,id.lex) | sintactico.referenciaErronea |
	 *             falta_expresion
	 */
	private Tipo memRERecord(Tipo tipoh) throws SintacticoException, InterpreteExcepcion {

		String id = null;
		Tipo tipo = null;

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO)) {

				id = sintactico.getLexico().getLexema();

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
					throw new MiExcepcion(SintacticoException.FALTA_ID);
				}

				if (!((TipoRegistro) tipoh).getCampos().keySet().contains(id)) {
					throw new MiExcepcion(
							"No existe ningún campo con ese identificador");
				}

				if (sintactico.referenciaErronea()) {
					throw new MiExcepcion(
							SintacticoException.REFERENCIA_ERRONEA);
				}

				tipo = ((TipoRegistro) tipoh).getCampos().get(id).getTipoBase();

				sintactico.getCodigo().add(
						new Apilar(new DatoPila(DatoPila.INT,
								((TipoRegistro) tipoh).getCampos().get(id)
										.getDesplazamiento())));
				sintactico.getCodigo().add(new Sumar());
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 2);

				tipo = memRE(tipo);

			}

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipo;
	}

	/**
	 * memREPuntero := ^ memRE
	 * 
	 * @param tipoh
	 * @return
	 * @throws InterpreteExcepcion 
	 * @throws sintactico.referenciaErronea
	 */
	private Tipo memREPuntero(Tipo tipoh) throws SintacticoException, InterpreteExcepcion {

		Tipo tipo = null;

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTERO_FLECHA)) {

				if (sintactico.referenciaErronea()) {
					throw new MiExcepcion(
							SintacticoException.REFERENCIA_ERRONEA);
				}

				sintactico.getCodigo().add(new ApilarInd());
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);

				memRE(tipo);

			}

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
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
