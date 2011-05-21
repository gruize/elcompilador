package es.ucm.plg.compilador.analizadorSintactico;

import java.util.ArrayList;

import es.ucm.plg.compilador.analizadorLexico.AnalizadorLexico;
import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.compilador.tablaSimbolos.Detalles.Clase;
import es.ucm.plg.compilador.tablaSimbolos.GestorTS;
import es.ucm.plg.compilador.tablaSimbolos.TS;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.interprete.InstruccionInterprete;

public class AnalizadorSintactico {

	private AnalizadorLexico lexico;
	private ArrayList<InstruccionInterprete> codigo;
	private int dir;
	private boolean error;
	private boolean finDecs;
	private Tipos tipos;
	private Acciones acciones;
	
private static TS ts;
	
	public static TS getInstancia(){
		if(ts == null)
			ts = new TS();
		return ts;
	}

	public AnalizadorSintactico(AnalizadorLexico lexico) {
		this.lexico = lexico;
	}

	public AnalizadorLexico getLexico() {
		return lexico;
	}

	public void setLexico(AnalizadorLexico aLexico) {
		this.lexico = aLexico;
	}

	public ArrayList<InstruccionInterprete> getCodigo() {
		return codigo;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public boolean isError() {
		return error;
	}
	
	public void setError(boolean error) {
		this.error = error;
	}
	
	public Tipos getTipos() {
		return tipos;
	}

	public Acciones getAcciones() {
		return acciones;
	}

	public void iniciaSintactico() {
		programa();
	}

	public void programa() {
		try {
			this.tipos = new Tipos(this);
			this.acciones = new Acciones(this);
			this.lexico.scanner();
			this.error = false;
			this.dir = 0;
			this.finDecs = false;
			declaraciones();
			this.codigo = new ArrayList<InstruccionInterprete>();
			this.acciones.acciones();
		} catch (Exception ex) {
			error = true;
			GestorErrores.agregaError(11, lexico.getFila(),
					lexico.getColumna(), "Error!!!");
		}
		if (error) {
			System.out.println("¡¡¡COMPILADO CON ERRORES!!!");
			System.out.println(GestorErrores.getErrores_propios().toString());
		}
	}

	public void declaraciones() throws Exception {
		declaracion();
		declaracionesRE();
	}

	public void declaracionesRE() throws Exception {
		if (!error) {
			declaracion();
			if (!finDecs)
				declaracionesRE();
		}
	}

	public void declaracion() throws SintacticoException {

		if (!error) {

			// Reconozco el tipo
			Tipo tipo = tipos.defTipo();
			if (tipo != null) {
				String lex = lexico.getLexema();

				// Reconozco el id
				if (reconoce(PalabrasReservadas.TOKEN_ID)) {

					// Reconozco el punto y coma
					if (reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA)) {

						// Si la variable no existe, la añado a la TS
						//FIXME Calcular el nivel ???
						if (!GestorTS.getInstancia().existeID(lex)) {
							dir = GestorTS.getInstancia().annadeID(lex, dir,
									tipo, Clase.var, 0);
						} else {
							error = true;
							GestorErrores.agregaError(11, lexico.getFila(),
									lexico.getColumna(), "Variable " + lex
											+ " duplicada");
						}
					} else {
						error = true;
						GestorErrores.agregaError(11, lexico.getFila(),
								lexico.getColumna(), "Falta un punto y coma");
					}
				} else {
					error = true;
					GestorErrores.agregaError(
							11,
							lexico.getFila(),
							lexico.getColumna(),
							"Se esperaba un tkid en lugar de"
									+ lexico.getToken_actual());
				}
			} else {
				// Si no reconozco una declaración, o ya he acabado las
				// declaraciones o es un error
				if (!finDecs)
					finDecs = true;
				else {
					error = true;
					GestorErrores.agregaError(11, lexico.getFila(),
							lexico.getColumna(), "Se esperaba un tipo");
				}
			}
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

	


}
