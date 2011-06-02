package es.ucm.plg.compilador.tablaSimbolos.tipos;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class TipoRegistro extends Tipo{
	
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

	public int getTamanyo() {
		
		int tamanyo = 0;
		Iterator<Campo> itCampos = campos.values().iterator();
		
		while (itCampos.hasNext()) {
			tamanyo += itCampos.next().getTipoBase().getTamanyo();
		}
		
		return tamanyo;
	}

	@Override
	public boolean equals(Tipo tipo) {
		
		if (!(tipo instanceof TipoRegistro))
			return false;

		TipoRegistro tipoReg = (TipoRegistro) tipo;
		
		if (!(tipoReg.campos.size() == this.campos.size()))
				return false;
		
		Collection<Campo> campos = tipoReg.campos.values();
		Iterator<Campo> it = this.campos.values().iterator();
		
		while (it.hasNext()) {
			Campo campo = it.next();
			if (campos.contains(campo.getTipoBase()))
				campos.remove(campo.getTipoBase());		
		}
		
		return (campos.size() == 0);
	}
	
}
