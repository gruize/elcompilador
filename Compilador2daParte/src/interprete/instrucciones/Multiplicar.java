package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

import compilador.gestorErrores.GestorErrores;

public class Multiplicar extends InstruccionInterprete {

	public Multiplicar(){
		super(InstruccionInterprete.CODIGO_MULTIPLICA);
	}

	public Multiplicar(DatoPila d){
		super(InstruccionInterprete.CODIGO_MULTIPLICA);
		GestorErrores.agregaError("La instruccion no acepta argumentos");
	}

	@Override
	public String toString() {
		return "multiplica";
	}

	@Override
	/*
	 * Si ambos operandos son de tipo entero, el resultado es entero. Si ambos
	 * operandos son de tipo real, el resultado es real. Si uno de los operandos
	 * es de tipo entero y el otro es de tipo real, el resultado es real.
	 */
	public boolean ejecutate(Interprete interprete) {

		DatoPila d1 = interprete.getPila().pop();
		DatoPila d2 = interprete.getPila().pop();

		Byte tipoRes = getTipoResult(d1.getTipo(), d2.getTipo());
		DatoPila resultado;

		if (tipoRes == DatoPila.INT)
			resultado = new DatoPila(DatoPila.INT,
					(Integer) (d1.getEntero() * d2.getEntero()));
		else
			resultado = new DatoPila(DatoPila.REAL,
					(Float) (d1.getReal() * d2.getReal()));

		interprete.getPila().push(resultado);

		return true;
	}

}
