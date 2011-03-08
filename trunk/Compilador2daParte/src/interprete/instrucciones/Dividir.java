package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

import compilador.gestorErrores.GestorErrores;

public class Dividir extends InstruccionInterprete {

	public Dividir(){
		super(InstruccionInterprete.CODIGO_DIVIDE);
	}

	public Dividir(DatoPila d){
		super(InstruccionInterprete.CODIGO_DIVIDE);
		GestorErrores.agregaError("La instruccion no acepta argumentos");
	}

	@Override
	public String toString() {
		return "dividir";
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila d1 = interprete.getPila().pop();
		DatoPila d2 = interprete.getPila().pop();

		Byte tipoRes = getTipoResult(d1.getTipo(), d2.getTipo());
		DatoPila resultado;

		if (tipoRes == DatoPila.INT)
			resultado = new DatoPila(DatoPila.INT,
					(Integer) (d1.getEntero() / (Integer) d2
							.getEntero()));
		else
			resultado = new DatoPila(DatoPila.REAL,
					(Float) ((Float) d1.getReal() / (Float) d2.getReal()));

		interprete.getPila().push(resultado);
		
		return true;
	}
}
