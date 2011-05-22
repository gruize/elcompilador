package es.ucm.plg.interprete.memoria;

import java.util.Collections;
import java.util.Vector;

import es.ucm.plg.interprete.datoPila.DatoPila;

public class Memoria {

	private DatoPila[] memoria;
	private Vector<Hueco> huecos;
	
	public Memoria(int longMem){
		this.memoria = new DatoPila[longMem];
		this.huecos = new Vector<Hueco>();
		int primerHueco = (int) (longMem * 70 * 0.01);
		huecos.add(new Hueco(primerHueco, longMem - primerHueco));
	}

	public DatoPila[] getMemoria() {
		return memoria;
	}

	public void setMemoria(DatoPila[] memoria) {
		this.memoria = memoria;
	}

	public Vector<Hueco> getHuecos() {
		return huecos;
	}

	public void setHuecos(Vector<Hueco> huecos) {
		this.huecos = huecos;
	}
	
	public Integer reservar(Integer tam) {
		boolean reservado = false;
		Integer direccion = -1;		
		int i = 0;		
		Collections.sort(this.huecos);
		while(!reservado && i < this.huecos.size()){
			if(this.huecos.get(i).getTam() >= tam){
				reservado = true;
				direccion = huecos.get(i).getDir();
				if(this.huecos.get(i).getTam() - tam > 0){
					this.huecos.get(i).setDir(this.huecos.get(i).getDir() + tam);
					this.huecos.get(i).setTam(this.huecos.get(i).getTam() - tam);
				}else{
					this.huecos.remove(i);
				}			
			}
			i++;
		}		
		return direccion;
	}

	public void liberar(Integer dir, Integer tam) {
		boolean liberado = false;
		int i = 0;
		while(!liberado && i < this.huecos.size()){
			if((this.huecos.get(i).getDir() == dir + tam) ||
				(this.huecos.get(i).getDir() + this.huecos.get(i).getTam() == dir)){
				liberado = true;
				if(this.huecos.get(i).getDir() == dir + tam)
					this.huecos.get(i).setDir(dir);
				this.huecos.get(i).setTam(this.huecos.get(i).getTam() + tam);									
			}
			i++;
		}
		if(!liberado){
			this.huecos.add(new Hueco(dir, tam));			
		}
		unifica();
	}

	public void unifica() {
		Collections.sort(this.huecos, new HuecosDireccionComparator());
		int i = 1;
		while(i < this.huecos.size()){
			if(this.huecos.get(i - 1).getDir() + this.huecos.get(i - 1).getTam() == this.huecos.get(i).getDir()){
				//Fusionar ambos huecos
				this.huecos.get(i - 1).setTam(this.huecos.get(i - 1).getTam() + this.huecos.get(i).getTam());				
				this.huecos.remove(i);
			}else
				i++;
		}
	}	
	
}
