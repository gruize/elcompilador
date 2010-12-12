package interprete.instrucciones;

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
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
