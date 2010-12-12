package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Distinto extends InstruccionInterprete {

	public Distinto() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_DISTINTO);
	}

	public Distinto(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_DISTINTO);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "distinto";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
