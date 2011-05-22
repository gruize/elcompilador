package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Mueve extends InstruccionInterprete{

	public Mueve(){
		super(InstruccionInterprete.CODIGO_MUEVE);
		GestorErrores.agregaError("La instruccion Mueve necesita un parametro");
	}

	public Mueve(DatoPila d) {
		super(InstruccionInterprete.CODIGO_MUEVE, d);
		if (d.getTipo() != DatoPila.INT)
			GestorErrores.agregaError(
					"El parametro de esta instruccion debe ser de tipo entero");
	}
	
	@Override
	public String toString() {
		return "mueve " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		boolean ok = false;
		DatoPila origen = interprete.getPila().pop();
		DatoPila destino = interprete.getPila().pop();
		if(origen.getTipo() == DatoPila.INT && destino.getTipo() == DatoPila.INT){
			if(origen.getEntero() >= 0 && origen.getEntero() < interprete.getMemoria().length){
				if(destino.getEntero() >= 0 && destino.getEntero() < interprete.getMemoria().length){
					if(this.getDato().getTipo() == DatoPila.INT && this.getDato().getEntero() > 0){
						if((origen.getEntero() + this.getDato().getEntero()) < interprete.getMemoria().length ||
								(destino.getEntero() + this.getDato().getEntero()) < interprete.getMemoria().length){
							for(int i = 0; i < this.getDato().getEntero(); i++)
								interprete.getMemoria()[destino.getEntero() + i] = interprete.getMemoria()[origen.getEntero() + i];
							ok = true;
						}else
							GestorErrores.agregaError("Movimiento imposible. Valor de memoria inexistente.");
					}else
						GestorErrores.agregaError("El tamaño identificado no es valido. Valor de memoria inexistente.");
				}else
					GestorErrores.agregaError("Las direccion de destino no es valida. Valor de memoria inexistente.");
			}else
				GestorErrores.agregaError("Las direccion de origen no es valida. Valor de memoria inexistente.");
		}else
			if(origen.getTipo() != DatoPila.INT)
				GestorErrores.agregaError("Las direccion de origen debe ser un valor entero.");
			else
				GestorErrores.agregaError("Las direccion de destino debe ser un valor entero.");
		return ok;
	}
	
}
