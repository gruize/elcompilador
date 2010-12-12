package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Igual extends InstruccionInterprete {

	public Igual() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_IGUAL);
	}

	public Igual(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_IGUAL);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "igual";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
