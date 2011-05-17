package es.ucm.plg.compilador.tablaSimbolos.tipos;

public class Campo {
	
	private Tipo tipoBase;
	private String nombre;
	
	public Campo(Tipo tipo, String nombre) {
		this.tipoBase = tipo;
		this.nombre = nombre;
	}
	
	public void setTipoBase(Tipo tipoBase) {
		this.tipoBase = tipoBase;
	}
	
	public Tipo getTipoBase() {
		return tipoBase;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
}