package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class DesapilarInd extends InstruccionInterprete{
	
	public DesapilarInd(){
		super(InstruccionInterprete.CODIGO_DESAPILARIND);
	}

	public DesapilarInd(DatoPila d){
		super(InstruccionInterprete.CODIGO_DESAPILARIND);
		GestorErrores.agregaError("La instruccion DesapilarInd no acepta argumentos");
	}

	@Override
	public String toString() {
		return "desapilarInd";
	}
	
	@Override
	public boolean ejecutate(Interprete interprete) {
		DatoPila dato = interprete.getPila().pop();
		if(interprete.getPila().peek().getTipo() == DatoPila.INT){
			Integer dir = interprete.getPila().pop().getEntero();
			interprete.getMemoria().getMemoria()[dir] = dato;
		}else
			GestorErrores.agregaError("La direccion de memoria debe ser un entero.");
		return true;
	}
	
}
