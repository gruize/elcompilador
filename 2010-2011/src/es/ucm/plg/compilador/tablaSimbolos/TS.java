package es.ucm.plg.compilador.tablaSimbolos;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import es.ucm.plg.compilador.tablaSimbolos.Detalles.Clase;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;

public class TS {

	private Hashtable<String, Detalles> tabla;

	public TS() {
		tabla = new Hashtable<String, Detalles>();
	}

	public Integer annadeID(String id, Integer dir, Tipo tipo, Clase clase,
			int nivel) {
		tabla.put(id, new Detalles(id, dir, tipo, clase, nivel));
		return dir + tipo.getTamanyo();
	}
	
	public Integer annadeID(String id, Integer dir, Tipo tipo, Clase clase,
			int nivel, int inicio) {
		tabla.put(id, new Detalles(id, dir, tipo, clase, nivel, inicio));
		return dir + tipo.getTamanyo();
	}

	public Tipo getTipo(String id) {
		return tabla.get(id).getTipo();
	}

	public Integer getDir(String id) {
		return tabla.get(id).getDir();
	}

	public Boolean existeID(String id) {
		return tabla.containsKey(id);
	}

	public Detalles getDetalles(String id) {
		return tabla.get(id);
	}

	public void salidaTS() {
		Set<String> clave = tabla.keySet();
		Iterator<String> it = clave.iterator();
		String clave_actual;
		Detalles details;
		while (it.hasNext()) {
			clave_actual = it.next();
			details = tabla.get(clave_actual);
			System.out.println(details.toString());
		}
	}

	public void limpiar() {
		this.tabla.clear();
	}

	public Hashtable<String, Detalles> getTabla() {
		return tabla;
	}

	public void setTabla(Hashtable<String, Detalles> tabla) {
		this.tabla = tabla;
	}
	
	

}
