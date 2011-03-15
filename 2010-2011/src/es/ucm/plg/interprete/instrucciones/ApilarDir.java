package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class ApilarDir extends InstruccionInterprete {

	public ApilarDir(){
		super(InstruccionInterprete.CODIGO_APILARDIR);
		GestorErrores.agregaError("La instruccion apiladir necesita un parametro");
	}

	public ApilarDir(DatoPila d){
		super(InstruccionInterprete.CODIGO_APILARDIR, d);
		if (d.getTipo() != DatoPila.INT)
			GestorErrores.agregaError("El parametro de esta instruccion debe ser de tipo entero");
	}

	@Override
	public String toString() {
		return "apiladir " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) {

		DatoPila dato = interprete.getMemoria()[this.getDato().getEntero()];

		// Si en la posicion de memoria especificada no existen ningun valor
		// apilo un valor representativo, como es Integer.MIN_VALUE
		if (dato == null) {
			interprete.getPila().addFirst(
					new DatoPila(DatoPila.INT, Integer.MIN_VALUE));
		} else {
			interprete.getPila().push(
					interprete.getMemoria()[this.getDato().getEntero()]);
		}
		return true;
	}

}
