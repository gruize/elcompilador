package es.ucm.plg.interprete.instrucciones;

import java.io.IOException;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Entrada extends InstruccionInterprete {
	public Entrada() {
		super(InstruccionInterprete.CODIGO_ENTRADA);
	}

	public Entrada(DatoPila d){
		super(InstruccionInterprete.CODIGO_ENTRADA, d);
		GestorErrores.agregaError("La instruccion Entrada/Lectura no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return " in ";
	}

	@Override
	public boolean ejecutate(Interprete interprete){

		String leido = "";
		DatoPila datoLeido = null;
		
		interprete.getWriter().print("Introduzca un numero >>>");
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
