package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class CastInt extends InstruccionInterprete {

	public CastInt() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_CASTINT);
	}

	public CastInt(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_CASTINT);
		throw new InterpreteException("La instrucción no acepta argumentos");
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
