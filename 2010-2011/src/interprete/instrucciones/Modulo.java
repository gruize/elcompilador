package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Modulo extends InstruccionInterprete {

	public Modulo() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MODULO);
	}

	public Modulo(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MODULO);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "modulo";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
