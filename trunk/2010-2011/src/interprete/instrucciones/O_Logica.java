package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class O_Logica extends InstruccionInterprete {

	public O_Logica() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_O);
	}

	public O_Logica(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_O);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}

	@Override
	public String toString() {
		return "o";
	}

	/**
	 * 
	 * @return siempre true (nunca modifica el cp del interprete)
	 * @throws InterpreteException
	 *             si encuentra tipos de datos no Bools
	 */

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteException {

		DatoPila d1 = interprete.getPila().pop();

		if (d1.getTipo() != DatoPila.INT)
			throw new InterpreteException(
					"Los operandos de esta instrucción deben ser enteros");

		if (!(d1.getEntero() == 0 || d1.getEntero() == 1))
			throw new InterpreteException(
					"Los operandos de esta instrucción deben ser 0 o 1");

		DatoPila d2 = interprete.getPila().pop();

		if (d2.getTipo() != DatoPila.INT)
			throw new InterpreteException(
					"Los operandos de esta instrucción deben ser enteros");

		if (!(d2.getEntero() == 0 || d2.getEntero() == 1))
			throw new InterpreteException(
					"Los operandos de esta instrucción deben ser 0 o 1");

		DatoPila resultado = new DatoPila(DatoPila.INT,
				(d1.getEntero() == 1) ? 1 : ((d2.getEntero() == 1) ? 1: 0));

		interprete.getPila().push(resultado);

		return true;

	}

}
