package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;

public class Copia extends InstruccionInterprete{
	
	public Copia(){
		super(InstruccionInterprete.CODIGO_COPIA);
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
