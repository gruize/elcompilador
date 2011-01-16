package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Sumar extends InstruccionInterprete {

	public Sumar() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_SUMA);
	}

	public Sumar(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_SUMA);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "sumar";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila d1 = interprete.getPila().pop();
		DatoPila d2 = interprete.getPila().pop();

		Byte tipoRes = getTipoResult(d1.getTipo(), d2.getTipo());
		DatoPila resultado;

		if (tipoRes == DatoPila.INT)
			resultado = new DatoPila(DatoPila.INT,
					(Integer) (d1.getEntero() + d2.getEntero()));
		else
			resultado = new DatoPila(DatoPila.REAL,
					(Float) (d1.getReal() + d2.getReal()));

		interprete.getPila().push(resultado);

		return true;
	}

}