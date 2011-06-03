package es.ucm.plg.interprete.instrucciones;

import java.io.IOException;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Entrada extends InstruccionInterprete {

	public Entrada(DatoPila d){
		super(InstruccionInterprete.CODIGO_ENTRADA, d);
	}
	
	@Override
	public String toString() {
		return " in ";
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion{

		String leido = "";
		DatoPila datoLeido = null;
		
		interprete.getWriter().print("Introduzca un numero >>>");
		interprete.getWriter().flush();
		
		try {
			leido = interprete.getReader().readLine();
		} catch (IOException e) {
			throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.LECTURA_ESCRITURA);
		}
		
		datoLeido = new DatoPila(DatoPila.REAL, Float.valueOf(leido));

		Integer dir = this.getDato().getEntero();

		if (dir < interprete.getMemoria().getMemoria().length && dir >= 0)
			interprete.getMemoria().getMemoria()[this.getDato().getEntero()] = datoLeido;
		else
			throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.DIRECCION_INVALIDA);

		interprete.getPila().push(datoLeido);

		return true;
	}



}
