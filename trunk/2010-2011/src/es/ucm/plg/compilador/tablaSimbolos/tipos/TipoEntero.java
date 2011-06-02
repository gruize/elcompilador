package es.ucm.plg.compilador.tablaSimbolos.tipos;

public class TipoEntero extends Tipo {
	
	@Override
	public int getTamanyo() {
		return 1;
	}

	@Override
	public boolean equals(Tipo tipo) {
		return tipo instanceof TipoEntero;
	}
}
