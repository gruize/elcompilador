package es.ucm.plg.compilador.analizadorSintactico;

import java.util.HashMap;
import java.util.Iterator;

import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
import es.ucm.plg.compilador.tablaSimbolos.Detalles.Clase;
import es.ucm.plg.compilador.tablaSimbolos.GestorTS;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Campo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoArray;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoEntero;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoNull;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoPuntero;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoReal;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoRegistro;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;
import es.ucm.plg.interprete.instrucciones.Apilar;
import es.ucm.plg.interprete.instrucciones.ApilarInd;
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
	 * @return tipo
	 * @throws errorh
	 */
	public Tipo desctipo() throws SintacticoException {

		String id = sintactico.getLexico().getLexema();

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
			if (GestorTS.getInstancia().ts().existeID(id)
					&& (GestorTS.getInstancia().ts().getDetalles(id).getClase() == Clase.type)) {
				return GestorTS.getInstancia().ts().getTipo(id);
			} else {
				sintactico.getPend().add(id);
				return new TipoPuntero(null);
			}
		}

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
	 * @return tipoResult
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
	 * registro := rec campos endrec id
	 * 
	 * @return tipoResult
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

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ENDREC)) {
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
	 * @return tipoResult
	 * @throws errorh
	 *             | cast(desctipo.ts, litInt,<t:int,tam:1>) |
	 *             sintactico.referenciaErronea && falta_expresion
	 */
	public Tipo array() throws SintacticoException {

		Tipo tipoResult = null;
		Tipo tipoBase = null;

		tipoBase = desctipo();

		try {

			if (tipoBase != null) {

				sintactico.getLexico().copiaEstado();
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_CORCHETE_AB)) {
					sintactico.getLexico().volverEstadoAnterior();
					return null;
				}

				int num = Integer.parseInt(sintactico.getLexico().getLexema());

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_INT)) {
					throw new MiExcepcion("Se esperaba un entero");
				}

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_CORCHETE_CE)) {
					throw new MiExcepcion(SintacticoException.FALTA_CORCHETE);
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
	 * puntero := pointer desctipo id
	 * 
	 * @return tipoResult
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
	 * @return campos
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

			campo.setDesplazamiento(0);
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

			if (tipoBase != null) {

				id = sintactico.getLexico().getLexema();

				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
					throw new MiExcepcion(SintacticoException.FALTA_ID);
				}

				campo = new Campo(tipoBase, id, 0);
			}

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
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA)) {

				campo = campo();
				
				if (campo != null) {

				Iterator<Campo> it = campos.values().iterator();
				Campo ultCampo = null;
				while (it.hasNext())
					ultCampo = it.next();

				if (ultCampo == null) {
					throw new MiExcepcion("Error en la obtencion de campos");
				}

				campo.setDesplazamiento(ultCampo.getDesplazamiento()
						+ ultCampo.getTipoBase().getTamanyo());
				campos.put(campo.getNombre(), campo);

				camposRE(campos);
				}
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
	 * @return tipo
	 * @throws InterpreteExcepcion
	 * @throws !existeID(mem.tsh, id.lex) | mem.tsh[id.lex].clase == var |
	 *         sintactico.referenciaErronea
	 */
	public Tipo mem() throws SintacticoException, InterpreteExcepcion {

		String id = sintactico.getLexico().getLexema();
		Tipo tipo = null;

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {

				if (!(GestorTS.getInstancia().ts().existeID(id) && GestorTS
						.getInstancia().ts().getDetalles(id).getClase() == Clase.var)) {
					throw new MiExcepcion(
							SintacticoException.VARIABLE_NO_DECLARADA);
				}

				sintactico.getCodigo().add(
						new Apilar(new DatoPila(DatoPila.INT, GestorTS
								.getInstancia().ts().getDir(id))));
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);

				tipo = memRE(GestorTS.getInstancia().ts().getTipo(id));

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
	private Tipo memRE(Tipo tipoh) throws SintacticoException,
			InterpreteExcepcion {

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
	private Tipo memREArray(Tipo tipoh) throws SintacticoException,
			InterpreteExcepcion {
		Tipo tipo = tipoh;

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
	 *             | !existeCampo(memRE1.camposh,id.lex) |
	 *             sintactico.referenciaErronea | falta_expresion
	 */
	private Tipo memRERecord(Tipo tipoh) throws SintacticoException,
			InterpreteExcepcion {

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
	private Tipo memREPuntero(Tipo tipoh) throws SintacticoException,
			InterpreteExcepcion {

		Tipo tipo = tipoh;

			if (sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTERO_FLECHA)) {

				sintactico.getCodigo().add(new ApilarInd());
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);

				memRE(tipo);

			}

		return tipo;
	}
	
	public boolean compatibles (Tipo tipo1, Tipo tipo2) {
		if (tipo1.equals(tipo2)) {
			return true;
		}
		else if (tipo1 instanceof TipoReal && tipo2 instanceof TipoEntero){
			return true;
		}
		else if (tipo1 instanceof TipoPuntero && tipo2 instanceof TipoEntero) {
			return true;
		}
		else if (tipo1 instanceof TipoPuntero && ((TipoPuntero) tipo1).getTipoBase().equals(tipo2)) {
			return true;
		}
		else if (tipo1 instanceof TipoPuntero && tipo2 instanceof TipoNull) {
			return true;
		}
		else {
			return false;
		}
	}

	@SuppressWarnings("serial")
	private class MiExcepcion extends Exception {

		public MiExcepcion(String mensaje) {
			super(mensaje);
		}
	}
}
