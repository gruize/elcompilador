package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Dividir extends InstruccionInterprete {

	public Dividir() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_DIVIDE);
	}

	public Dividir(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_DIVIDE);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "dividir";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
