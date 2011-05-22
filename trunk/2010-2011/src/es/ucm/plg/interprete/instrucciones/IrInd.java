package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class IrInd extends InstruccionInterprete{

	public IrInd(){
		super(InstruccionInterprete.CODIGO_IR_IND);
		GestorErrores.agregaError("La instruccion IrInd necesita un parametro");
	}

	public IrInd(DatoPila d) {
		super(InstruccionInterprete.CODIGO_IR_IND, d);
		if (d.getTipo() != DatoPila.INT)
			GestorErrores.agregaError(
					"El parametro de esta instruccion debe ser de tipo entero");		
	}
	
	@Override
	public String toString() {
		return "ir-ind " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		DatoPila dir = interprete.getPila().pop();
		if(dir.getTipo() == DatoPila.INT){
			if(dir.getEntero() < interprete.getPrograma().size() && dir.getEntero() >= 0)
				interprete.setCp(dir.getEntero());
			else
				GestorErrores.agregaError(302,0,0,"La direccion no se corresponde con una posicion valida de programa");
		}else
			GestorErrores.agregaError(302,0,0,"La direccion no se corresponde con una direccion valida de memoria");
		return false;
	}
	
}
