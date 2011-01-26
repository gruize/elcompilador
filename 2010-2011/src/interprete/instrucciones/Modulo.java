package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

import compilador.gestorErrores.GestorErrores;

public class Modulo extends InstruccionInterprete {

	public Modulo(){
		super(InstruccionInterprete.CODIGO_MODULO);
	}

	public Modulo(DatoPila d){
		super(InstruccionInterprete.CODIGO_MODULO);
		GestorErrores.agregaError("La instrucción no acepta argumentos");
	}

	@Override
	public String toString() {
		return "modulo";
	}

	@Override
	/*
	 * Tanto los operandos como el resultado deben ser de tipo entero
	 */
	public boolean ejecutate(Interprete interprete){

		DatoPila d1 = interprete.getPila().pop();

		if (d1.getTipo() != DatoPila.INT)
			GestorErrores.agregaError(
					"Los operandos de esta instrucción deben ser enteros");

		DatoPila d2 = interprete.getPila().pop();

		if (d2.getTipo() != DatoPila.INT)
			GestorErrores.agregaError(
					"Los operandos de esta instrucción deben ser enteros");

		DatoPila resultado = new DatoPila(DatoPila.INT,
				(Integer) (d1.getEntero() % d2.getEntero()));

		interprete.getPila().push(resultado);

		return true;
	}

}
