package util;

import java.util.Hashtable;
import java.util.Vector;
import util.util.Cabecera;

public class MemoriaPORTERMINAR {

	//TODO: Por terminar 
	
	private Integer tam_max = 500;
	private Hashtable<Integer,Object> memoria;
	private Vector<Cabecera> cabecera;
	private Integer tam_cab = 500;
	
	public MemoriaPORTERMINAR() {
		memoria = new Hashtable<Integer,Object>();
		cabecera = new Vector<Cabecera>();
	}
	
	public Object obtenerPosicion(Integer posicion){
		Object valor_posicion = null;
		valor_posicion = memoria.get(posicion);
		return valor_posicion;
	}
	
	public void modificarPosicion(Integer posicion, Object valor){
		memoria.put(posicion, valor);
	}
	
}
