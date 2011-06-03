package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Y_Logica extends InstruccionInterprete {

	public Y_Logica(){
		super(InstruccionInterprete.CODIGO_Y);
	}

	public String toString() {
		return "y";
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion{

		DatoPila d1 = interprete.getPila().pop();

		if (d1.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(),
					"Los operandos de esta instruccion deben ser enteros");

		if (!(d1.getEntero() == 0 || d1.getEntero() == 1))
			throw new InterpreteExcepcion(this.toString(),
					"Los operandos de esta instruccion deben ser 0 o 1");

		DatoPila d2 = interprete.getPila().pop();

		if (d2.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(),
					"Los operandos de esta instruccion deben ser enteros");

		if (!(d2.getEntero() == 0 || d2.getEntero() == 1))
			throw new InterpreteExcepcion(this.toString(),
					"Los operandos de esta instruccion deben ser 0 o 1");

		DatoPila resultado = new DatoPila(DatoPila.INT,
				(d2.getEntero() == 0) ? 0 : ((d1.getEntero() == 0) ? 0: 1));

		interprete.getPila().push(resultado);

		return true;

	}

}
