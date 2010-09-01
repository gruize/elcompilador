package tablaDeSimbolos.util.Tipos;

import tablaDeSimbolos.util.Clase;

public class TParam extends Tipo {

	private Tipo tipobase;
	private String modo;
	private int dir;
	
	public TParam(Tipo tipobase, String modo, int dir) {		
		this.tipobase = tipobase;
		this.modo = modo;
		this.dir = dir;
	}

	public Tipo getTipobase() {
		return tipobase;
	}

	public void setTipobase(Tipo tipobase) {
		this.tipobase = tipobase;
	}

	public String getModo() {
		return modo;
	}

	public void setModo(String modo) {
		this.modo = modo;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	@Override
	public String toString() {
		return "TParam [tipobase=" + tipobase + ", modo=" + modo + ", dir="
				+ dir + "]";
	}
	
}
