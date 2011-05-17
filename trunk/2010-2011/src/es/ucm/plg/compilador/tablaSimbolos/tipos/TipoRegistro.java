package es.ucm.plg.compilador.tablaSimbolos.tipos;

import java.util.List;

public class TipoRegistro implements Tipo{
	
	private List<Campo> campos;
		
	public TipoRegistro(List<Campo> campos) {
		this.setCampos(campos);
	}
	
	public void setCampos(List<Campo> campos) {
		this.campos = campos;
	}

	public List<Campo> getCampos() {
		return campos;
	}

	public int getTamaño() {
		int tamaño = 0;
		
		for (Campo c : campos) {
			tamaño += c.getTipoBase().getTamaño();
		}
		
		return tamaño;
	}
	
}
