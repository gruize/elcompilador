package interprete.instrucciones;

import interprete.Interprete;
import interprete.datoPila.DatoPila;

public class ApilarDir extends InstruccionInterprete {

	public ApilarDir() throws Exception {
		super(InstruccionInterprete.CODIGO_APILARDIR);
		throw new Exception("La instrucción apiladir necesita un parámetro");
	}

	public ApilarDir(DatoPila d) {
		super(InstruccionInterprete.CODIGO_APILARDIR, d);
	}
	
	@Override
	public String toString() {
		return "apiladir " + this.getDato().getValor();
	}
	
	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
