package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

import compilador.gestorErrores.GestorErrores;

public class Y_Logica extends InstruccionInterprete {

	public Y_Logica(){
		super(InstruccionInterprete.CODIGO_Y);
	}

	public Y_Logica(DatoPila d){
		super(InstruccionInterprete.CODIGO_Y);
		GestorErrores.agregaError("La instruccion no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return "y";
	}

	@Override
	public boolean ejecutate(Interprete interprete){

		DatoPila d1 = interprete.getPila().pop();

		if (d1.getTipo() != DatoPila.INT)
			GestorErrores.agregaError(
					"Los operandos de esta instruccion deben ser enteros");

		if (!(d1.getEntero() == 0 || d1.getEntero() == 1))
			GestorErrores.agregaError(
					"Los operandos de esta instruccion deben ser 0 o 1");

		DatoPila d2 = interprete.getPila().pop();

		if (d2.getTipo() != DatoPila.INT)
			GestorErrores.agregaError(
					"Los operandos de esta instruccion deben ser enteros");

		if (!(d2.getEntero() == 0 || d2.getEntero() == 1))
			GestorErrores.agregaError(
					"Los operandos de esta instruccion deben ser 0 o 1");

		DatoPila resultado = new DatoPila(DatoPila.INT,
				(d1.getEntero() == 0) ? 0 : ((d2.getEntero() == 0) ? 0: 1));

		interprete.getPila().push(resultado);

		return true;

	}

}
