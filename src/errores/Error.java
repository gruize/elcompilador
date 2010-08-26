package errores;

public class Error {

	private int fila;
	private int col;
	private String descripcion;
	private int id;
	
	public Error(int fila, int col, String descripcion, int id) {
		this.fila = fila;
		this.col = col;
		this.descripcion = descripcion;
		this.id = id;
	}

	public Error(int fila, int col, int id) {
		this.fila = fila;
		this.col = col;
		this.id = id;
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
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
		return "Error [fila=" + fila + ", col=" + col + ", descripcion="
				+ descripcion + ", id=" + id + "]";
	}
	
	
}
