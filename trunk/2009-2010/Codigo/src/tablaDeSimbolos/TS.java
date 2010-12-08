package tablaDeSimbolos;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import tablaDeSimbolos.util.Clase;
import tablaDeSimbolos.util.Detalles;
import tablaDeSimbolos.util.Tipos.Tipo;
import tablaDeSimbolos.util.Tipos.TipoDec;

public class TS {

		private Hashtable<String,Detalles> tabla;
		
		public TS() {
			tabla = new Hashtable<String,Detalles>();
		}
		
		public void añadeID(String id, Clase clase, Integer dir, Tipo tipo, int nivel){
			tabla.put(id, new Detalles(id,clase,dir,tipo,nivel));
		}
		
		public Tipo getTipo(String id){
			return tabla.get(id).getTipo();
		}
		
		public Integer getDir(String id){
			return tabla.get(id).getDir();
		}
		
		public Clase getClase(String id){
			return tabla.get(id).getClase();
		}
		
		public int getNivel(String id){
			return tabla.get(id).getNivel();
		}
		
		public Boolean existeID(String id){
			return tabla.containsKey(id);
		}
		
		public Detalles getDetalles(String id){
			return tabla.get(id);
		}
		 
		public void combinaProps(Detalles p1, Detalles p2){
			//TODO: En la memoria se especifica:
			/**
			 * p1 y p2 son registros de propiedades, 
			 * y esta función devolverá las propiedades que resultan
			 * de añadir a p1 los campos de p2 que no contiene
			 */				
		}
		
		public boolean esTipo(Tipo obj){
			boolean esTipo = false;
			Set<String> clave = tabla.keySet();
			Iterator<String> busqueda = clave.iterator();
			String clave_actual;
			Detalles details;
			while(!esTipo && busqueda.hasNext()){
				clave_actual = busqueda.next();
				details = tabla.get(clave_actual);
				if(details.getTipo().equals(obj))
					esTipo = true;
			}
			return esTipo;
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
		
		public boolean refErronea(Tipo tip){
			boolean ref = false;
			if(!existeID(tip.getId()) && tip.getTipo().equals(TipoDec.TipoReferencia))
				ref = true;
			return ref;
		}
}
