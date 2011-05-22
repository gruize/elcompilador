package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Copia extends InstruccionInterprete{
	
	public Copia(){
		super(InstruccionInterprete.CODIGO_COPIA);
	}

	public Copia(DatoPila d){
		super(InstruccionInterprete.CODIGO_COPIA);
		GestorErrores.agregaError("La instruccion Copia no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "copia";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		boolean ok = false;
		if(!interprete.getPila().isEmpty())
			interprete.getPila().push(interprete.getPila().peek());
		return ok;
	}
	
}
