package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class IrV extends InstruccionInterprete {

	public IrV() throws InterpreteExcepcion {
		super(InstruccionInterprete.CODIGO_IR_V);
		throw new InterpreteExcepcion(this.toString(),
				InterpreteExcepcion.FALTA_PARAMETRO);
	}

	public IrV(DatoPila d) throws InterpreteExcepcion {
		super(InstruccionInterprete.CODIGO_IR_V, d);
		if (d.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.TIPO_INCORRECTO);
	}

	@Override
	public String toString() {
		return "ir-v " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion {
		
		boolean salto = true;
		DatoPila saltar = interprete.getPila().pop();

		if (!(saltar.getTipo() == DatoPila.INT && (saltar.getEntero() == 0 || saltar
				.getEntero() == 1))) {
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.TIPO_INCORRECTO);
		}
		if (!((this.getDato().getTipo() == DatoPila.INT) && (interprete
				.getPrograma().size() > this.getDato().getEntero()))) {
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.DIRECCION_INVALIDA);
		}
		if (saltar.getEntero() == 1) {
			interprete.setCp(this.getDato().getEntero());
			salto = true;
		}

		return salto;

	}

}
