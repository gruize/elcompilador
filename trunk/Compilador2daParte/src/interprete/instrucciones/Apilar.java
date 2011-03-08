package interprete.instrucciones;

import compilador.gestorErrores.GestorErrores;
import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

public class Apilar extends InstruccionInterprete {

	public Apilar(){
		super(InstruccionInterprete.CODIGO_APILAR);
		GestorErrores.agregaError("La instruccion apilar necesita un parametro");
	}

	public Apilar(DatoPila d) {
		super(InstruccionInterprete.CODIGO_APILAR, d);
	}
	
	@Override
	public String toString() {
		return "apila " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		interprete.getPila().push(this.getDato());
		return true;
	}

}
