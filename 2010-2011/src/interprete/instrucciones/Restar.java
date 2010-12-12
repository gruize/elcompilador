package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Restar extends InstruccionInterprete {

	public Restar() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_RESTA);
	}

	public Restar(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_RESTA);
		throw new InterpreteException("La instrucción no admite operadores");
	}
	
	@Override
	public String toString() {
		return "restar";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
