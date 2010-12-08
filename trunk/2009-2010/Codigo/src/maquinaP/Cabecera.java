package maquinaP;

public class Cabecera {

	private Integer inicio = 0;
	private Integer tamanyo = 0;
	
	public Cabecera(Integer inicio, Integer tamanyo) {
		this.inicio = inicio;
		this.tamanyo = tamanyo;
	}

	public Cabecera() {
	}

	public Integer getInicio() {
		return inicio;
	}

	public void setInicio(Integer inicio) {
		this.inicio = inicio;
	}

	public Integer getTamanyo() {
		return tamanyo;
	}

	public void setTamanyo(Integer tamanyo) {
		this.tamanyo = tamanyo;
	}
	
}
