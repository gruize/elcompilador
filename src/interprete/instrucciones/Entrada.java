package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;

import java.io.IOException;

import compilador.gestorErrores.GestorErrores;

public class Entrada extends InstruccionInterprete {
	public Entrada() {
		super(InstruccionInterprete.CODIGO_ENTRADA);
	}

	public Entrada(DatoPila d){
		super(InstruccionInterprete.CODIGO_ENTRADA, d);
		GestorErrores.agregaError("La instruccion no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return " in ";
	}

	@Override
	public boolean ejecutate(Interprete interprete){

		String leido = "";
		DatoPila datoLeido = null;
		
		interprete.getWriter().print("Introduzca un número >>>");
		interprete.getWriter().flush();
		
		try {
			leido = interprete.getReader().readLine();
		} catch (IOException e) {
			GestorErrores.agregaError("Error al leer el dato");
		}
		
		datoLeido = new DatoPila(DatoPila.REAL, Float.valueOf(leido));
		interprete.getPila().push(datoLeido);

		return true;
	}



}
