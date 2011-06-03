package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class ApilarInd extends InstruccionInterprete {

	public ApilarInd() {
		super(InstruccionInterprete.CODIGO_APILARIND);
	}

	@Override
	public String toString() {
		return "apilaind";
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion {

		if (interprete.getPila().peek().getTipo() != DatoPila.INT) {
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.TIPO_INCORRECTO);
		}

		if (!(interprete.getPila().peek().getEntero() < interprete.getMemoria()
				.getMemoria().length || interprete.getPila().peek().getEntero() >= 0)) {
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.DIRECCION_INVALIDA);
		}

		Integer dir = interprete.getPila().pop().getEntero();
		interprete.getPila().push(interprete.getMemoria().getMemoria()[dir]);

		return true;
	}

}
