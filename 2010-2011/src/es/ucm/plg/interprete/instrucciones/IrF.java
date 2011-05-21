package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class IrF extends InstruccionInterprete{

	public IrF(){
		super(InstruccionInterprete.CODIGO_IR_F);
		GestorErrores.agregaError("La instruccion IrF necesita un parametro");
	}

	public IrF(DatoPila d) {
		super(InstruccionInterprete.CODIGO_IR_F, d);
	}
	
	@Override
	public String toString() {
		return "ir-f " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		boolean salto = false;		
		DatoPila saltar = interprete.getPila().pop();
		if(saltar.getTipo() == DatoPila.INT && (saltar.getEntero() == 0 || saltar.getEntero() == 1)){
			if((this.getDato().getTipo() == DatoPila.INT) && (interprete.getPrograma().size() > this.getDato().getEntero())){
				if(saltar.getEntero() == 0){
					interprete.setCp(this.getDato().getEntero());
					salto = true;
				}
			}else
				GestorErrores.agregaError(302,0,0,"La direccion no se corresponde con una direccion valida de memoria");
		}else
			GestorErrores.agregaError(301,0,0,"El valor contenido en la pila no corresponde con un valor valido para las comparaciones (Distinto de 1 o 0).");          
		return salto;
	}

}
