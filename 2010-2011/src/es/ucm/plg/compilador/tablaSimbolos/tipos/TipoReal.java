package es.ucm.plg.compilador.tablaSimbolos.tipos;

public class TipoReal implements Tipo {

	@Override
	public int getTamaño() {
		return 1;
	}
	
	@Override
	public boolean equals(Tipo tipo) {
		return tipo instanceof TipoReal;
	}

}
