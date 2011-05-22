package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Delete extends InstruccionInterprete{

	public Delete(){
		super(InstruccionInterprete.CODIGO_DELETE);
		GestorErrores.agregaError("La instruccion Delete necesita un parametro");
	}

	public Delete(DatoPila d){
		super(InstruccionInterprete.CODIGO_DELETE, d);
		if (d.getTipo() != DatoPila.INT)
			GestorErrores.agregaError("El parametro de esta instruccion debe ser de tipo entero");
	}

	@Override
	public String toString() {
		return "delete " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete){		
		Integer dir = this.getDato().getEntero();
		if(interprete.getPila().peek().getTipo() == DatoPila.INT){				
			Integer tam = interprete.getPila().pop().getEntero();
			interprete.liberar(dir,tam);
		}else
			GestorErrores.agregaError("El tamaño no se ha identificado de la forma adecuada. Debe ser un entero.");
		return true;
	}
	
}
