package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Restar extends InstruccionInterprete {

	public Restar() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_RESTA);
	}

	public Restar(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_RESTA);
		throw new InterpreteException("La instrucción no admite operadores");
	}

	@Override
	public String toString() {
		return "restar";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila d1 = interprete.getPila().pop();
		DatoPila d2 = interprete.getPila().pop();

		Byte tipoRes = getTipoResult(d1.getTipo(), d2.getTipo());
		DatoPila resultado;

		if (tipoRes == DatoPila.INT)
			resultado = new DatoPila(DatoPila.INT,
					(Integer) (d2.getEntero() - d1.getEntero()));
		else
			resultado = new DatoPila(DatoPila.REAL,
					(Float) (d2.getReal() - d1.getReal()));

		interprete.getPila().push(resultado);

		return true;
	}

}
