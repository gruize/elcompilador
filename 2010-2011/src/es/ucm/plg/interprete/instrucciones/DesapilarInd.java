package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class DesapilarInd extends InstruccionInterprete {

	public DesapilarInd() {
		super(InstruccionInterprete.CODIGO_DESAPILARIND);
	}

	public DesapilarInd(DatoPila d) throws InterpreteExcepcion {
		super(InstruccionInterprete.CODIGO_DESAPILARIND);
		throw new InterpreteExcepcion(this.toString(),
				InterpreteExcepcion.SOBRA_PARAMETRO);
	}

	@Override
	public String toString() {
		return "desapilarInd";
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion {
		DatoPila dato = interprete.getPila().pop();
		if (interprete.getPila().peek().getTipo() == DatoPila.INT) {
			Integer dir = interprete.getPila().pop().getEntero();
			interprete.getMemoria().getMemoria()[dir] = dato;
		} else
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.TIPO_INCORRECTO);
		return true;
	}

}
