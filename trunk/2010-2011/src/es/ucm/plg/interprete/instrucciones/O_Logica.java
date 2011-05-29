package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class O_Logica extends InstruccionInterprete {

	public O_Logica(){
		super(InstruccionInterprete.CODIGO_O);
	}

	public O_Logica(DatoPila d) throws InterpreteExcepcion{
		super(InstruccionInterprete.CODIGO_O);
		throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.SOBRA_PARAMETRO);
	}

	@Override
	public String toString() {
		return "o";
	}

	/**
	 * 
	 * @return siempre true (nunca modifica el cp del interprete)
	 * @throws InterpreteExcepcion 
	 * @throws InterpreteException
	 *             si encuentra tipos de datos no Bools
	 */

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion{

		DatoPila d2 = interprete.getPila().pop();

		if (d2.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(), 
					"Los operandos de esta instruccion deben ser enteros");

		if (!(d2.getEntero() == 0 || d2.getEntero() == 1))
			throw new InterpreteExcepcion(this.toString(), 
					"Los operandos de esta instruccion deben ser 0 o 1");

		DatoPila d1 = interprete.getPila().pop();

		if (d1.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(), 
					"Los operandos de esta instruccion deben ser enteros");

		if (!(d1.getEntero() == 0 || d1.getEntero() == 1))
			throw new InterpreteExcepcion(this.toString(), 
					"Los operandos de esta instruccion deben ser 0 o 1");

		DatoPila resultado = new DatoPila(DatoPila.INT,
				(d1.getEntero() == 1) ? 1 : ((d2.getEntero() == 1) ? 1: 0));

		interprete.getPila().push(resultado);

		return true;

	}

}
