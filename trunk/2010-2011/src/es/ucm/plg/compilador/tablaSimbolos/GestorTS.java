package es.ucm.plg.compilador.tablaSimbolos;

import java.util.ArrayDeque;
import java.util.Iterator;

public class GestorTS {

	/**
	 * La utilidad de este gestor es tener una unica instancia de la pila tabla
	 * de simbolos, por lo que se requiere el uso del patron Singlenton
	 */

	private static GestorTS gestor;
	private ArrayDeque<TS> pilaTS;
	private int n;

	public static GestorTS getInstancia() {
		if (gestor == null) {
			gestor = new GestorTS();
			gestor.pilaTS = new ArrayDeque<TS>();
			gestor.pilaTS.push(new TS());
		}
		return gestor;
	}

	private GestorTS() {
	}

	public int getN() {
		return n;
	}

	public void nuevoAmbito() {
		gestor.pilaTS.push(new TS());
		gestor.n++;
	}

	public void cerrarAmbitoActual() {
		gestor.pilaTS.pop();
		gestor.n--;
	}

	public Detalles buscaGlobal(String id) {

		Iterator<TS> iterator = gestor.pilaTS.iterator();
		TS ts = null;
		Detalles detalles = null;

		while (iterator.hasNext() && detalles == null) {
			ts = iterator.next();
			detalles = ts.getDetalles(id);
		}

		return detalles;
	}

	public TS ts() {
		return gestor.pilaTS.getFirst();
	}

	public boolean existe(String id) {

		Iterator<TS> iterator = gestor.pilaTS.iterator();
		boolean resultado = false;

		while (iterator.hasNext() && resultado == false) {
			TS actual = iterator.next();
			resultado = actual.existeID(id);
		}
		return resultado;
	}

}
