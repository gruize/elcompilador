package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Igual extends InstruccionInterprete {

	public Igual(){
		super(InstruccionInterprete.CODIGO_IGUAL);
	}

	public Igual(DatoPila d){
		super(InstruccionInterprete.CODIGO_IGUAL);
		GestorErrores.agregaError("La instruccion Igual no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "igual";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		
		DatoPila d1 = interprete.getPila().pop();
		DatoPila d2 = interprete.getPila().pop();
		
		DatoPila resultado = new DatoPila(
				DatoPila.INT, (d1.getValor().equals(d2.getValor()) ? 1: 0));

		interprete.getPila().push(resultado);

		return true;
	}

}
