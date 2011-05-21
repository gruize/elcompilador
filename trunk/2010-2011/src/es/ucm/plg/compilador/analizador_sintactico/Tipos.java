package es.ucm.plg.compilador.analizador_sintactico;

import java.util.ArrayList;
import java.util.List;

import es.ucm.plg.compilador.analizador_lexico.PalabrasReservadas;
import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.compilador.tablaSimbolos.Detalles;
import es.ucm.plg.compilador.tablaSimbolos.GestorTS;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Campo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoArray;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoEntero;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoPuntero;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoReal;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoRegistro;
import es.ucm.plg.interprete.datoPila.DatoPila;
import es.ucm.plg.interprete.instrucciones.ApilarDir;

public class Tipos {

	private AnalizadorSintactico sintactico;

	public Tipos(AnalizadorSintactico sintactico) {
		this.sintactico = sintactico;
	}

	public Tipo desctipo() {

		Tipo tipo = null;

		if (!sintactico.isError()) {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_INT))
				tipo = new TipoEntero();
			else if (sintactico.reconoce(PalabrasReservadas.TOKEN_REAL))
				tipo = new TipoReal();
		}

		return tipo;

	}

	public Tipo defTipo() {

		Tipo tipoResult = null;

		if (!sintactico.isError()) {

			if (sintactico.reconoce(PalabrasReservadas.TOKEN_TIPO)) {
				tipoResult = array();
				if (tipoResult == null) {
					tipoResult = puntero();
					if (tipoResult == null) {
						tipoResult = registro();
					}
				}
			}

		}

		return tipoResult;
	}

	public Tipo registro() {

		Tipo tipoResult = null;

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_REC)) {
			List<Campo> campos = campos();
			if (campos.size() > 0)
				if (sintactico.reconoce(PalabrasReservadas.TOKEN_ENDREC)
						&& sintactico.reconoce(PalabrasReservadas.TOKEN_ID))
					tipoResult = new TipoRegistro(campos);
		}

		return tipoResult;
	}

	public Tipo array() {

		Tipo tipoResult = null;
		Tipo tipoBase = null;

		tipoBase = desctipo();

		if (tipoBase != null) {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_CORCHETE_AB)) {
				int num = Integer.parseInt(sintactico.getLexico().getLexema());
				if (sintactico.reconoce(PalabrasReservadas.TOKEN_INT)) {
					if (sintactico
							.reconoce(PalabrasReservadas.TOKEN_CORCHETE_CE)) {
						tipoResult = new TipoArray(tipoBase, num);
					} else {
						sintactico.setError(true);
						GestorErrores.agregaError(11, sintactico.getLexico()
								.getFila(),
								sintactico.getLexico().getColumna(),
								"Error en la declaración del array");
					}
				} else {
					sintactico.setError(true);
					GestorErrores.agregaError(11, sintactico.getLexico()
							.getFila(), sintactico.getLexico().getColumna(),
							"Error en la declaración del array");
				}
			}
		}

		return tipoResult;

	}

	public Tipo puntero() {

		Tipo tipoResult = null;
		Tipo tipoBase = null;

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_POINTER)) { // PUNTEROS
			tipoBase = desctipo();
			if (tipoBase != null) {
				tipoResult = new TipoPuntero(tipoBase);
			}
		}

		return tipoResult;
	}

	public List<Campo> campos() {
		List<Campo> campos = new ArrayList<Campo>();
		Tipo tipoBase = desctipo();
		boolean endRec = false;

		while (!(endRec = sintactico.reconoce(PalabrasReservadas.TOKEN_ENDREC))
				&& tipoBase != null) {
			String id = sintactico.getLexico().getLexema();
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID)
					&& sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA)) {
				campos.add(new Campo(tipoBase, id));
			}
			tipoBase = desctipo();
		}

		if (!endRec) {
			sintactico.setError(true);
			campos.clear();
			GestorErrores.agregaError(11, sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna(),
					"Error en los campos del registro");
		}

		return campos;
	}

	public Tipo men() {
		Tipo tipoResult = null;
		Detalles detalle = null;

		detalle = mem();

		if (detalle != null) {
			tipoResult = menRec(detalle);

			if (tipoResult == null) {
				tipoResult = menPuntero(detalle);
			}
		}

		return tipoResult;
	}

	private Tipo menRec(Detalles detalle) {

		Tipo tipoResult = null;
		String id;

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO)) {
			id = sintactico.getLexico().getLexema();

			if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {

				if (detalle.getTipo() instanceof TipoRegistro) {
					List<Campo> campos = ((TipoRegistro) detalle.getTipo())
							.getCampos();
					Campo campo = null;
					boolean encontrado = false;
					int i = 0;

					while (!encontrado && i < campos.size()) {
						if (campos.get(i).getNombre().equals(id)) {
							campo = campos.get(i);
							encontrado = true;
						}
					}

					if (campo != null) {
						tipoResult = campo.getTipoBase();
					}
				} else {
					sintactico.setError(true);
					GestorErrores.agregaError(11, sintactico.getLexico()
							.getFila(), sintactico.getLexico().getColumna(),
							"Error al obtener el registro");
				}
			}
		}

		return tipoResult;
	}

	public Tipo menPuntero(Detalles detalle) {

		Tipo tipoResult = null;

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTERO_FLECHA)) {
			tipoResult = detalle.getTipo();
			sintactico.getCodigo().add(
					new ApilarDir(new DatoPila(DatoPila.INT, GestorTS
							.getInstancia().getDir(detalle.getId()))));
		}

		return tipoResult;
	}

	public Detalles mem() {

		Detalles detalles = null;
		String id = sintactico.getLexico().getLexema();

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {

			detalles = memRE(id);

		}

		return detalles;

	}

	private Detalles memRE(String id) {
		Detalles detalle = null;
		
//		if (sintactico.reconoce(token_necesario))

		return detalle;
	}

}
