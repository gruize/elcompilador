package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Modulo extends InstruccionInterprete {

	public Modulo() {
		super(InstruccionInterprete.CODIGO_MODULO);
	}

	@Override
	public String toString() {
		return "modulo";
	}

	@Override
	/*
	 * Tanto los operandos como el resultado deben ser de tipo entero
	 */
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion {

		DatoPila d2 = interprete.getPila().pop();

		if (d2.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.TIPO_INCORRECTO);

		DatoPila d1 = interprete.getPila().pop();

		if (d1.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.TIPO_INCORRECTO);

		DatoPila resultado = new DatoPila(DatoPila.INT,
				(Integer) (d1.getEntero() % d2.getEntero()));

		interprete.getPila().push(resultado);

		return true;
	}

}
