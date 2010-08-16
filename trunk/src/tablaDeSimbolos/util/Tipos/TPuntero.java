package tablaDeSimbolos.util.Tipos;

public class TPuntero extends Tipo{

	private Tipo tipobase;

	public TPuntero(Tipo tipobase) {
		super(TipoDec.TipoPuntero, 1);
		this.tipobase = tipobase;
	}

	public Tipo getTipobase() {
		return tipobase;
	}

	public void setTipobase(Tipo tipobase) {
		this.tipobase = tipobase;
	}

	@Override
	public String toString() {
		return "TPuntero [tipobase=" + tipobase + ", tipo=" + tipo + "]";
	}
	
	public boolean equals(Tipo obj){
		boolean igual = false;
		if(super.equals(obj) && this.getTipobase().equals(obj.getTipobase()))
			igual = true;
		if(this.getTipobase() == null && super.equals(obj))
			igual = true;
		return igual;
	}
	
}
