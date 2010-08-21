package analizadorSintactico;

import java.util.Hashtable;
import java.util.Vector;

import tablaDeSimbolos.util.Clase;
import tablaDeSimbolos.util.Detalles;
import tablaDeSimbolos.util.Tipos.TParam;
import util.InstruccionesMaquinaP;
import util.OperacionesMaquinaP;
import analizadorLexico.AnalizadorLexico;

public class AnalizadorSintactico {
	//Valores constantes
	private static final int longInicio = 4;
	private static final int longApilaRet = 5;
	private static final int longPrologo = 13;
	private static final int longEpilogo = 13;
	private static final int longInicioPaso = 3;
	private static final int longFinPaso = 1;
	private static final int longDirParFormal = 2;
	private static final int longPasoParametro = 1;
	//Fin de valores constantes
	private AnalizadorLexico alexico;
	private Vector<InstruccionesMaquinaP> codigo;
	private Vector<String> pendientes;
	private Vector<TParam> parametros;
	private Hashtable<String,Boolean> forward;
	//TODO: HashMap<String,LinkedList<Integer>>
	private Hashtable<String,Integer> procConForwardDone;
	
	private int nivel;
	private int etq;
	private int nDisplays;
	private int etqSaltos;

	public AnalizadorSintactico(AnalizadorLexico alexico) {
		this.alexico = alexico;
		this.codigo = new Vector<InstruccionesMaquinaP>();
		this.pendientes = new Vector<String>();
		this.parametros = new Vector<TParam>();
		this.procConForwardDone = new Hashtable<String, Integer>();
	}
	
	public void inicio(int numNiveles, int tamDatos){
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, numNiveles));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, 2));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_DIR, 1));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, numNiveles));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, 1));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, tamDatos));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_DIR, 0));
	}
	
	public void apilaRet(int ret){
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_DIR, 0));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, 1));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, ret));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_IND));
	}
	
	public void prologo(int nivel, int tamLocales){
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_DIR, 0));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, 2));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_DIR, 1 + nivel));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_IND));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_DIR, 0));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, 3));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_DIR, 1 + nivel));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_DIR, 0));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, tamLocales + 2));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_DIR, 0));
	}
	
	public void epilogo(int nivel){
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_DIR, 1 + nivel));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, 2));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.RESTA));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_IND));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_DIR, 1 + nivel));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, 3));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.RESTA));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.COPIA));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_DIR, 0));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, 2));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_IND));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_DIR, 1 + nivel));
	}
	
	public void accesoVar(Detalles infoID){
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_DIR, 1 - infoID.getNivel()));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, infoID.getDir()));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
		if(infoID.getClase() == Clase.pvar){
			codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_IND));
		}
	}
	
	public int longAccesoVar(Detalles infoID){
		int longAccesoVar;
		if(infoID.getClase() == Clase.pvar)
			longAccesoVar = 4;
		else
			longAccesoVar = 3;
		return longAccesoVar;
	}
	
	//TODO: Aun por definir si TParam tiene un atributo direccion o no
	public void dirParFormal(TParam pformal){
		//codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, pformal))
	}
	
	public void pasoParametro(String modo, TParam pformal){
		if(pformal.getModo().equals("valor") && modo.equals("variable"))
			codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.MUEVE,pformal.getTipobase().getTamanyo()));
		else
			codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_IND));
	}
	
	public void analiza(){
		alexico.scanner();
		
	}
	
}
