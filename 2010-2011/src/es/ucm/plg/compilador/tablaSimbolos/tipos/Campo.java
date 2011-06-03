package es.ucm.plg.compilador.tablaSimbolos.tipos;

public class Campo {
	
	private Tipo tipoBase;
	private String nombre;
	private int desplazamiento;
	
	public Campo(Tipo tipo, String nombre, int desplazamiento) {
		this.tipoBase = tipo;
		this.nombre = nombre;
		this.setDesplazamiento(desplazamiento);
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

	public void setDesplazamiento(int desplazamiento) {
		this.desplazamiento = desplazamiento;
	}

	public int getDesplazamiento() {
		return desplazamiento;
	}

	@Override
	public String toString() {
		return "Campo [nombre=" + nombre + "]";
	}
	
}