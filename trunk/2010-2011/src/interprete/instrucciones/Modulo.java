package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Modulo extends InstruccionInterprete {

	public Modulo() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MODULO);
	}

	public Modulo(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_MODULO);
		throw new InterpreteException("La instrucción no acepta argumentos");
	}

	@Override
	public String toString() {
		return "modulo";
	}

	@Override
	/*
	 * Tanto los operandos como el resultado deben ser de tipo entero
	 */
	public boolean ejecutate(Interprete interprete) throws InterpreteException {

		DatoPila d1 = interprete.getPila().pop();

		if (d1.getTipo() != DatoPila.INT)
			throw new InterpreteException(
					"Los operandos de esta instrucción deben ser enteros");

		DatoPila d2 = interprete.getPila().pop();

		if (d2.getTipo() != DatoPila.INT)
			throw new InterpreteException(
					"Los operandos de esta instrucción deben ser enteros");

		DatoPila resultado = new DatoPila(DatoPila.INT,
				(Integer) (d1.getEntero() % d2.getEntero()));

		interprete.getPila().push(resultado);

		return true;
	}

}
