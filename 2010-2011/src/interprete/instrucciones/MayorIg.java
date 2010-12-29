package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class MayorIg extends InstruccionInterprete {

	public MayorIg() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MAYORIG);
	}

	public MayorIg(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MAYORIG);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "mayorigual";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila d1 = interprete.getPila().pop();
		DatoPila d2 = interprete.getPila().pop();

		DatoPila resultado = new DatoPila(
				DatoPila.INT, (d1.getReal() >= d2.getReal() ? 1: 0));

		interprete.getPila().push(resultado);

		return true;
	}

}
