package maquinaP;

import java.util.Hashtable;
import java.util.Vector;

public class Memoria {

	//TODO: Por terminar 
	
	private Integer tam_max = 500;
	private Hashtable<Integer,Object> memoria;
	private Vector<Cabecera> cabecera;
	private Integer tam_cab = 500;
	
	public Memoria() {
		memoria = new Hashtable<Integer,Object>();
		cabecera = new Vector<Cabecera>();
	}
	
	public Integer getTam_max() {
		return tam_max;
	}

	public void setTam_max(Integer tam_max) {
		this.tam_max = tam_max;
	}

	public Hashtable<Integer, Object> getMemoria() {
		return memoria;
	}

	public void setMemoria(Hashtable<Integer, Object> memoria) {
		this.memoria = memoria;
	}

	public Vector<Cabecera> getCabecera() {
		return cabecera;
	}

	public void setCabecera(Vector<Cabecera> cabecera) {
		this.cabecera = cabecera;
	}

	public Integer getTam_cab() {
		return tam_cab;
	}

	public void setTam_cab(Integer tam_cab) {
		this.tam_cab = tam_cab;
	}

	public Object obtenerPosicion(Integer posicion){
		Object valor_posicion = null;
		valor_posicion = memoria.get(posicion);
		return valor_posicion;
	}
	
	public void modificarPosicion(Integer posicion, Object valor){
		memoria.put(posicion, valor);
	}
	
	public Integer reservar(Integer tam_reserva) throws Exception{
		Integer direccion = null;
		Integer tamanyo = null;
		boolean reservado = false;
		int i = 0;
		while(!reservado && i < cabecera.size()){
			if(cabecera.get(i).getTamanyo() - tam_reserva < tam_max &&
				cabecera.get(i).getTamanyo() - tam_reserva >= 0){
				reservado = true;
				direccion = cabecera.get(i).getInicio();
				tamanyo = cabecera.get(i).getTamanyo();
			}				
			i++;
		}
		if(reservado && tamanyo < tam_max){
			for(int j = 0; j < cabecera.size(); j++){
				if(cabecera.get(j).getInicio() == direccion){
					//Objeto a modificar
					if(cabecera.get(j).getTamanyo() - tam_reserva > 0){
						cabecera.get(j).setInicio(cabecera.get(j).getInicio() - tamanyo);
						cabecera.get(j).setTamanyo(cabecera.get(j).getTamanyo() - tamanyo);
					}else{
						cabecera.remove(j);
					}
				}
			}			
		}else
			throw new Exception("Overflow");
		return direccion - tamanyo;
	}
	
	public void liberar(Integer direccion, Integer tamanyo){
		boolean liberado = false;
		int i = 0;
		while(!liberado && i < cabecera.size()){
			if(cabecera.get(i).getInicio() - cabecera.get(i).getTamanyo() == direccion + tamanyo){
				cabecera.get(i).setTamanyo(cabecera.get(i).getTamanyo() + tamanyo);
				liberado = true;
			}
			i++;
		}
		if(!liberado)
			cabecera.add(new Cabecera(direccion+tamanyo,tamanyo));
	}
	
	public String escribir(){
		return this.memoria.toString();
	}
}
