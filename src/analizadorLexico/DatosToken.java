package analizadorLexico;

public class DatosToken {
	
	private String token;
	private int fila;
	private int columna;
	private int indice;
	
	public DatosToken(String token, int fila, int columna, int indice) {
		this.token = token;
		this.fila = fila;
		this.columna = columna;
		this.indice = indice;
	}
	
	public DatosToken(String token, int fila, int columna) {
		this.token = token;
		this.fila = fila;
		this.columna = columna;
	}
	
	public DatosToken(String token, int indice) {
		this.token = token;
		this.indice = indice;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	@Override
	public String toString() {
		return "DatosToken [token=" + token + ", fila=" + fila + ", columna="
				+ columna + ", indice=" + indice + "]";
	}
	
}
