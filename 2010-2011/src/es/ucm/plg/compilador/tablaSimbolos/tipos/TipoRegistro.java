package es.ucm.plg.compilador.tablaSimbolos.tipos;

import java.util.HashMap;
import java.util.Iterator;

public class TipoRegistro implements Tipo{
	
	private HashMap<String, Campo> campos;
		
	public TipoRegistro(HashMap<String, Campo> campos) {
		this.setCampos(campos);
	}
	
	public void setCampos(HashMap<String, Campo> campos) {
		this.campos = campos;
	}

	public HashMap<String, Campo> getCampos() {
		return campos;
	}

	public int getTamaño() {
		
		int tamaño = 0;
		Iterator<Campo> itCampos = campos.values().iterator();
		
		while (itCampos.hasNext()) {
			tamaño += itCampos.next().getTipoBase().getTamaño();
		}
		
		return tamaño;
	}
	
}
