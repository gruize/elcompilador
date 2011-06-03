package es.ucm.plg.compilador.tablaSimbolos.tipos;

public class TipoReal extends Tipo {

	@Override
	public int getTamanyo() {
		return 1;
	}
	
	@Override
	public boolean equals(Tipo tipo) {
		return tipo instanceof TipoReal;
	}

	@Override
	public String toString() {
		return "TipoReal";
	}

}
