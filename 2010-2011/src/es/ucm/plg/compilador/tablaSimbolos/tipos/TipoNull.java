package es.ucm.plg.compilador.tablaSimbolos.tipos;

public class TipoNull extends Tipo{

	@Override
	public int getTamanyo() {
		return 1;
	}

	@Override
	public boolean equals(Tipo tipo) {
		return tipo instanceof TipoNull;
	}

	@Override
	public String toString() {
		return "TipoNull";
	}

}
