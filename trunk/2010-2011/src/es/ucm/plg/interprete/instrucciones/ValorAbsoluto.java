package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class ValorAbsoluto extends InstruccionInterprete {

	public byte tipo;

	public ValorAbsoluto(){
		super(InstruccionInterprete.CODIGO_ABS);
	}

	public ValorAbsoluto(DatoPila d){
		super(InstruccionInterprete.CODIGO_ABS, d);
	}

	@Override
	public String toString() {
		return "absoluto";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila dato = interprete.getPila().pop();

		if (dato.getTipo() == DatoPila.INT)
			Math.abs(dato.getEntero());
		else
			Math.abs(dato.getReal());
		
		return true;
	}

}
