package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Distinto extends InstruccionInterprete {

	public Distinto() {
		super(InstruccionInterprete.CODIGO_DISTINTO);
	}

	public Distinto(DatoPila d) throws InterpreteExcepcion {
		super(InstruccionInterprete.CODIGO_DISTINTO);
		throw new InterpreteExcepcion(this.toString(),
				InterpreteExcepcion.SOBRA_PARAMETRO);
	}

	@Override
	public String toString() {
		return "distinto";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila d1 = interprete.getPila().pop();
		DatoPila d2 = interprete.getPila().pop();

		DatoPila resultado = new DatoPila(DatoPila.INT, (!d1.getValor().equals(
				d2.getValor()) ? 1 : 0));

		interprete.getPila().push(resultado);

		return true;
	}
}
