package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class MayorIg extends InstruccionInterprete {

	public MayorIg() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MAYORIG);
	}

	public MayorIg(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MAYORIG);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "mayorigual";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
