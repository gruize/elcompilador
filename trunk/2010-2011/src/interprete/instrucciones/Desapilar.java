package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Desapilar extends InstruccionInterprete {

    public Desapilar() throws InterpreteException {
        super(InstruccionInterprete.CODIGO_DESAPILAR);
    }

    public Desapilar(DatoPila d) throws InterpreteException{
        super(InstruccionInterprete.CODIGO_DESAPILAR);
        throw new InterpreteException("La instrucción no acepta argumentos");
    }
	
	@Override
	public String toString() {
		return "desapila";
	}
    
	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
