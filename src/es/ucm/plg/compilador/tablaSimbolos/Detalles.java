package es.ucm.plg.compilador.tablaSimbolos;

import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;

public class Detalles {

	public enum Clase {
		var, type
	};

	private String id;
	private Integer dir;
	private Tipo tipo;
	private Clase clase;
	private int nivel;

	public Detalles(String id, Integer dir, Tipo tipo, Clase clase, int nivel) {
		super();
		this.id = id;
		this.dir = dir;
		this.tipo = tipo;
		this.clase = clase;
		this.nivel = nivel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getDir() {
		return dir;
	}

	public void setDir(Integer dir) {
		this.dir = dir;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Detalles [id=" + id + ", dir=" + dir + ", tipo=" + tipo
				+ ", clase=" + clase + "]";
	}

	public void setClase(Clase clase) {
		this.clase = clase;
	}

	public Clase getClase() {
		return clase;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getNivel() {
		return nivel;
	}
}
