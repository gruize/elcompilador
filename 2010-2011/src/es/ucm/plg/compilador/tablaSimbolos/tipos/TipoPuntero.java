package es.ucm.plg.compilador.tablaSimbolos.tipos;

public class TipoPuntero implements Tipo{
	
	private Tipo tipoBase;
	
	public TipoPuntero(Tipo tipo) {
		tipoBase = tipo;
	}

	public Tipo getTipoBase() {
		return tipoBase;
	}
	
	@Override
	public int getTamanyo() {
		return 1;
	}

	@Override
	public boolean equals(Tipo tipo) {
		return (tipo instanceof TipoPuntero) && (((TipoPuntero) tipo).getTipoBase() == this.tipoBase);
	}

}