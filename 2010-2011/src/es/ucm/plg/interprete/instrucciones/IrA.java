package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class IrA extends InstruccionInterprete{

	public IrA(){
		super(InstruccionInterprete.CODIGO_IR_A);
		GestorErrores.agregaError("La instruccion IrA necesita un parametro");
	}

	public IrA(DatoPila d) {
		super(InstruccionInterprete.CODIGO_IR_A, d);
	}
	
	@Override
	public String toString() {
		return "ir-a " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		boolean salto = false;						
		if((this.getDato().getTipo() == DatoPila.INT) && (interprete.getPrograma().size() > this.getDato().getEntero())){
			interprete.setCp(this.getDato().getEntero());
			salto = true;
		}else
			GestorErrores.agregaError(302,0,0,"La direccion no se corresponde con una direccion valida de memoria");
		return salto;
	}
	
}
