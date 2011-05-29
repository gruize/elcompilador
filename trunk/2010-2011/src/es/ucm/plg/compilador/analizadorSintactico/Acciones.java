package es.ucm.plg.compilador.analizadorSintactico;

import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
import es.ucm.plg.interprete.instrucciones.LimpiarPila;

public class Acciones {

	private AnalizadorSintactico sintactico;

	public Acciones(AnalizadorSintactico sintactico) {
		this.sintactico = sintactico;
	}

	public void acciones() throws Exception {

		try {
			accion();
			accionesRE();

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	private void accionesRE() throws Exception {
		
		try {
			sintactico.getCodigo().add(new LimpiarPila());
			if (!sintactico.getLexico().isFin_programa()) {
				accion();
				accionesRE();
			}
			
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	private boolean accion() throws Exception {

		try {
			if (!accionAlternativa() && !accionIteracion() && !accionReserva()
					&& !accionLibera()
					&& !(sintactico.getExpresiones().expresion() != null)) {
				throw new MiExcepcion("Se esperaba una accion");
			}

			if (!sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA))
				throw new MiExcepcion(SintacticoException.FALTA_PUNTO_COMA);

			return true;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	private boolean accionLibera() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean accionReserva() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean accionAlternativa() throws Exception {
		boolean ok = false;

		if (sintactico.reconoce(PalabrasReservadas.TOKEN_IF)) {
//			Tipo tipo = expresiones.expresion2();
//			if (tipo instanceof TipoEntero) {
//				// ME QUEDO AQUI -- GABI
//			} else {
//				// sintactico.setError(true);
//				GestorErrores
//						.agregaError(101, sintactico.getLexico().getFila(),
//								sintactico.getLexico().getColumna(),
//								"Se esperaba una expresion de tipo entero (Comparacion)");
//			}
		}
		return ok;
	}

	private boolean accionIteracion() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings("serial")
	private class MiExcepcion extends Exception {

		public MiExcepcion(String mensaje) {
			super(mensaje);
		}

	}

}
