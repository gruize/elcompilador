package compilador.tablaSimbolos;

import compilador.tablaSimbolos.tipos.Tipo;

public class Detalles {
	
	private String id;
	private Integer dir;
	private Tipo tipo;
	
	public Detalles(String id, Integer dir, Tipo tipo) {
		super();
		this.id = id;
		this.dir = dir;
		this.tipo = tipo;
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
		return "Detalles [id=" + id + ", dir=" + dir + ", tipo=" + tipo + "]";
	}
}
