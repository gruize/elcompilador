package interprete.instrucciones;

import interprete.InstruccionInterprete;
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

		DatoPila dato = interprete.getPila().pop();

		if (dato.getTipo() == DatoPila.INT)
			Math.abs(dato.getEntero());
		else
			Math.abs(dato.getReal());
		
		return true;
	}

}
