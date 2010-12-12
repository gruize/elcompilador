package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Menor extends InstruccionInterprete {

	public Menor() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MENOR);
	}

	public Menor(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MENOR);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "menor";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
