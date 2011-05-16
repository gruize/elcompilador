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
	public int getTamaño() {
		return 1;
	}

}