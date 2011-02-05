package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

import compilador.gestorErrores.GestorErrores;

public class MayorIg extends InstruccionInterprete {

	public MayorIg(){
		super(InstruccionInterprete.CODIGO_MAYORIG);
	}

	public MayorIg(DatoPila d){
		super(InstruccionInterprete.CODIGO_MAYORIG);
		GestorErrores.agregaError("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "mayorigual";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila d1 = interprete.getPila().pop();
		DatoPila d2 = interprete.getPila().pop();

		DatoPila resultado = new DatoPila(
				DatoPila.INT, (d1.getReal() >= d2.getReal() ? 1: 0));

		interprete.getPila().push(resultado);

		return true;
	}

}
