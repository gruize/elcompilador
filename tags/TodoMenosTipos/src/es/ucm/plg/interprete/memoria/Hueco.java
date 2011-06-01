package es.ucm.plg.interprete.memoria;

@SuppressWarnings("rawtypes")
public class Hueco implements Comparable{

	private int dir;
	private int tam;
		
	public Hueco(int dir, int tam) {
		this.dir = dir;
		this.tam = tam;
	}

	public int getDir() {
		return dir;
	}
	
	public void setDir(int dir) {
		this.dir = dir;
	}
	
	public int getTam() {
		return tam;
	}
	
	public void setTam(int tam) {
		this.tam = tam;
	}

	@Override
	public int compareTo(Object o) {
		Hueco hueco = (Hueco)o;
		if(this.tam > hueco.tam)
			return 1;
		else
			if(this.tam == hueco.tam)
				return 0;
			else
				return -1;
	}	
	
}
