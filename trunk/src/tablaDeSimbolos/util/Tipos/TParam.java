package tablaDeSimbolos.util.Tipos;

import tablaDeSimbolos.util.Clase;

public class TParam extends Tipo {

	private Tipo tipobase;
	private String modo;
	
	public TParam(Tipo tipobase, String modo) {
		this.tipobase = tipobase;
		this.modo = modo;
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

	@Override
	public String toString() {
		return "TParam [tipobase=" + tipobase + ", modo=" + modo + ", tipo="
				+ tipo + "]";
	}
	
}
