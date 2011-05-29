package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class New extends InstruccionInterprete{

	public New() throws InterpreteExcepcion{
		super(InstruccionInterprete.CODIGO_NEW);
		throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.FALTA_PARAMETRO);
	}

	public New(DatoPila d) throws InterpreteExcepcion{
		super(InstruccionInterprete.CODIGO_NEW, d);
		if (d.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.TIPO_INCORRECTO);
	}

	@Override
	public String toString() {
		return "new " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete){		
		Integer tam = this.getDato().getEntero();
		interprete.getPila().push(new DatoPila(DatoPila.INT, interprete.reservar(tam)));
		return true;
	}
	
}
