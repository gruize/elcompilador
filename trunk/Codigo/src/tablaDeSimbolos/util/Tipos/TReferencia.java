package tablaDeSimbolos.util.Tipos;

public class TReferencia extends Tipo{

	private String id;

	public TReferencia(int tamanyo, String id) {
		super(TipoDec.TipoReferencia, tamanyo);
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public String getIdRef(){
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setIdRef(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TReferencia [id=" + id + ", tipo=" + tipo + "]";
	}
	
	public boolean equals(Tipo obj){
		boolean igual = false;
		if(this.id.equals(obj.getId()) && super.equals(obj))
			igual = true;
		return igual;
	}
}
