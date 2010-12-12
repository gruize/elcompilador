package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Negacion extends InstruccionInterprete {

	public Negacion() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_NEGACION);
	}

	public Negacion(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_NEGACION);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "negacion";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
