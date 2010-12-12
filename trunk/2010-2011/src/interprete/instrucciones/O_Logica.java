package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class O_Logica extends InstruccionInterprete {

	public O_Logica() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_O);
	}

	public O_Logica(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_O);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "o";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
