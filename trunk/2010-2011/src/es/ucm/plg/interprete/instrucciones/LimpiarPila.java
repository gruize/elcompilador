package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class LimpiarPila extends InstruccionInterprete{

	public LimpiarPila(){
		super(InstruccionInterprete.CODIGO_LIMPIAR);
	}

	public LimpiarPila(DatoPila d){
		super(InstruccionInterprete.CODIGO_LIMPIAR);
		GestorErrores.agregaError("La instruccion Limpiar no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return " limpiar ";
	}

	
	@Override
	public boolean ejecutate(Interprete interprete) {
		interprete.getPila().clear();
		return true;
	}
	

}
