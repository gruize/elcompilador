package es.ucm.plg.compilador.analizadorSintactico;

import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoEntero;

public class Acciones {

	private AnalizadorSintactico sintactico;
	private Expresiones expresiones;

	public Acciones(AnalizadorSintactico sintactico) {
		this.sintactico = sintactico;
		this.expresiones = new Expresiones(sintactico);
	}

	public boolean acciones() throws Exception {

		boolean ok = false;

		if (accion()) {
			if (accionesRE()) {
				ok = true;
			} else {
				ok = sintactico.isError();
			}
		} else {
			ok = sintactico.isError();
		}

		return ok;
	}

	private boolean accionesRE() throws Exception {

		boolean ok = false;

		if (!sintactico.getLexico().isFin_programa()) {
			if (accion()) {
				if (accionesRE()) {
					ok = true;
				} else {
					ok = false;
				}
			} else {
				ok = false;
			}
		}

		return ok;
	}

	private boolean accion() throws Exception {

		boolean ok = false;

		if (this.sintactico.getLexico().getToken_actual()
				.equals(PalabrasReservadas.TOKEN_IF)) {
			ok = accionAlternativa();
		} else {
			if (this.sintactico.getLexico().getToken_actual()
					.equals(PalabrasReservadas.TOKEN_WHILE)) {
				ok = accionIteracion();
			} else {
				if (this.sintactico.getLexico().getToken_actual()
						.equals(PalabrasReservadas.TOKEN_RESERVA)) {
					ok = accionReserva();
				} else {
					if (this.sintactico.getLexico().getToken_actual()
							.equals(PalabrasReservadas.TOKEN_LIBERA)) {
						ok = accionLibera();
					} else {
						// Falta invocacion y expresion basica
						// else{
//						sintactico.setError(true);
						GestorErrores.agregaError(100, sintactico.getLexico()
								.getFila(),
								sintactico.getLexico().getColumna(),
								"Se esperaba una accion");
						// }
					}
				}
			}

			/**
			 * Anterior/ Tipo tipo = expresion(); if (tipo != null) { if
			 * (sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA)) { ok =
			 * true; codigo.add(new LimpiarPila()); } else {
			 * sintactico.setError(true); GestorErrores.agregaError(11,
			 * sintactico.getLexico().getFila(),
			 * sintactico.getLexico().getColumna(), "Falta un punto y coma"); }
			 * } else if (sintactico.getLexico().isFin_programa()) { ok = true;
			 * } else { sintactico.setError(true); GestorErrores.agregaError(11,
			 * sintactico.getLexico().getFila(),
			 * sintactico.getLexico().getColumna(),
			 * "Se esperaba una expresion"); } /Anterior
			 **/
		}

		return ok;
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
			Tipo tipo = expresiones.expresion2();
			if (tipo instanceof TipoEntero) {
				// ME QUEDO AQUI -- GABI
			} else {
//				sintactico.setError(true);
				GestorErrores
						.agregaError(101, sintactico.getLexico().getFila(),
								sintactico.getLexico().getColumna(),
								"Se esperaba una expresion de tipo entero (Comparacion)");
			}
		}
		return ok;
	}

	private boolean accionIteracion() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
