package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Parar extends InstruccionInterprete {

	public Parar(){
		super(InstruccionInterprete.CODIGO_PARAR);
	}

	public Parar(DatoPila d){
		super(InstruccionInterprete.CODIGO_PARAR, d);
		GestorErrores.agregaError(
				"La instruccion Parar no necesita parametros");
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
