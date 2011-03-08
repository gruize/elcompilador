package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

import compilador.gestorErrores.GestorErrores;

public class CastInt extends InstruccionInterprete {

	public CastInt() {
		super(InstruccionInterprete.CODIGO_CASTINT);
	}

	public CastInt(DatoPila d) {
		super(InstruccionInterprete.CODIGO_CASTINT);
		GestorErrores.agregaError("La instruccion no acepta argumentos");
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
	 * @throws InterpreteException si se produce algun error al hacer el casting
	 */
	public boolean ejecutate(Interprete interprete) {
		DatoPila e = new DatoPila(DatoPila.INT, interprete.getPila().pop().getEntero());
		interprete.getPila().push(e);
		return true;
	}

}
