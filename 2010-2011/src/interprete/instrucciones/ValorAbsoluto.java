package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class ValorAbsoluto extends InstruccionInterprete {

    public byte tipo;

    public ValorAbsoluto() throws InterpreteException {
        super(InstruccionInterprete.CODIGO_ABS);
    }

    public ValorAbsoluto(DatoPila d) throws InterpreteException {
        super(InstruccionInterprete.CODIGO_ABS, d);
    }
	
	@Override
	public String toString() {
		return "absoluto";
	}
    
	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
