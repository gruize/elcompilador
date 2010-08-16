package tablaDeSimbolos.util.Tipos;

import java.util.Vector;

import tablaDeSimbolos.util.Campo;
import tablaDeSimbolos.util.Clase;

public class Tipo {

	protected TipoDec tipo;
	
	private int tamanyo;

	public Tipo(TipoDec tipo, int tamanyo) {
		this.tipo = tipo;
		this.tamanyo = tamanyo;
	}
	
	public Tipo(){
		
	}

	public TipoDec getTipo() {
		return tipo;
	}
	
	public Tipo getTipoProc() {
		return null;
	}

	public Clase getModo() {
		return null;
	}
	
	public Vector<TParam> getParametros() {
		return null;
	}

	public void setTipo(TipoDec tipo) {
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
		if(this.getTipo().equals(obj.getTipo()) && this.getTamanyo() == obj.getTamanyo())
			igual = true;
		return igual;
	}
	
	public Integer getNelems() {
		return null;
	}
	
	public Tipo getTipobase() {
		return null;
	}
	
	public Vector<Campo> getCampos() {
		return null;
	}
	
	public boolean existeCampo(String id){
		boolean existe = false;
		return existe;
	}
	
	public Campo getCampo(String id){
		Campo temp = null;
		return temp;
	}

	public String getId(){
		return null;
	}
	
	public String getIdRef(){
		return null;
	}
	
	public void setIdRef(String id) {}
}
