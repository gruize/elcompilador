package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class IrF extends InstruccionInterprete {

	public IrF() throws InterpreteExcepcion {
		super(InstruccionInterprete.CODIGO_IR_F);
		throw new InterpreteExcepcion(this.toString(),
				InterpreteExcepcion.FALTA_PARAMETRO);
	}

	public IrF(DatoPila d) {
		super(InstruccionInterprete.CODIGO_IR_F, d);
	}

	@Override
	public String toString() {
		return "ir-f " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion {

		boolean salto = false;
		DatoPila saltar = interprete.getPila().pop();

		if (!(saltar.getTipo() == DatoPila.INT && (saltar.getEntero() == 0 || saltar
				.getEntero() == 1))) {
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.TIPO_INCORRECTO);
		}
		if ((this.getDato().getTipo() == DatoPila.INT)
				&& (interprete.getPrograma().size() > this.getDato()
						.getEntero()))
			salto = true;
		else
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.DIRECCION_INVALIDA);
		
		if (saltar.getEntero() == 0) {
			interprete.setCp(this.getDato().getEntero() - 1);
		}

		return salto;
	}

}
