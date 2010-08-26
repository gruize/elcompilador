package analizadorSintactico;

import java.util.Hashtable;
import java.util.Vector;

import errores.GestorErrores;

import tablaDeSimbolos.GestorTS;
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
	//Otras constantes
	private static final int principal = 0;
	private static final int procedure = 1;
	//Fin otras constantes
	private AnalizadorLexico alexico;
	private Vector<InstruccionesMaquinaP> codigo;
	private Vector<String> pendientes;
	private Vector<TParam> parametros;
	private Hashtable<String,Boolean> forward;
	//TODO: HashMap<String,LinkedList<Integer>>
	private Hashtable<String,Integer> procConForwardDone;
	
	private int etq;
	private int nivel;
	private int anidamiento;
	private int tamano_datos;
	
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
	
	public void pasoParametro(String modo, TParam pformal){
		if(pformal.getModo().equals("valor") && modo.equals("variable"))
			codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.MUEVE,pformal.getTipobase().getTamanyo()));
		else
			codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_IND));
	}
	
	public boolean reconoce(String token_necesario) throws Exception{
		boolean reconoce = false;
		if(token_necesario.equals(alexico.getToken_actual())){
			alexico.scanner();
			reconoce = true;
		}else{
			GestorErrores.agregaError(11, alexico.getFila(), alexico.getColumna(), "Se esperaba un token " + token_necesario + " en lugar de " + alexico.getToken_actual());
			throw new Exception("Token inesperado");
		}
		return reconoce;
	}

	
	
	public void analiza() throws Exception{
		alexico.scanner();
		programa();
	}
	
	public void programa()throws Exception{
		this.etq = longInicio + 1;
		this.nivel = 0;
		int dirh = 0;
		this.anidamiento = 0;
		this.tamano_datos = 0;
		inicio(anidamiento,tamano_datos);
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_A,this.etq));
		if(alexico.getToken_actual().equals("tk&")){
			instrucciones();
		}else{
			declaraciones();
			correccionTSTiposPendientes();
			if(reconoce("tk&"))
				instrucciones();				
		}
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.STOP));
	}
	
	public void declaraciones()throws Exception{
		declaracion();
		
	}
	
	public void declaracion()throws Exception{
		if(alexico.getToken_actual().equals("tkprocedure")){
			alexico.scanner();
			if(alexico.getToken_actual().equals("tkid")){
				//Se añade el nuevo ambito
				GestorTS.getInstancia().creaTS();
				this.nivel = this.nivel + 1;
				
			}else{
				GestorErrores.agregaError(12, alexico.getFila(), alexico.getColumna(), "Se requiere identificador para el procedimiento");
				throw new Exception("Token inesperado");
			}
		}else{
			if(alexico.getToken_actual().equals("tktipo")){
				
			}else{
				if(alexico.getToken_actual().equals("tkid") || 
				alexico.getToken_actual().equals("tkboolean") || 
				alexico.getToken_actual().equals("tkcharacter") || 
				alexico.getToken_actual().equals("tknatural") ||
				alexico.getToken_actual().equals("tkinteger") ||
				alexico.getToken_actual().equals("tkfloat")){
					
				}else{
					GestorErrores.agregaError(12, alexico.getFila(), alexico.getColumna(), "Se requiere procedure, tipo, float, natural, integer, character, boolean o cualquier otro identificador de tipo");
					throw new Exception("Token inesperado");
				}
			}
		}
			
	}
	
	public void instrucciones(){
		
	}
	
	public void correccionTSTiposPendientes(){
		
	}
	
	//TODO: Aun por definir si TParam tiene un atributo direccion o no
	public void dirParFormal(TParam pformal){
		//codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA, pformal))
	}
	
	
	
}
