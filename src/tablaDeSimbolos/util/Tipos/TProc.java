package tablaDeSimbolos.util.Tipos;

import java.util.Vector;

import tablaDeSimbolos.util.Clase;

public class TProc extends Tipo{

	private Vector<TParam> parametros;
	private Clase clase;
	//TODO: Requiere un tipo pero no se porque.
	private Tipo tipo;
	
	public TProc(int tamanyo, Vector<TParam> parametros,
			Clase clase, Tipo tipop) {
		this.parametros = parametros;
		this.clase = clase;
		tipo = tipop;
	}
	
	public TProc(){
		this.parametros = new Vector<TParam>();
	}

	public Vector<TParam> getParametros() {
		return parametros;
	}

	public void setParametros(Vector<TParam> parametros) {
		this.parametros = parametros;
	}

	public Clase getClase() {
		return clase;
	}

	public void setClase(Clase clase) {
		this.clase = clase;
	}

	public Tipo getTipoProc() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "TProc [parametros=" + parametros + ", clase=" + clase
				+ ", tipo=" + tipo + "]";
	}
	
}
