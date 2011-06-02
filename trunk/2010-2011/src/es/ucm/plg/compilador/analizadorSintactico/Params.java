package es.ucm.plg.compilador.analizadorSintactico;

import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;

public class Params {

	String modo;
	Tipo tipo;
	
	public Params(){		 
	}
	
	public Params(String modo, Tipo tipo) {
		super();
		this.modo = modo;
		this.tipo = tipo;
	}

	public String getModo() {
		return modo;
	}

	public void setModo(String modo) {
		this.modo = modo;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	
}
