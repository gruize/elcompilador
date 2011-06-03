package es.ucm.plg.compilador.tablaSimbolos.tipos;

import java.util.Vector;

import es.ucm.plg.compilador.analizadorSintactico.Params;

public class TipoFuncion extends Tipo {

	int nparams;
	
	Vector<Params> params;
	
	@Override
	public boolean equals(Tipo tipo) {
		return false;
	}

	@Override
	public int getTamanyo() {
		return 1;
	}

	public int getNparams() {
		return nparams;
	}

	public void setNparams(int nparams) {
		this.nparams = nparams;
	}

	public Vector<Params> getParams() {
		return params;
	}

	public void setParams(Vector<Params> params) {
		this.params = params;
	}
	
	@Override
	public String toString() {
		return "TipoFuncion";
	}	

}
