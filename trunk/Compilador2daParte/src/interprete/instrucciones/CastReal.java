package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

import compilador.gestorErrores.GestorErrores;

public class CastReal extends InstruccionInterprete {

	public CastReal(){
		super(InstruccionInterprete.CODIGO_CASTREAL);
	}

	public CastReal(DatoPila d){
		super(InstruccionInterprete.CODIGO_CASTREAL);
		GestorErrores.agregaError("La instruccion no acepta argumentos");
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
