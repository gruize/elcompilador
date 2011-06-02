package es.ucm.plg.compilador.tablaSimbolos;

import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;

/**
 * @author Alicia Pérez y Gabriela Ruíz Gestiona los campos de cada entrada de
 *         la tabla de símbolos
 */
public class Detalles {

	public enum Clase {
		var, type, fun, val, ref
	};

	private String id;
	private Integer dir;
	private Tipo tipo;
	private Clase clase;
	private int nivel;
	private int inicio;

	public Detalles(String id, Integer dir, Tipo tipo, Clase clase, int nivel) {
		super();
		this.id = id;
		this.dir = dir;
		this.tipo = tipo;
		this.clase = clase;
		this.nivel = nivel;
	}

	public Detalles(String id, Integer dir, Tipo tipo, Clase clase, int nivel,
			int inicio) {
		super();
		this.id = id;
		this.dir = dir;
		this.tipo = tipo;
		this.clase = clase;
		this.nivel = nivel;
		this.inicio = inicio;
	}

	public int getInicio() {
		return inicio;
	}

	public void setInicio(int inicio) {
		this.inicio = inicio;
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

	@Override
	public String toString() {
		return "id:" + id + "\t dir:" + dir + "\t tipo:" + tipo.toString()
				+ "\t clase:" + clase;
	}
}
