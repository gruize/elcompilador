package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

import java.util.ArrayDeque;

public class Negacion extends InstruccionInterprete {

	public Negacion() throws InterpreteException {
		super(InstruccionInterprete.CODIGO_NEGACION);
	}

	public Negacion(DatoPila d) throws InterpreteException {
		super(InstruccionInterprete.CODIGO_NEGACION);
		throw new InterpreteException("La instrucción no acepta argumentos");
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
	public boolean ejecutate(Interprete interprete) throws InterpreteException {
		ArrayDeque<DatoPila> pila = interprete.getPila();
		DatoPila d = pila.pop();
		DatoPila res;

		switch (d.getTipo()) {
		case DatoPila.INT:
			res = new DatoPila(DatoPila.INT, (d.getValor().equals(1) ? 0 : 1));
			break;
		default:
			throw new InterpreteException(this, "Tipo inválido ("
					+ d.toString() + ")");
		}
		pila.addFirst(res);
		return true;
	}

}
