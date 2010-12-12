package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Salida extends InstruccionInterprete {

	public Salida() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_SALIDA);
	}

	public Salida(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_SALIDA, d);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	
	@Override
	public String toString() {
		return "salida";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
