package es.ucm.plg.compilador.analizadorSintactico;

import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo.Modo;

public class Params {

	Modo modo;
	Tipo tipo;
	
	public Params(){		 
	}
	
	public Params(Modo modo, Tipo tipo) {
		super();
		this.modo = modo;
		this.tipo = tipo;
	}

	public Modo getModo() {
		return modo;
	}

	public void setModo(Modo modo) {
		this.modo = modo;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	
}
