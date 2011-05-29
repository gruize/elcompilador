package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Parar extends InstruccionInterprete {

	public Parar(){
		super(InstruccionInterprete.CODIGO_PARAR);
	}

	public Parar(DatoPila d) throws InterpreteExcepcion{
		super(InstruccionInterprete.CODIGO_PARAR, d);
		throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.SOBRA_PARAMETRO);
	}
	
	@Override
	public String toString() {
		return "parar";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		interprete.setParar(true);
        return true;
	}

}
