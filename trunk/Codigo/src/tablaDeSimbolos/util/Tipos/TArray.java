package tablaDeSimbolos.util.Tipos;

public class TArray extends Tipo{

	private Integer nelems;
	private Tipo tipobase;
	
	public TArray(Integer numero_elementos,Tipo tipo_base){
		super(TipoDec.TipoArray,numero_elementos * tipo_base.getTamanyo());
		nelems = numero_elementos;
		tipobase = tipo_base;
	}

	public Integer getNelems() {
		return nelems;
	}

	public void setNelems(Integer nelems) {
		this.nelems = nelems;
	}

	public Tipo getTipobase() {
		return tipobase;
	}

	public void setTipobase(Tipo tipobase) {
		this.tipobase = tipobase;
	}

	@Override
	public String toString() {
		return "TipoArray [nelems=" + nelems + ", tipobase=" + tipobase
				+ ", tipo=" + tipo + "]";
	}

	public boolean equals(Tipo obj) {
		boolean igual = false;
		if(super.equals(obj) && obj.getNelems().equals(this.getNelems()) && obj.getTipobase().equals(this.getTipobase()))
			igual = true;
		return igual;		
	}
	
	
	
}
