package es.ucm.plg.compilador.tablaSimbolos;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;

public class TS {
	
	private Hashtable<String,Detalles> tabla;
	
	public TS() {
		tabla = new Hashtable<String,Detalles>();
	}
	
	public Integer annadeID(String id, Integer dir, Tipo tipo){
		tabla.put(id, new Detalles(id,dir,tipo));
		Integer prim_dir_disponible = dir + tipo.getTamaño();
		return prim_dir_disponible;
	}
	
	public Tipo getTipo(String id){
		return tabla.get(id).getTipo();
	}
	
	public Integer getDir(String id){
		return tabla.get(id).getDir();
	}
		
	public Boolean existeID(String id){
		return tabla.containsKey(id);
	}
	
	public Detalles getDetalles(String id){
		return tabla.get(id);
	}
	 	
	public void salidaTS(){
		Set<String> clave = tabla.keySet();
		Iterator<String> it = clave.iterator();
		String clave_actual;
		Detalles details;
		while(it.hasNext()){
			clave_actual = it.next();
			details = tabla.get(clave_actual);
			System.out.println(details.toString());				
		}
	}
	
}
