package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Desapilar extends InstruccionInterprete {

    public Desapilar(){
        super(InstruccionInterprete.CODIGO_DESAPILAR);
    }

    public Desapilar(DatoPila d){
        super(InstruccionInterprete.CODIGO_DESAPILAR);
        GestorErrores.agregaError("La instruccion Desapilar no acepta argumentos");
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
