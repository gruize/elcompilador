package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class DesapilarDir extends InstruccionInterprete {

	public DesapilarDir(){
		super(InstruccionInterprete.CODIGO_DESAPILARDIR);
		GestorErrores.agregaError("La instrucción desapiladir necesita un parámetro");
	}

	public DesapilarDir(DatoPila d){
		super(InstruccionInterprete.CODIGO_DESAPILARDIR, d);
		if (d.getTipo() != DatoPila.INT)
			GestorErrores.agregaError(
					"El parámetro de esta instrucción debe ser de tipo entero");
	}

	@Override
	public String toString() {
		return "desapiladir " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete){

		DatoPila dato = interprete.getPila().getLast();
		Integer dir = this.getDato().getEntero();

		if (dir < interprete.getMemoria().length && dir >= 0)
			interprete.getMemoria()[this.getDato().getEntero()] = dato;
		else
			GestorErrores.agregaError(
					"La dirección no se corresponde con una dirección válida de memoria");

		return true;
	}

}
