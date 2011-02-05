package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

import compilador.gestorErrores.GestorErrores;

public class MenorIg extends InstruccionInterprete {

	public MenorIg(){
		super(InstruccionInterprete.CODIGO_MENORIG);
	}

	public MenorIg(DatoPila d){
		super(InstruccionInterprete.CODIGO_MENORIG);
		GestorErrores.agregaError("La instrucción no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "menorigual";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila d1 = interprete.getPila().pop();
		DatoPila d2 = interprete.getPila().pop();

		DatoPila resultado = new DatoPila(
				DatoPila.INT, (d1.getReal() <= d2.getReal() ? 1: 0));

		interprete.getPila().push(resultado);

		return true;
	}

}
