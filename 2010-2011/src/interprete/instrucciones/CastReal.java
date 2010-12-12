package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class CastReal extends InstruccionInterprete {

    public CastReal() throws InterpreteException {
        super(InstruccionInterprete.CODIGO_CASTREAL);
    }

    public CastReal(DatoPila d) throws InterpreteException{
        super(InstruccionInterprete.CODIGO_CASTREAL);
        throw new InterpreteException("La instrucción no acepta argumentos");
    }
	
	@Override
	public String toString() {
		return "castreal";
	}
    
	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
