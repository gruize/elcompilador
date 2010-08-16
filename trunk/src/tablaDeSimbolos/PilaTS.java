package tablaDeSimbolos;

import java.util.Stack;

import tablaDeSimbolos.util.Clase;
import tablaDeSimbolos.util.Detalles;
import tablaDeSimbolos.util.Tipos.Tipo;
import tablaDeSimbolos.util.Tipos.TipoDec;

public class PilaTS {

	private Stack<TS> pilats;

	public PilaTS() {
		pilats = new Stack<TS>();
		pilats.add(new TS());
	}

	public void añadeID(String id, Clase clase, Integer dir, Tipo tipo, int nivel){
		pilats.peek().añadeID(id, clase, dir, tipo, nivel);
	}
	
	public void creaTS(){
		pilats.push(new TS());
	}
	
	public void borraTS(){
		pilats.pop();
	}
	
	/**
	 * La búsqueda en la pila de tabla de simbolos debe hacerse desde el fondo de la pila hacia arriba.
	 */
	
	public boolean existeID(String id){
		Boolean encontrado = false;
		int i = 1;
		while(!encontrado && i <= pilats.size()){
			TS temp = pilats.elementAt(pilats.size() - i);
			if(temp.existeID(id))
				encontrado = true;
			i++;
		}
		return encontrado;
	}
	
	public Detalles getDetalles(String id){
		Detalles detalles = null;
		int i = 1;
		while(detalles == null && i <= pilats.size()){
			TS temp = pilats.elementAt(pilats.size() - i);
			if (temp.existeID(id))
				detalles = temp.getDetalles(id);
			i++;
		}
		return detalles;
	}
	
	public Clase getClase(String id){
		Clase clase = null;
		int i = 1;
		while(clase == null && i <= pilats.size()){
			TS temp = pilats.elementAt(pilats.size()-i);
			if(temp.existeID(id))
				clase = temp.getClase(id);
			i++;
		}
		return clase;
	}

	public Integer getDir(String id){
		Boolean encontrado = false;
		Integer dir = null;
		int i = 1;
		while(!encontrado && i <= pilats.size()){
			TS temp = pilats.elementAt(pilats.size() - i);
			if(temp.existeID(id)){
				encontrado = true;
				dir = temp.getDir(id);
			}
			i++;
		}
		return dir;
	}
	
	public Tipo getTipo(String id){
		if(id.equals("TipoBoolean"))
			return new Tipo(TipoDec.TipoBoolean,1);
		else 
			if(id.equals("TipoCharacter"))
				return new Tipo(TipoDec.TipoCharacter,1);
			else
				if(id.equals("TipoNatural"))
					return new Tipo(TipoDec.TipoNatural,1);
				else
					if(id.equals("TipoFloat"))
						return new Tipo(TipoDec.TipoFloat,1);
					else{
						boolean encontrado = false;
						int i = 1;
						Tipo type = null;
						while(!encontrado && i <= pilats.size()){
							TS temp = pilats.elementAt(pilats.size() - i);
							if(temp.existeID(id)){
								encontrado = true;
								type = temp.getTipo(id);
							}
							i++;
						}
						return type;
					}
	}
	
	public int getNivel(String id){
		Boolean encontrado = false;
		int nivel = 0;
		int i = 1;
		while(!encontrado && i <= pilats.size()){
			TS temp = pilats.elementAt(pilats.size() - i);
			if(temp.existeID(id)){
				encontrado = true;
				nivel = temp.getNivel(id);
			}
			i++;
		}
		return nivel;
	}

	/**
	 * Indica si existe en la Tabla de Simbolos actual
	 * @param id
	 * @return
	 */
	public boolean existeActualTS(String id){
		boolean existe = false;
		if(this.pilats.peek().existeID(id))
			existe = true;
		return existe;
	}
	
	/**
	 * Indica si un tipo es basico 
	 */
	public boolean esTipoBasico(Tipo obj){
		boolean basico = false;
		if(obj != null){
			if(obj.getTipo().equals(TipoDec.TipoBoolean) || obj.getTipo().equals(TipoDec.TipoCharacter) || 
				obj.getTipo().equals(TipoDec.TipoNatural) || obj.getTipo().equals(TipoDec.TipoFloat) || 
				obj.getId().equals(TipoDec.TipoBoolean.toString()) || 
				obj.getId().equals(TipoDec.TipoCharacter.toString()) || 
				obj.getId().equals(TipoDec.TipoNatural.toString()) ||
				obj.getId().equals(TipoDec.TipoFloat.toString()))
				basico = true;
		}
		return basico;
	}
	/**
	 * Indica si un tipo dado existe
	 */
	public boolean esTipo(Tipo obj){
		boolean esTipo = false;
		int i = 1;
		if(obj != null){
			while(i <= pilats.size() && !esTipo){
				TS temp = pilats.get(pilats.size() - i);
				if(temp.esTipo(obj))
					esTipo = true;
				i++;
			}
		}
		return esTipo;
	}
	
	public void salidaTSActual(){
		pilats.peek().salidaTS();
	}
}
