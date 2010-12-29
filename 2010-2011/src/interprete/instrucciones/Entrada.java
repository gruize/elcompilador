package interprete.instrucciones;

import java.io.IOException;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Entrada extends InstruccionInterprete {
	public Entrada() {
		super(InstruccionInterprete.CODIGO_ENTRADA);
	}

	public Entrada(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_ENTRADA, d);
		throw new InterpreteException("La instruccion no acepta argumentos");
	}
	
	@Override
	public String toString() {
		return " in ";
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteException {

		String leido;
		DatoPila datoLeido;
		
		interprete.getWriter().print("Introduzca un número >>>");
		interprete.getWriter().flush();
		
		try {
			leido = interprete.getReader().readLine();
		} catch (IOException e) {
			throw new InterpreteException("Error al leer el dato");
		}
		
		datoLeido = new DatoPila(DatoPila.REAL, Float.valueOf(leido));
		interprete.getPila().push(datoLeido);

		return true;
	}



}
