package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class IrInd extends InstruccionInterprete {

	public IrInd(DatoPila d) throws InterpreteExcepcion {
		super(InstruccionInterprete.CODIGO_IR_IND, d);
		if (d.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.TIPO_INCORRECTO);
	}

	@Override
	public String toString() {
		return "ir-ind " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion {

		DatoPila dir = interprete.getPila().pop();

		if (dir.getTipo() != DatoPila.INT) {
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.TIPO_INCORRECTO);
		}
		if (!(dir.getEntero() < interprete.getPrograma().size() && dir
				.getEntero() >= 0)) {
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.DIRECCION_INVALIDA);
		}
		interprete.setCp(dir.getEntero());

		return false;
	}

}
