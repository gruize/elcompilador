package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

import compilador.gestorErrores.GestorErrores;

public class Desapilar extends InstruccionInterprete {

    public Desapilar(){
        super(InstruccionInterprete.CODIGO_DESAPILAR);
    }

    public Desapilar(DatoPila d){
        super(InstruccionInterprete.CODIGO_DESAPILAR);
        GestorErrores.agregaError("La instrucción no acepta argumentos");
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
