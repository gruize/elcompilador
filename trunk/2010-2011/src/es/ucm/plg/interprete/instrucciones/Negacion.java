package es.ucm.plg.interprete.instrucciones;

import java.util.ArrayDeque;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Negacion extends InstruccionInterprete {

	public Negacion(){
		super(InstruccionInterprete.CODIGO_NEGACION);
	}

	public Negacion(DatoPila d){
		super(InstruccionInterprete.CODIGO_NEGACION);
		GestorErrores.agregaError("La instrucción no acepta argumentos");
	}

	@Override
	public String toString() {
		return "negacion";
	}

	@Override
	/**
	 * Reglas de tipo:
	 * El tipo del operando y el del resultado ha de ser entero.
	 * 
	 * Semantica:
	 * d = desapilar
	 * if(d no es entero)
	 *      throw new InstruccionExc
	 * if(d es 1)
	 *      apilar(0)
	 * else if (d es 0)
	 *      apilar(1)
	 *
	 * @return siempre true (nunca modifica el cp del interprete)
	 * @throws InstruccionExc si encuentra tipos de datos no booleanos
	 */
	public boolean ejecutate(Interprete interprete){
		ArrayDeque<DatoPila> pila = interprete.getPila();
		DatoPila d = pila.pop();
		DatoPila res = null;

		switch (d.getTipo()) {
		case DatoPila.INT:
			res = new DatoPila(DatoPila.INT, (d.getValor().equals(1) ? 0 : 1));
			break;
		default:
			GestorErrores.agregaError("Tipo inválido ("
					+ d.toString() + ")");
		}
		pila.addFirst(res);
		return true;
	}

}
