package es.ucm.plg.compilador.analizadorSintactico;

import java.util.ArrayList;
import java.util.List;

import es.ucm.plg.compilador.analizadorLexico.AnalizadorLexico;
import es.ucm.plg.compilador.tablaSimbolos.Detalles;
import es.ucm.plg.compilador.tablaSimbolos.Detalles.Clase;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo.Modo;
import es.ucm.plg.compilador.tablaSimbolos.GestorTS;
import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;
import es.ucm.plg.interprete.instrucciones.Apilar;
import es.ucm.plg.interprete.instrucciones.ApilarDir;
import es.ucm.plg.interprete.instrucciones.ApilarInd;
import es.ucm.plg.interprete.instrucciones.Copia;
import es.ucm.plg.interprete.instrucciones.Desapilar;
import es.ucm.plg.interprete.instrucciones.DesapilarDir;
import es.ucm.plg.interprete.instrucciones.DesapilarInd;
import es.ucm.plg.interprete.instrucciones.Mueve;
import es.ucm.plg.interprete.instrucciones.Restar;
import es.ucm.plg.interprete.instrucciones.Sumar;

public class AnalizadorSintactico {

	private AnalizadorLexico lexico;

	private ArrayList<InstruccionInterprete> codigo;
	private int dir;
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
			this.pend = new ArrayList<String>();
			this.codigo = new ArrayList<InstruccionInterprete>();
			this.declaraciones.declaraciones();
			if(pend.contains(lexico.getLexema()))
				pend.remove(lexico.getLexema());
			GestorTS.getInstancia().ts().salidaTS();
			if (pend.size() > 0) 
				throw new Exception("Existen tipos pendientes sin declarar");
			this.acciones.acciones();
		} catch (Exception ex) {
			error = true;
			System.out.println("Compilado con errores: ");
			System.out.println(ex.getMessage());
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

	public void setDir(int dir) {
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

	public void inicio(int numNiveles, int tamDatos) throws InterpreteExcepcion {
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, numNiveles + 2)));
		this.codigo.add(new DesapilarDir(new DatoPila(DatoPila.INT, 1)));
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, 1 + numNiveles
				+ tamDatos)));
		this.codigo.add(new DesapilarDir(new DatoPila(DatoPila.INT, 0)));
		this.etiqueta += 4;
	}

	public void apilarRet(int ret) throws InterpreteExcepcion {
		this.codigo.add(new ApilarDir(new DatoPila(DatoPila.INT, 0)));
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, 1)));
		this.codigo.add(new Sumar());
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, ret)));
		this.codigo.add(new DesapilarInd());
		this.etiqueta += 5;
	}

	public void prologo(int nivel, int tamLocales) throws InterpreteExcepcion {
		this.codigo.add(new ApilarDir(new DatoPila(DatoPila.INT, 0)));
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, 2)));
		this.codigo.add(new Sumar());
		this.codigo.add(new ApilarDir(new DatoPila(DatoPila.INT, 1 + nivel)));
		this.codigo.add(new DesapilarInd());
		this.codigo.add(new ApilarDir(new DatoPila(DatoPila.INT, 0)));
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, 3)));
		this.codigo.add(new Sumar());
		this.codigo
				.add(new DesapilarDir(new DatoPila(DatoPila.INT, 1 + nivel)));
		this.codigo.add(new ApilarDir(new DatoPila(DatoPila.INT, 0)));
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, tamLocales + 2)));
		this.codigo.add(new Sumar());
		this.codigo.add(new DesapilarDir(new DatoPila(DatoPila.INT, 0)));
		this.etiqueta += 13;
	}

	public void epilogo(int nivel) throws InterpreteExcepcion {
		this.codigo.add(new ApilarDir(new DatoPila(DatoPila.INT, 1 + nivel)));
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, 2)));
		this.codigo.add(new Restar());
		this.codigo.add(new ApilarInd());
		this.codigo.add(new ApilarDir(new DatoPila(DatoPila.INT, 1 + nivel)));
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, 3)));
		this.codigo.add(new Restar());
		this.codigo.add(new Sumar());
		this.codigo.add(new Copia());
		this.codigo.add(new DesapilarDir(new DatoPila(DatoPila.INT, 0)));
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, 2)));
		this.codigo.add(new Sumar());
		this.codigo
				.add(new DesapilarDir(new DatoPila(DatoPila.INT, 1 + nivel)));
		this.etiqueta += 13;
	}

	public void accesoVar(String id) throws InterpreteExcepcion {

		Detalles info = GestorTS.getInstancia().ts().getDetalles(id);
//		this.codigo.add(new ApilarDir(new DatoPila(DatoPila.INT, info
//				.getNivel() + 1)));
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, info.getDir())));
//		this.codigo.add(new Sumar());
		this.etiqueta += 1;

		if (info.getClase().equals(Clase.var)) {
			this.codigo.add(new ApilarInd());
			this.etiqueta += 1;
		}
	}

	
	public void inicioPaso() throws InterpreteExcepcion {
		this.codigo.add(new ApilarDir(new DatoPila(DatoPila.INT, 0)));
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, 3)));
		this.codigo.add(new Sumar());
		this.etiqueta += 3;
	}
	
	public void direccionParFormal(int dir) {
		this.codigo.add(new Apilar(new DatoPila(DatoPila.INT, dir)));
		this.codigo.add(new Sumar());
		this.etiqueta += 2;		
	}
	
	public void pasoParametro(Modo modoReal, Tipo pFormal) throws InterpreteExcepcion {
		
		if (modoReal == Modo.var && pFormal.getModo() == Modo.valor) {
			this.codigo.add(new Mueve(new DatoPila(DatoPila.INT, pFormal.getTamanyo())));
		}
		else {
			this.codigo.add(new DesapilarInd());
		}
		this.etiqueta += 1;		
		
	}
	
	public void finPaso() throws InterpreteExcepcion {
		this.codigo.add(new Desapilar());
		this.etiqueta += 1;
	}

}
