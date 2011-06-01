package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Copia extends InstruccionInterprete{
	
	public Copia(){
		super(InstruccionInterprete.CODIGO_COPIA);
	}

	public Copia(DatoPila d) throws InterpreteExcepcion{
		super(InstruccionInterprete.CODIGO_COPIA);
		throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.SOBRA_PARAMETRO);
	}
	
	@Override
	public String toString() {
		return "copia";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		boolean ok = false;
		if(!interprete.getPila().isEmpty())
			interprete.getPila().push(interprete.getPila().peek());
		return ok;
	}
	
}
