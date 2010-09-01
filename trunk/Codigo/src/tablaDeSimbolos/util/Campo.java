package tablaDeSimbolos.util;

import tablaDeSimbolos.util.Tipos.Tipo;

public class Campo {

	private String id;
	private Tipo tipo;
	private Integer desp;
	
	public Campo(String id, Tipo tipo, Integer desp) {
		this.id = id;
		this.tipo = tipo;
		this.desp = desp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Integer getDesp() {
		return desp;
	}

	public void setDesp(Integer desp) {
		this.desp = desp;
	}

	@Override
	public String toString() {
		return "Campo [id=" + id + ", tipo=" + tipo + ", desp=" + desp + "]";
	}
	
	public boolean equals(Campo obj){
		boolean igual = false;
		if(obj.getId().equals(this.getId()) && obj.getTipo().equals(this.getTipo()) && obj.getDesp().equals(this.getDesp()))
			igual = true;
		return igual;
	}
}
