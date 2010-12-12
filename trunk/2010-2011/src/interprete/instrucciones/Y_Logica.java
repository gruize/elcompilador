package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Y_Logica extends InstruccionInterprete {

	public Y_Logica() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_Y);
	}

	public Y_Logica(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_Y);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "y";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
