package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class CastReal extends InstruccionInterprete {

	public CastReal(){
		super(InstruccionInterprete.CODIGO_CASTREAL);
	}

	public CastReal(DatoPila d){
		super(InstruccionInterprete.CODIGO_CASTREAL);
		GestorErrores.agregaError("La instruccion Cast de Real no acepta argumentos");
	}

	@Override
	public String toString() {
		return "castreal";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		DatoPila e = new DatoPila(DatoPila.REAL, interprete.getPila().pop().getReal());
		interprete.getPila().addFirst(e);
		return true;
	}

}
