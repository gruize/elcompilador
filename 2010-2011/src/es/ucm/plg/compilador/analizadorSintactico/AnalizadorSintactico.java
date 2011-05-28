package es.ucm.plg.compilador.analizadorSintactico;

import java.util.ArrayList;
import java.util.List;

import es.ucm.plg.compilador.analizadorLexico.AnalizadorLexico;
import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;

public class AnalizadorSintactico {

	private AnalizadorLexico lexico;
	
	private ArrayList<InstruccionInterprete> codigo;
	private int dir;
	private int nivel;
	private int etiqueta;
	private boolean error;
	private List<String> pend;
	
	private Tipos tipos;
	private Acciones acciones;
	private Expresiones expresiones;
	private Declaraciones declaraciones;

	public AnalizadorSintactico(AnalizadorLexico lexico) {
		this.lexico = lexico;
		this.tipos = new Tipos(this);
		this.acciones = new Acciones(this);
		this.expresiones = new Expresiones(this);
		this.declaraciones = new Declaraciones(this);
	}

	public void iniciaSintactico() {
		try {
			this.lexico.scanner();
			this.error = false;
			this.dir = 0;
			this.etiqueta = 0;
			declaraciones.declaraciones();
			this.codigo = new ArrayList<InstruccionInterprete>();
			this.acciones.acciones();
		} catch (Exception ex) {
			error = true;
			GestorErrores.agregaError(11, lexico.getFila(),
					lexico.getColumna(), "Error!!!" + ex.getMessage());
		}
		if (error) {
			System.out.println("¡¡¡COMPILADO CON ERRORES!!!");
			System.out.println(GestorErrores.getErrores_propios().toString());
		}
	}

	public boolean reconoce(String token_necesario) {

		boolean reconoce = false;

		if (!error) {
			if (token_necesario.equals(lexico.getToken_actual())) {
				lexico.scanner();
				reconoce = true;
			}
		}

		return reconoce;
	}

	public AnalizadorLexico getLexico() {
		return lexico;
	}

	public ArrayList<InstruccionInterprete> getCodigo() {
		return codigo;
	}

	public int getDir() {
		return dir;
	}
	
	public void setDir(int dir){
		this.dir = dir;
	}

	public boolean isError() {
		return error;
	}

	public Tipos getTipos() {
		return tipos;
	}

	public Acciones getAcciones() {
		return acciones;
	}

	public Expresiones getExpresiones() {
		return expresiones;
	}

	public Declaraciones getDeclaraciones() {
		return declaraciones;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getNivel() {
		return nivel;
	}

	public void setPend(List<String> pend) {
		this.pend = pend;
	}

	public List<String> getPend() {
		return pend;
	}

	public void setEtiqueta(int etiqueta) {
		this.etiqueta = etiqueta;
	}

	public int getEtiqueta() {
		return etiqueta;
	}

}
