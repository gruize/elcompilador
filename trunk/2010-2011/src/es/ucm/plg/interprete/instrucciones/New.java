package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class New extends InstruccionInterprete{

	public New(){
		super(InstruccionInterprete.CODIGO_NEW);
		GestorErrores.agregaError("La instruccion New necesita un parametro");
	}

	public New(DatoPila d){
		super(InstruccionInterprete.CODIGO_NEW, d);
		if (d.getTipo() != DatoPila.INT)
			GestorErrores.agregaError("El parametro de esta instruccion debe ser de tipo entero");
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
