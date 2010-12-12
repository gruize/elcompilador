package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Mayor extends InstruccionInterprete {

	public Mayor() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MAYOR);
	}

	public Mayor(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MAYOR);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}	
	
	@Override
	public String toString() {
		return "mayor";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
