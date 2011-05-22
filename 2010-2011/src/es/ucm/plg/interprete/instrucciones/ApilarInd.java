package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class ApilarInd extends InstruccionInterprete{

	public ApilarInd(){
		super(InstruccionInterprete.CODIGO_APILARIND);
	}

	public ApilarInd(DatoPila d){
		super(InstruccionInterprete.CODIGO_APILARIND);
		GestorErrores.agregaError("La instruccion ApilarInd no acepta argumentos");
	}

	@Override
	public String toString() {
		return "apilarInd";
	}
	
	@Override
	public boolean ejecutate(Interprete interprete) {
		if(interprete.getPila().peek().getTipo() == DatoPila.INT){
			if(interprete.getPila().peek().getEntero() < interprete.getMemoria().getMemoria().length 
					|| interprete.getPila().peek().getEntero() >= 0){
				Integer dir = interprete.getPila().pop().getEntero();
				interprete.getPila().push(interprete.getMemoria().getMemoria()[dir]);
			}else
				GestorErrores.agregaError("Direccion de memoria invalida.");
		}else
			GestorErrores.agregaError("La direccion de memoria debe ser un entero.");
		return true;
	}
	
}
