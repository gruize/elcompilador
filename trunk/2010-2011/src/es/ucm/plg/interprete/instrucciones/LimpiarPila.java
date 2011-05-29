package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class LimpiarPila extends InstruccionInterprete{

	public LimpiarPila(){
		super(InstruccionInterprete.CODIGO_LIMPIAR);
	}

	public LimpiarPila(DatoPila d) throws InterpreteExcepcion{
		super(InstruccionInterprete.CODIGO_LIMPIAR);
		throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.SOBRA_PARAMETRO);
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
