package es.ucm.plg.compilador.analizadorSintactico;

import java.util.Vector;

import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
import es.ucm.plg.compilador.tablaSimbolos.Detalles.Clase;
import es.ucm.plg.compilador.tablaSimbolos.GestorTS;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoEntero;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoFuncion;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoPuntero;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;
import es.ucm.plg.interprete.instrucciones.Apilar;
import es.ucm.plg.interprete.instrucciones.Delete;
import es.ucm.plg.interprete.instrucciones.DesapilarInd;
import es.ucm.plg.interprete.instrucciones.IrA;
import es.ucm.plg.interprete.instrucciones.IrF;
import es.ucm.plg.interprete.instrucciones.LimpiarPila;
import es.ucm.plg.interprete.instrucciones.New;

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
			sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
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
					&& !accionLibera() && !accionInvocacion()
					&& !accionReturn()
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

	/**
	 * accioninvoca ≡ id ( Aparams )
	 * 
	 * @return ok
	 * @throws Exception
	 */
	public boolean accionInvocacion() throws Exception {
		boolean ok = true;
		String id = sintactico.getLexico().getLexema();
		sintactico.getLexico().copiaEstado();
		if (sintactico.reconoce(PalabrasReservadas.TOKEN_ID)) {
			if (!(GestorTS.getInstancia().ts().existeID(id)
					&& GestorTS
					.getInstancia().buscaGlobal(id).getClase() == Clase.fun)) {
				sintactico.getLexico().volverEstadoAnterior();
				ok = false;
			} else {
				if (sintactico.getLexico().getToken_actual()
						.equals(PalabrasReservadas.TOKEN_PARENTESIS_AP)) {
					Vector<Params> params = new Vector<Params>();
					ok = aparams(params, id);
					TipoFuncion funTipo = (TipoFuncion) GestorTS.getInstancia()
							.ts().getTipo(id);
					if (funTipo.getNparams() == params.size()) {
						boolean valido = true;
						int i = 0;
						while (valido && i < funTipo.getNparams()) {
							if (!funTipo.getParams().get(i).getTipo()
									.equals(params.get(i).getTipo())
									&& !funTipo.getParams().get(i).getModo()
											.equals(params.get(i).getModo()))
								valido = false;
							i++;
						}
						if (!valido)
							throw new MiExcepcion(
									SintacticoException.PARAMETROS_INVALIDOS);
					} else
						throw new MiExcepcion(
								SintacticoException.NUM_PARAMETROS_INVALIDO);
					if (ok) {
						sintactico.apilarRet(sintactico.getEtiqueta());
						sintactico.getCodigo().add(
								new IrA(new DatoPila(DatoPila.INT, GestorTS
										.getInstancia().buscaGlobal(id)
										.getInicio())));
						sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
					}
				} else
					throw new MiExcepcion(
							SintacticoException.FALTA_PARENTESIS_AP);
			}
		} else {
			ok = false;
		}
		return ok;
	}

	/**
	 * Aparams ≡ expresiones Aparams ≡ λ
	 * 
	 * @return ok
	 * @throws Exception
	 */
	private boolean aparams(Vector<Params> params, String id) throws Exception {
		boolean ok = true;
		if (sintactico.reconoce(PalabrasReservadas.TOKEN_PARENTESIS_AP)) {
			if (!sintactico.getLexico().getToken_actual()
					.equals(PalabrasReservadas.TOKEN_PARENTESIS_CE)) {
				sintactico.inicioPaso();
				if (sintactico.getExpresiones().expresiones(params, id)) {
					sintactico.finPaso();
				} else
					throw new MiExcepcion(
							SintacticoException.ESPERABA_PARAMETROS);
				if (!sintactico
						.reconoce(PalabrasReservadas.TOKEN_PARENTESIS_CE))
					throw new MiExcepcion(
							SintacticoException.FALTA_PARENTESIS_CE);
			}
		}
		return ok;
	}

	/**
	 * accionreturn := return expresion2 ;
	 * 
	 * @throws InterpreteExcepcion
	 * @throws SintacticoException
	 * 
	 */
	public boolean accionReturn() throws SintacticoException,
			InterpreteExcepcion {
		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_RETURN)) {

				Tipo tipo = sintactico.getExpresiones().expresion2();

				if (tipo == null) {
					throw new MiExcepcion(SintacticoException.TIPO_INCOMPATIBLE);
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
	 * accionlibera := free mem ;
	 * 
	 * @return ok
	 * @throws Exception
	 */
	public boolean accionLibera() throws Exception {

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_LIBERA)) {

				Tipo tipo = sintactico.getTipos().mem();

				if (tipo == null) {
					throw new MiExcepcion(SintacticoException.FALTA_ID);
				}

				if (!(tipo instanceof TipoPuntero)) {
					throw new MiExcepcion(SintacticoException.TIPO_INCOMPATIBLE);
				}

				sintactico.getCodigo().add(
						new Delete(
								new DatoPila(DatoPila.INT, tipo.getTamanyo())));
				sintactico.getCodigo().add(
						new Apilar(
								new DatoPila(DatoPila.INT, Integer.MIN_VALUE)));
				sintactico.getCodigo().add(new DesapilarInd());
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 3);

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
	 * accionreserva := alloc mem ;
	 * 
	 * @return ok
	 * @throws errorh
	 *             && mem.tipo <t:puntero>
	 * @throws InterpreteExcepcion
	 */
	public boolean accionReserva() throws SintacticoException,
			InterpreteExcepcion {

		try {
			if (sintactico.reconoce(PalabrasReservadas.TOKEN_RESERVA)) {

				Tipo tipo = sintactico.getTipos().mem();

				if (tipo == null) {
					throw new MiExcepcion(SintacticoException.FALTA_ID);
				}

				if (!(tipo instanceof TipoPuntero)) {
					throw new MiExcepcion(SintacticoException.TIPO_INCOMPATIBLE);
				}

				int tam = ((TipoPuntero) tipo).getTipoBase().getTamanyo();

				sintactico.getCodigo().add(
						new New(new DatoPila(DatoPila.INT, tam)));
				sintactico.getCodigo().add(new DesapilarInd());
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 2);

				return true;
			}

			return false;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	private boolean accionAlternativa() throws Exception {
		boolean ok = false;
		if (sintactico.reconoce(PalabrasReservadas.TOKEN_IF)) {
			Tipo tipo = sintactico.getExpresiones().expresion2();
			if (tipo instanceof TipoEntero
					&& sintactico.reconoce(PalabrasReservadas.TOKEN_THEN)) {
				sintactico.getCodigo().add(new IrF(null));
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
				int irfalseAux = sintactico.getCodigo().size() - 1;
				ok = bloque(PalabrasReservadas.TOKEN_IF);
				sintactico.getCodigo().add(new IrA(null));
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
				int irAAux = sintactico.getCodigo().size() - 1;
				sintactico.getCodigo().set(
						irfalseAux,
						new IrF(new DatoPila(DatoPila.INT, sintactico
								.getEtiqueta())));
				ok = accionelse();
				sintactico.getCodigo().set(
						irAAux,
						new IrA(new DatoPila(DatoPila.INT, sintactico
								.getEtiqueta())));
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_END_IF))
					throw new MiExcepcion(
							"Alternativa sin finalizar. Se esperaba la palabra EndIf");
			} else
				throw new MiExcepcion(
						"Se esperaba un entero con valor 1 o 0 y la palabra Then");
		}
		return ok;
	}

	private boolean accionelse() throws Exception {
		boolean ok = false;
		if (sintactico.getLexico().getToken_actual()
				.equals(PalabrasReservadas.TOKEN_ELSIF)) {
			sintactico.reconoce(PalabrasReservadas.TOKEN_ELSIF);
			Tipo tipo = sintactico.getExpresiones().expresion2();
			if (tipo instanceof TipoEntero
					&& sintactico.reconoce(PalabrasReservadas.TOKEN_THEN)) {
				sintactico.getCodigo().add(new IrF(null));
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
				int irfalseAux = sintactico.getCodigo().size() - 1;

				ok = bloque(PalabrasReservadas.TOKEN_ELSIF);

				sintactico.getCodigo().add(new IrA(null));
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
				int irAAux = sintactico.getCodigo().size() - 1;

				sintactico.getCodigo().set(
						irfalseAux,
						new IrF(new DatoPila(DatoPila.INT, sintactico
								.getEtiqueta())));

				ok = accionelse();

				sintactico.getCodigo().set(
						irAAux,
						new IrA(new DatoPila(DatoPila.INT, sintactico
								.getEtiqueta())));
			} else
				throw new MiExcepcion(
						"Se esperaba un entero con valor 1 o 0 y la palabra Then");
		} else {
			if (sintactico.getLexico().getToken_actual()
					.equals(PalabrasReservadas.TOKEN_ELSE)) {
				sintactico.reconoce(PalabrasReservadas.TOKEN_ELSE);
				ok = bloque(PalabrasReservadas.TOKEN_ELSE);
				if (sintactico.getLexico().getToken_actual()
						.equals(PalabrasReservadas.TOKEN_END_IF))
					ok = true;
			} else
				ok = true;
		}
		return ok;
	}

	private boolean bloque(String tokenFinaliza) throws Exception {
		boolean ok = false;
		if (tokenFinaliza.equals(PalabrasReservadas.TOKEN_IF)
				|| tokenFinaliza.equals(PalabrasReservadas.TOKEN_ELSIF)) {
			while (!sintactico.getLexico().getToken_actual()
					.equals(PalabrasReservadas.TOKEN_ELSIF)
					&& !sintactico.getLexico().getToken_actual()
							.equals(PalabrasReservadas.TOKEN_ELSE)
					&& !sintactico.getLexico().getToken_actual()
							.equals(PalabrasReservadas.TOKEN_END_IF)
					&& !sintactico.getLexico().isFin_programa())
				ok = accion();
		} else {
			if (tokenFinaliza.equals(PalabrasReservadas.TOKEN_ELSE)) {
				while (!sintactico.getLexico().getToken_actual()
						.equals(PalabrasReservadas.TOKEN_END_IF)
						&& !sintactico.getLexico().isFin_programa())
					ok = accion();
			} else {
				if (tokenFinaliza.equals(PalabrasReservadas.TOKEN_WHILE)) {
					while (!sintactico.getLexico().isFin_programa()
							&& !sintactico.getLexico().getToken_actual()
									.equals(PalabrasReservadas.TOKEN_END_WHILE))
						ok = accion();
				}
			}
		}
		return ok;
	}

	private boolean accionIteracion() throws Exception {
		boolean ok = false;
		if (sintactico.reconoce(PalabrasReservadas.TOKEN_WHILE)) {
			int whileAux = sintactico.getEtiqueta();
			Tipo tipo = sintactico.getExpresiones().expresion2();
			if (tipo instanceof TipoEntero
					&& sintactico.reconoce(PalabrasReservadas.TOKEN_DO)) {
				sintactico.getCodigo().add(new IrF(null));
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
				int irFalseAux = sintactico.getCodigo().size() - 1;
				ok = bloque(PalabrasReservadas.TOKEN_WHILE);
				sintactico.getCodigo().add(
						new IrA(new DatoPila(DatoPila.INT, whileAux)));
				sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
				sintactico.getCodigo().set(
						irFalseAux,
						new IrF(new DatoPila(DatoPila.INT, sintactico
								.getEtiqueta())));
				if (!sintactico.reconoce(PalabrasReservadas.TOKEN_END_WHILE))
					throw new MiExcepcion(
							"Bucle infinito. Se esperaba la palabra EndWhile");
			} else
				throw new MiExcepcion(
						"Se esperaba una expresion entera y la palabra DO");
		}
		return ok;
	}

	/**
	 * cuerpo := declaraciones acciones
	 * 
	 * @throws Exception
	 * 
	 */
	public int cuerpo() throws Exception {
		boolean findecs = sintactico.getDeclaraciones().isFinDecs();
		sintactico.getDeclaraciones().declaraciones();
		int inicio = sintactico.getEtiqueta();
		while (!accionReturn())
			accion();
		sintactico.getDeclaraciones().setFinDecs(findecs);
		return inicio;
	}

	/**
	 * cuerpo := acciones
	 * 
	 */

	@SuppressWarnings("serial")
	private class MiExcepcion extends Exception {

		public MiExcepcion(String mensaje) {
			super(mensaje);
		}

	}

}
