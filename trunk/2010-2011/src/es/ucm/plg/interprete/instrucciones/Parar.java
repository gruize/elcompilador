package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;

public class Parar extends InstruccionInterprete {

	public Parar(){
		super(InstruccionInterprete.CODIGO_PARAR);
	}
	
	@Override
	public String toString() {
		return "parar";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		interprete.setParar(true);
        return true;
	}

}
