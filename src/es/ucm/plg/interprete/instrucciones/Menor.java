package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Menor extends InstruccionInterprete {

	public Menor(){
		super(InstruccionInterprete.CODIGO_MENOR);
	}

	public Menor(DatoPila d) throws InterpreteExcepcion{
		super(InstruccionInterprete.CODIGO_MENOR);
		throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.SOBRA_PARAMETRO);
	}
	
	@Override
	public String toString() {
		return "menor";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila d2 = interprete.getPila().pop();
		DatoPila d1 = interprete.getPila().pop();

		DatoPila resultado = new DatoPila(
				DatoPila.INT, (d1.getReal() < d2.getReal() ? 1: 0));

		interprete.getPila().push(resultado);

		return true;
	}

}
