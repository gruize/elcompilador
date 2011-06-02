package es.ucm.plg.compilador.tablaSimbolos.tipos;

public abstract class Tipo {
		
	int tamanyo;
	
	public enum Modo{
		var, valor
	}
	
	Modo modo;
	
	public int getTamanyo(){
		return 1;
	}
	
	public boolean equals(Tipo tipo){
		return false;
	}
	
	public Modo getModo(){
		return modo;
	}
	
	public void setModo(Modo modoAux){
		modo = modoAux;
	}
	
}
