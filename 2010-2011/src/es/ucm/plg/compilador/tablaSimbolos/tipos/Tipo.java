package es.ucm.plg.compilador.tablaSimbolos.tipos;

public class Tipo {

	protected String tipo;
	
	private int tamanyo;

	public Tipo(String tipo, int tamanyo) {
		this.tipo = tipo;
		this.tamanyo = 1;
	}
	
	public Tipo(){
		
	}

	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getTamanyo() {
		return tamanyo;
	}

	public void setTamanyo(int tamanyo) {
		this.tamanyo = tamanyo;
	}

	@Override
	public String toString() {
		return "Tipo [tipo=" + tipo + ", tamanyo=" + tamanyo + "]";
	}
	
	public boolean equals(Tipo obj){
		boolean igual = false;
		if(this.getTipo().equals(obj.getTipo()))
			igual = true;
		return igual;
	}
	
}
