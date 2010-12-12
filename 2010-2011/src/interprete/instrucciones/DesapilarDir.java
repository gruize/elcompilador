package interprete.instrucciones;

import interprete.Interprete;
import interprete.datoPila.DatoPila;

public class DesapilarDir extends InstruccionInterprete {

	public DesapilarDir() throws Exception {
		super(InstruccionInterprete.CODIGO_DESAPILARDIR);
		throw new Exception("La instrucción desapiladir necesita un parámetro");
	}

	public DesapilarDir(DatoPila d) {
		super(InstruccionInterprete.CODIGO_DESAPILARDIR, d);
	}
	
	@Override
	public String toString() {
		return "desapiladir " + this.getDato().getValor();
	}
	
	@Override
	public boolean ejecutate(Interprete interprete) {
		// TODO Auto-generated method stub
		return false;
	}

}
