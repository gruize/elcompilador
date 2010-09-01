package tablaDeSimbolos.util;

import tablaDeSimbolos.util.Tipos.Tipo;

public class Detalles {

	private String id;
	private Clase clase;
	private Integer dir;
	private Tipo tipo;
	private int nivel;
	
	public Detalles(String id, Clase clase, Integer dir, Tipo tipo, int nivel) {
		super();
		this.id = id;
		this.clase = clase;
		this.dir = dir;
		this.tipo = tipo;
		this.nivel = nivel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Clase getClase() {
		return clase;
	}

	public void setClase(Clase clase) {
		this.clase = clase;
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

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	@Override
	public String toString() {
		return "Detalles [id=" + id + ", clase=" + clase + ", dir=" + dir
				+ ", tipo=" + tipo + ", nivel=" + nivel + "]";
	}
		
}
