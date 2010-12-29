package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class CastReal extends InstruccionInterprete {

	public CastReal() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_CASTREAL);
	}

	public CastReal(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_CASTREAL);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}

	@Override
	public String toString() {
		return "castreal";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		DatoPila e = new DatoPila(DatoPila.REAL, interprete.getPila().pop().getReal());
		interprete.getPila().addFirst(e);
		return true;
	}

}
