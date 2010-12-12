package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Sumar extends InstruccionInterprete {

	public Sumar() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_SUMA);
	}

	public Sumar(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_SUMA);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "sumar";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
