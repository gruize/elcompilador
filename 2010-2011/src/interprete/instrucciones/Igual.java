package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Igual extends InstruccionInterprete {

	public Igual() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_IGUAL);
	}

	public Igual(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_IGUAL);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "igual";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		
		DatoPila d1 = interprete.getPila().pop();
		DatoPila d2 = interprete.getPila().pop();
		
		DatoPila resultado = new DatoPila(
				DatoPila.INT, (d1.getValor().equals(d2.getValor()) ? 1: 0));

		interprete.getPila().push(resultado);

		return true;
	}

}
