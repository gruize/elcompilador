package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class CastReal extends InstruccionInterprete {

	public CastReal(){
		super(InstruccionInterprete.CODIGO_CASTREAL);
	}

	public CastReal(DatoPila d) throws InterpreteExcepcion{
		super(InstruccionInterprete.CODIGO_CASTREAL);
		throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.SOBRA_PARAMETRO);
	}

	@Override
	public String toString() {
		return "castreal";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		DatoPila e = new DatoPila(DatoPila.REAL, interprete.getPila().pop().getReal());
		interprete.getPila().addFirst(e);
		return true;
	}

}
