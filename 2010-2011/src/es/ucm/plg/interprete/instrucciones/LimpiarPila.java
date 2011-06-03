package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;

public class LimpiarPila extends InstruccionInterprete{

	public LimpiarPila(){
		super(InstruccionInterprete.CODIGO_LIMPIAR);
	}
	
	@Override
	public String toString() {
		return "limpiar ";
	}

	
	@Override
	public boolean ejecutate(Interprete interprete) {
		interprete.getPila().clear();
		return true;
	}
	

}
