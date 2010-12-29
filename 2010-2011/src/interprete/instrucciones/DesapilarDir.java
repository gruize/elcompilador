package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class DesapilarDir extends InstruccionInterprete {

	public DesapilarDir() throws Exception {
		super(InstruccionInterprete.CODIGO_DESAPILARDIR);
		throw new Exception("La instrucción desapiladir necesita un parámetro");
	}

	public DesapilarDir(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_DESAPILARDIR, d);
		if (d.getTipo() != DatoPila.INT)
			throw new InterpreteException(
					"El parámetro de esta instrucción debe ser de tipo entero");
	}

	@Override
	public String toString() {
		return "desapiladir " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteException {

		DatoPila dato = interprete.getPila().pop();
		Integer dir = this.getDato().getEntero();

		if (dir < interprete.getMemoria().length && dir >= 0)
			interprete.getMemoria()[this.getDato().getEntero()] = dato;
		else
			throw new InterpreteException(
					"La dirección no se corresponde con una dirección válida de memoria");

		return true;
	}

}
