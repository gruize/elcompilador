package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;

public class Desapilar extends InstruccionInterprete {

    public Desapilar(){
        super(InstruccionInterprete.CODIGO_DESAPILAR);
    }
	
	@Override
	public String toString() {
		return "desapila";
	}
    
	@Override
	public boolean ejecutate(Interprete interprete) {
		interprete.getPila().pop();
        return true;
	}

}
