package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Delete extends InstruccionInterprete {

	public Delete() throws InterpreteExcepcion {
		super(InstruccionInterprete.CODIGO_DELETE);
		throw new InterpreteExcepcion(this.toString(),
				"La instruccion Delete necesita un parametro");
	}

	public Delete(DatoPila d) throws InterpreteExcepcion {
		super(InstruccionInterprete.CODIGO_DELETE, d);
		if (d.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.TIPO_INCORRECTO);
	}

	@Override
	public String toString() {
		return "delete " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion {
		Integer tam = this.getDato().getEntero();
		if (interprete.getPila().peek().getTipo() == DatoPila.INT) {
			Integer dir = interprete.getPila().getFirst().getEntero();
			interprete.liberar(dir, tam);
		} else
			throw new InterpreteExcepcion(this.toString(),
					"El tamaño no se ha identificado de la forma adecuada. Debe ser un entero.");
		return true;
	}

}
