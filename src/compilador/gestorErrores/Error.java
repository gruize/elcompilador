package compilador.gestorErrores;

public class Error {
	
	private int fila;
	private int columna;
	private String descripcion;
	private int id;
	
	public Error(int fila, int columna, String descripcion, int id) {
		this.fila = fila;
		this.columna = columna;
		this.descripcion = descripcion;
		this.id = id;
	}

	public Error(int fila, int columna, int id) {
		this.fila = fila;
		this.columna = columna;
		this.id = id;
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}

	public int getColumna() {
		return columna;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Error [fila=" + fila + ", columna=" + columna + ", descripcion="
				+ descripcion + ", id=" + id + "]";
	}
}
