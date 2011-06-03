package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class MenorIg extends InstruccionInterprete {

	public MenorIg(){
		super(InstruccionInterprete.CODIGO_MENORIG);
	}
	
	@Override
	public String toString() {
		return "menorigual";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila d2 = interprete.getPila().pop();
		DatoPila d1 = interprete.getPila().pop();

		DatoPila resultado = new DatoPila(
				DatoPila.INT, (d1.getReal() <= d2.getReal() ? 1: 0));

		interprete.getPila().push(resultado);

		return true;
	}

}
