package es.ucm.plg.compilador.tablaSimbolos.tipos;

public class TipoArray implements Tipo {
	
	private Tipo tipoBase;
	private int num;
	
	public TipoArray(Tipo tipo, int num) {
		this.num = num;
		this.tipoBase = tipo;
	}

	public Tipo getTipoBase() {
		return tipoBase;
	}

	@Override
	public int getTamanyo() {
		return tipoBase.getTamanyo() * num;
	}
	
	@Override
	public boolean equals(Tipo tipo) {
		return tipoBase.equals(tipo) && (tipoBase.getTamanyo() == this.getTamanyo());
	}
	
}
