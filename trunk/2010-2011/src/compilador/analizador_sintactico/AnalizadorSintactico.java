package compilador.analizador_sintactico;

import interprete.InstruccionInterprete;

import java.util.Vector;

import compilador.analizador_lexico.AnalizadorLexico;

public class AnalizadorSintactico {

	private AnalizadorLexico a_lexico;
	private Vector<InstruccionInterprete> codigo;
	private int dir;
	private boolean error;
		
	public AnalizadorSintactico(AnalizadorLexico lexico){
		this.a_lexico = lexico;
	}

	public AnalizadorLexico getA_lexico() {
		return a_lexico;
	}

	public void setA_lexico(AnalizadorLexico a_lexico) {
		this.a_lexico = a_lexico;
	}

	public Vector<InstruccionInterprete> getCodigo() {
		return codigo;
	}

	public void setCodigo(Vector<InstruccionInterprete> codigo) {
		this.codigo = codigo;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public void iniciaSintactico()throws Exception{
		//TODO
		programa();
	}
	
	public void programa()throws Exception{
		this.error = false;
		this.dir = 0;
		declaraciones();
		this.codigo = this.codigo = new Vector<InstruccionInterprete>();
		acciones();
		//TODO: Añadir al codigo la instruccion STOP
	}
	
	public void declaraciones(){
		//TODO: Reconocer si es un token tkint o tkreal o no
			declaracion();
			declaracionesRE();
		//En caso de no ser ninguno de los 2 pasa a acciones
	}
	
	public void declaracion(){
		//TODO: Reconocer el token (tkint o tkreal)
		
	}
	
	public void declaracionesRE(){
		//TODO: Reconocer si es un token tkint o tkreal o no
		declaracion();
		declaracionesRE();
		//En caso de no ser ninguno de los 2 pasa a acciones
	}
	
	public void acciones(){
		
	}
}
