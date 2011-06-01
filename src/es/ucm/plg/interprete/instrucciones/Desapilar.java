package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Desapilar extends InstruccionInterprete {

    public Desapilar(){
        super(InstruccionInterprete.CODIGO_DESAPILAR);
    }

    public Desapilar(DatoPila d) throws InterpreteExcepcion{
        super(InstruccionInterprete.CODIGO_DESAPILAR);
		throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.SOBRA_PARAMETRO);
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
