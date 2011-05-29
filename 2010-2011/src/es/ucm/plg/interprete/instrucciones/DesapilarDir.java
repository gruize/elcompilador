package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class DesapilarDir extends InstruccionInterprete {

	public DesapilarDir() {
		super(InstruccionInterprete.CODIGO_DESAPILARDIR);
	}

	public DesapilarDir(DatoPila d) throws InterpreteExcepcion {
		super(InstruccionInterprete.CODIGO_DESAPILARDIR, d);
		if (d.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.TIPO_INCORRECTO);
	}

	@Override
	public String toString() {
		return "desapiladir ";
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion {

		int dir = this.getDato().getEntero();
		DatoPila dato = interprete.getPila().pop();

	
		if (dir < interprete.getMemoria().getMemoria().length && dir >= 0)
			interprete.getMemoria().getMemoria()[dir] = dato;
		else
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.DIRECCION_INVALIDA);

		interprete.getPila().push(dato);

		return true;
	}

}
