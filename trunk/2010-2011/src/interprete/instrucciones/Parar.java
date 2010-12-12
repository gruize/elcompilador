package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Parar extends InstruccionInterprete {

	public Parar() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_PARAR);
	}

	public Parar(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_PARAR, d);
		throw new InterpreteException(
				"La instrucción no necesita parámetros");
	}
	
	@Override
	public String toString() {
		return "parar";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
