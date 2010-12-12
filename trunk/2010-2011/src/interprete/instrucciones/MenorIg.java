package interprete.instrucciones;

import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class MenorIg extends InstruccionInterprete {

	public MenorIg() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MENORIG);
	}

	public MenorIg(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MENORIG);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "menorigual";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
