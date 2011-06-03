package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class CastInt extends InstruccionInterprete {

	public CastInt() {
		super(InstruccionInterprete.CODIGO_CASTINT);
	}

	@Override
	public String toString() {
		return "castint";
	}

	@Override
	/**
	 * Semantica:
	 * apilar(desapilar().toInt())
	 * @return siempre true (nunca modifica el cp del interprete)
	 */
	public boolean ejecutate(Interprete interprete) {
		DatoPila e = new DatoPila(DatoPila.INT, interprete.getPila().pop()
				.getEntero());
		interprete.getPila().push(e);
		return true;
	}

}
