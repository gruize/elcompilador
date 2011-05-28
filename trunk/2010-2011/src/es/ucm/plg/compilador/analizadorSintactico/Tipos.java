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
import es.ucm.plg.interprete.datoPila.DatoPila;
import es.ucm.plg.interprete.instrucciones.Apilar;
import es.ucm.plg.interprete.instrucciones.ApilarDir;
import es.ucm.plg.interprete.instrucciones.Multiplicar;
import es.ucm.plg.interprete.instrucciones.Sumar;

public class Tipos {

	private AnalizadorSintactico sintactico;
	private Expresiones expresiones;

	public Tipos(AnalizadorSintactico sintactico) {
		this.sintactico = sintactico;
		this.expresiones = new Expresiones(sintactico);
	}

	/*
	 * desctipo ≡ id|int|real
	 */
	public Tipo desctipo() throws SintacticoException {

		String id = sintactico.getLexico().getLexema();

		// id
		if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID))
			return new TipoPuntero(GestorTS.getInstancia().getTipo(id));

		// int
		if (sintactico.reconoce(PalabrasReservadas.TOKEN_INT))
			return new TipoEntero();

		// real
		else if (sintactico.reconoce(PalabrasReservadas.TOKEN_REAL))
			return new TipoReal();

		else
			return null;

	}

	/*
	 * deftipo ≡ array | registro | puntero
	 */
	public Tipo defTipo() throws SintacticoException {

		Tipo tipoResult = null;

		// array
		tipoResult = array();

		// registro
		if (tipoResult == null)
			tipoResult = puntero();

		// puntero
		if (tipoResult == null)
			tipoResult = registro();

		return tipoResult;
	}

	/*
	 * registro ≡ rec campos endrec
	 */
	public Tipo registro() throws SintacticoException {

		Tipo tipoResult = null;

		try {
			// rec
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_REC)) {

				// campos
				HashMap<String, Campo> campos = campos();

				if (campos.size() == 0) {
					throw new MiExcepcion(
							"Debe definir al menos un campo en el registro");
				}

				// endrec
				if (sintactico.reconoce(PalabrasReservadas.TOKEN_ENDREC)) {
					throw new MiExcepcion(
							"Falta ENDREC al final de la declaración del registro");
				}

				tipoResult = new TipoRegistro(campos);

			}

			// error = (campos.size = 0) && falta_expresion
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipoResult;
	}

	/*
	 * array ≡ desctipo [ litInt ]
	 */
	public Tipo array() throws SintacticoException {

		Tipo tipoResult = null;
		Tipo tipoBase = null;

		// desctipo
		tipoBase = desctipo();

		try {

			if (tipoBase != null) {

				// [
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_CORCHETE_AB)) {
					throw new MiExcepcion(SintacticoException.FALTA_CORCHETE);
				}

				// litInt
				int num = Integer.parseInt(sintactico.getLexico().getLexema());

				// error = cast(desctipo.ts, litInt,<t:int,tam:1>)
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_INT)) {
					throw new MiExcepcion("Se esperaba un entero");
				}

				// ]
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_CORCHETE_CE)) {
					throw new MiExcepcion(SintacticoException.FALTA_CORCHETE);
				}

				// error = referenciaErronea
				if (referenciaErronea()) {
					throw new MiExcepcion(
							SintacticoException.REFERENCIA_ERRONEA);
				}

				tipoResult = new TipoArray(tipoBase, num);

			}

			// error = cast(desctipo.ts, litInt,<t:int,tam:1>) &&
			// referenciaErronea && falta_expresion
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipoResult;

	}

	/*
	 * puntero ≡ pointer desctipo
	 */
	public Tipo puntero() throws SintacticoException {

		Tipo tipoResult = null;
		Tipo tipoBase = null;

		try {
			// pointer
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_POINTER)) {

				// desctipo
				tipoBase = desctipo();

				// error = falta_expresion
				if (tipoBase == null)
					throw new MiExcepcion(SintacticoException.FALTA_DESCTIPO);

				// error = referenciaErronea
				if (referenciaErronea()) {
					throw new MiExcepcion(
							SintacticoException.REFERENCIA_ERRONEA);
				}

				tipoResult = new TipoPuntero(tipoBase);

			}

			// error = referenciaErronea && falta_expresion
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipoResult;
	}

	/*
	 * campos ≡ campo camposRE
	 */
	public HashMap<String, Campo> campos() throws SintacticoException {

		HashMap<String, Campo> campos = new HashMap<String, Campo>();
		Campo campo = null;

		try {
			// campo
			campo = campo();

			// error = (campos.size = 0)
			if (campo == null) {
				throw new MiExcepcion(
						"No se puede declarar un registro sin campos");
			}

			campos.put(campo.getNombre(), campo);

			// camposRE
			camposRE(campos);

			// error = (campos.size = 0)
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return campos;
	}

	/*
	 * campo ≡ desctipo id
	 */
	private Campo campo() throws SintacticoException {

		Tipo tipoBase = null;
		String id = null;
		Campo campo = null;

		try {

			// desctipo
			tipoBase = desctipo();

			// error = falta_expresion
			if (tipoBase == null) {
				throw new MiExcepcion(SintacticoException.FALTA_DESCTIPO);
			}

			// id
			id = sintactico.getLexico().getLexema();

			// error = falta_expresion
			if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
				throw new MiExcepcion(SintacticoException.FALTA_ID);
			}

			campo = new Campo(tipoBase, id, 0);

			// error = falta_expresion;
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return campo;
	}

	/*
	 * camposRE ≡ , campo camposRE | vacio
	 */
	private void camposRE(HashMap<String, Campo> campos)
			throws SintacticoException {

		Campo campo = null;

		try {
			// ,
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_COMA)) {

				// campo
				campo = campo();

				// error = falta_expresion
				if (campo == null) {
					throw new MiExcepcion(
							"Se esperaba un campo después de la coma");
				}

				campo.setDesplazamiento(campos.size());
				campos.put(campo.getNombre(), campo);

				// camposRE
				camposRE(campos);
			}

			// error = falta_expresion
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/*
	 * mem ≡ id memRE
	 */
	public Tipo mem() throws SintacticoException {

		String id = sintactico.getLexico().getLexema();
		Tipo tipo = null;

		try {
			// id
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {

				// error = existeID(mem.tsh, id.lex) && mem.tsh[id.lex].clase ==
				// var
				if (GestorTS.getInstancia().existeID(id)
						&& GestorTS.getInstancia().getDetalles(id).getClase() == Clase.var) {
					throw new MiExcepcion(
							SintacticoException.VARIABLE_NO_DECLARADA);
				}

				// error = referenciaErronea
				if (referenciaErronea()) {
					throw new MiExcepcion(
							SintacticoException.REFERENCIA_ERRONEA);
				}

				// cod = accesoVar(mem.tsh[id.lex])
				accesoVar(id);

				// memRE
				tipo = memRE(GestorTS.getInstancia().getDetalles(id).getTipo());
				
			}
			
			return tipo;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

	}

	/*
	 * memRE ≡ [ expresion2 ] memRE | .id memRE | ^ memRE | vacio
	 */
	private Tipo memRE(Tipo tipoh) throws SintacticoException {

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
			return null;
		}
	}

	/*
	 * memREArray ≡ [ expresion2 ] memRE
	 */
	private Tipo memREArray(Tipo tipoh) throws SintacticoException {

		Tipo tipo = null;

		try {
			// [
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_CORCHETE_AB)) {

				// expresion2
				try {
					Tipo tipoNum = expresiones.expresion2();

					// error = expresion2.tipo = <t:int,tam:1>
					if ((tipoNum == null) || !(tipoNum instanceof TipoEntero))
						throw new MiExcepcion(
								SintacticoException.EXPRESION_INVALIDA);

				} catch (Exception e) {
					// FIXME Cuando estén todas las excepciones asi esto no hará
					// falta
					throw new MiExcepcion(e.getMessage());
				}

				// ]
				if (sintactico.reconoce(PalabrasReservadas.TOKEN_CORCHETE_CE)) {

					tipo = ((TipoArray) tipoh).getTipoBase();

					// memRE0.cod = apila(memRE0.tipo.tbase.tam) ++ mult ++ suma
					sintactico.getCodigo().add(
							new Apilar(new DatoPila(DatoPila.INT, tipo
									.getTamaño())));
					sintactico.getCodigo().add(new Multiplicar());
					sintactico.getCodigo().add(new Sumar());

				} else
					throw new MiExcepcion(SintacticoException.FALTA_CORCHETE);

			}

			// error = expresion2.tipo = <t:int,tam:1> && referenciaerronea
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipo;
	}

	/*
	 * memRERecord ≡ .id memRE
	 */
	private Tipo memRERecord(Tipo tipoh) throws SintacticoException {

		String id = null;
		Tipo tipo = null;

		try {
			// .
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO)) {

				// id
				id = sintactico.getLexico().getLexema();

				// error = falta_expresion
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
					throw new MiExcepcion(SintacticoException.FALTA_ID);
				}

				// error = existeCampo(memRE1.camposh,id.lex)
				if (!((TipoRegistro) tipoh).getCampos().keySet().contains(id)) {
					throw new MiExcepcion(
							"No existe ningún campo con ese identificador");
				}

				// error = referenciaErronea
				if (referenciaErronea()) {
					throw new MiExcepcion(
							SintacticoException.REFERENCIA_ERRONEA);
				}

				tipo = ((TipoRegistro) tipoh).getCampos().get(id).getTipoBase();

				// cod = apila(memRE0.tipo.campos[id.lex].desp) ++ suma
				sintactico.getCodigo().add(
						new Apilar(new DatoPila(DatoPila.INT,
								((TipoRegistro) tipoh).getCampos().get(id)
										.getDesplazamiento())));
				sintactico.getCodigo().add(new Sumar());

				// memRE
				tipo = memRE(tipo);

			}

			// error = existeCampo(memRE1.camposh,id.lex) && referenciaErronea
			// && falta_expresion
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipo;
	}

	/*
	 * memREPuntero ≡ ^ memRE
	 */
	private Tipo memREPuntero(Tipo tipoh) throws SintacticoException {
		Tipo tipo = null;

		try {
			// ^
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTERO_FLECHA)) {

				// error = referenciaErronea
				if (referenciaErronea()) {
					throw new MiExcepcion(
							SintacticoException.REFERENCIA_ERRONEA);
				}

				// cod = memRE1.cod ++ apila-ind
				// FIXME HACER EL APILAIND!!
				// sintactico.getCodigo().add(new ApilaInd());

				// memRE
				memRE(tipo);

			}

			// error = referenciaErronea
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}

		return tipo;
	}

	/* FUNCIONES SEMÁNTICAS */

	private void accesoVar(String id) {

		Detalles info = GestorTS.getInstancia().getDetalles(id);
		sintactico.getCodigo().add(
				new ApilarDir(new DatoPila(DatoPila.INT, info.getNivel() + 1)));
		sintactico.getCodigo().add(
				new Apilar(new DatoPila(DatoPila.INT, info.getDir())));
		sintactico.getCodigo().add(new Sumar());
		if (info.getClase().equals(Clase.var)) {
			// FIXME Hacer el APILAIND!!!
			// sintactico.getCodigo().add(new ApilarInd());
		}
	}

	private boolean referenciaErronea() {
		// TODO HACER!!!
		return false;
	}
	
	@SuppressWarnings("serial")
	private class MiExcepcion extends Exception {

		public MiExcepcion(String mensaje) {
			super(mensaje);
		}

	}
}
