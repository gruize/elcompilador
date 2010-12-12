package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Multiplicar extends InstruccionInterprete {

	public Multiplicar() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MULTIPLICA);
	}

	public Multiplicar(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MULTIPLICA);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "multiplica";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
