/**
 * 
 */
package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Apilar extends InstruccionInterprete {

	public Apilar() throws Exception {
		super(InstruccionInterprete.CODIGO_APILAR);
		throw new Exception("La instrucción apilar necesita un parámetro");
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