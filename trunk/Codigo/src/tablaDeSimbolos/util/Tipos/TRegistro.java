package tablaDeSimbolos.util.Tipos;

import java.util.Vector;

import tablaDeSimbolos.util.Campo;

public class TRegistro extends Tipo{

	Vector<Campo> campos;

	public TRegistro(Vector<Campo> campos,int tamanyo) {
		super(TipoDec.TipoRegistro, tamanyo);
		this.campos = campos;
	}

	public Vector<Campo> getCampos() {
		return campos;
	}

	public void setCampos(Vector<Campo> campos) {
		this.campos = campos;
	}

	@Override
	public String toString() {
		return "TRegistro [campos=" + campos + ", tipo=" + tipo + "]";
	}

	//TODO: Por confirmar
	public boolean equals(Tipo obj){
		boolean igual = false;
		if(super.equals(obj)){
			if (this.getCampos().size() == obj.getCampos().size()){
				int cantidad_igual = 0;
				for(int i = 0; i <= this.getCampos().size(); i++){
					if(this.getCampos().get(i).equals(obj.getCampos().get(i)))
						cantidad_igual++;
				}
				if(cantidad_igual == this.getCampos().size())
					igual = true;
			}
		}
		return igual;
	}
	
	//TODO: Por confirmar
	public boolean existeCampo(String id){
		boolean existe = false;
		int i = 0;
		while(!existe && i < this.getCampos().size()){
			if(this.getCampos().get(i).getId().equals(id))
				existe = true;
			i++;
		}
		return existe;
	}
	
	//TODO: Por confirmar
	public Campo getCampo(String id){
		Campo temp = null;
		int i = 0;
		while(temp == null && i < this.getCampos().size()){
			if(this.getCampos().get(i).getId().equals(id))
				temp = this.getCampos().get(i);
			i++;
		}
		return temp;
	}

}
