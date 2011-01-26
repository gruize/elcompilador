package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

import compilador.gestorErrores.GestorErrores;

public class Parar extends InstruccionInterprete {

	public Parar(){
		super(InstruccionInterprete.CODIGO_PARAR);
	}

	public Parar(DatoPila d){
		super(InstruccionInterprete.CODIGO_PARAR, d);
		GestorErrores.agregaError(
				"La instrucción no necesita parámetros");
	}
	
	@Override
	public String toString() {
		return "parar";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {
		interprete.setParar(true);
        return true;
	}

}
