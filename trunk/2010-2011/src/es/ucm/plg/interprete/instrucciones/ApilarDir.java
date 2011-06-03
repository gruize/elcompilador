package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class ApilarDir extends InstruccionInterprete {

	public ApilarDir(DatoPila d) throws InterpreteExcepcion{
		super(InstruccionInterprete.CODIGO_APILARDIR, d);
		if (d.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.TIPO_INCORRECTO);
	}

	@Override
	public String toString() {
		return "apiladir " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila dato = interprete.getMemoria().getMemoria()[this.getDato().getEntero()];

		// Si en la posicion de memoria especificada no existen ningun valor
		// apilo un valor representativo, como es Integer.MIN_VALUE
		if (dato == null) {
			interprete.getPila().addFirst(
					new DatoPila(DatoPila.INT, Integer.MIN_VALUE));
		} else {
			interprete.getPila().push(
					interprete.getMemoria().getMemoria()[this.getDato().getEntero()]);
		}
		return true;
	}

}
