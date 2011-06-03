package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class IrA extends InstruccionInterprete {

	public IrA(DatoPila d) {
		super(InstruccionInterprete.CODIGO_IR_A, d);
	}

	@Override
	public String toString() {
		return "ir-a " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion {

		boolean salto = false;

		if ((this.getDato().getTipo() == DatoPila.INT)
				&& (interprete.getPrograma().size() > this.getDato()
						.getEntero())) {
			interprete.setCp(this.getDato().getEntero() - 1);
			salto = true;
		} else
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.DIRECCION_INVALIDA);

		return salto;
	}

}
