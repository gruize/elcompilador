package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class CambioSigno extends InstruccionInterprete {

	public CambioSigno() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_CAMBIO_SIGNO);
	}

	public CambioSigno(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_CAMBIO_SIGNO);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}

	@Override
	public String toString() {
		return "menos";
	}
	
	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
