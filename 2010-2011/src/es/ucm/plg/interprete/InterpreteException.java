package es.ucm.plg.interprete;

import es.ucm.plg.interprete.datoPila.DatoPila;

public class InterpreteException extends Exception {

	private static final long serialVersionUID = -8370399403488712724L;
	
	private InstruccionInterprete instruccion;
	private DatoPila dato;
	

	public InterpreteException(String message) {
		super(message);
	}

	public InterpreteException(InstruccionInterprete instruccion, String message) {
		super(message);
		this.instruccion = instruccion;
	}

	public InterpreteException(DatoPila dato, String message) {
		super(message);
		this.dato = dato;
	}

	@Override
	public String toString() {
		return "Error en el intérprete "
				+ (instruccion == null ? " en la instrucción "
						+ Byte.toString(instruccion.getTipoIns()) : "")
				+ (dato == null ? " en el dato " + dato.toString() : "")
				+ " : " + super.getMessage();
	}

}
