package analizadorSintactico;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import maquinaP.InstruccionesMaquinaP;
import maquinaP.OperacionesMaquinaP;

import errores.GestorErrores;

import tablaDeSimbolos.GestorTS;
import tablaDeSimbolos.util.Campo;
import tablaDeSimbolos.util.Clase;
import tablaDeSimbolos.util.Detalles;
import tablaDeSimbolos.util.Tipos.TArray;
import tablaDeSimbolos.util.Tipos.TError;
import tablaDeSimbolos.util.Tipos.TParam;
import tablaDeSimbolos.util.Tipos.TProc;
import tablaDeSimbolos.util.Tipos.TPuntero;
import tablaDeSimbolos.util.Tipos.TReferencia;
import tablaDeSimbolos.util.Tipos.TRegistro;
import tablaDeSimbolos.util.Tipos.Tipo;
import tablaDeSimbolos.util.Tipos.TipoDec;
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
	private Hashtable<String,LinkedList<Integer>> procConForwardDone;
	private int dir_n[];
	
	private int etq;
	private int nivel;
	private int anidamiento;
	private int tamano_datos;

	public AnalizadorSintactico(AnalizadorLexico alexico) {
		this.alexico = alexico;
		this.codigo = new Vector<InstruccionesMaquinaP>();		
	}
	public Vector<InstruccionesMaquinaP> getCodigo() {
		return codigo;
	}
	public void setCodigo(Vector<InstruccionesMaquinaP> codigo) {
		this.codigo = codigo;
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
	public void inicioPaso(){
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_DIR,0));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA,3));
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
	}
	public void finPaso(){
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA));
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
		this.nivel = 0;
		this.pendientes = new Vector<String>();
		this.forward = new Hashtable<String, Boolean>();
		int dirh = 0;
		this.etq = longInicio + 1;
		
		//Gonzalo
		this.anidamiento = 0;
		this.tamano_datos = 0;
		this.parametros = new Vector<TParam>();
		this.procConForwardDone = new Hashtable<String, LinkedList<Integer>>();
		this.dir_n = new int[50];
		for(int i = 0; i < 50; i++){
			this.dir_n[i] = 0;
		}
		//FinGonzalo
		
		if(!alexico.getToken_actual().equals("tk&")){
			declaraciones();
		}
        
		inicio(this.anidamiento,this.tamano_datos);
		this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_A, this.etq));
		if(!this.forward.isEmpty()){
			Enumeration<String> e = this.forward.keys();
			Object obj;
			while(e.hasMoreElements()){
				obj = e.nextElement();
				if(this.forward.get(obj)){
					GestorErrores.agregaError(15, alexico.getFila(), alexico.getColumna(), "Se requiere la declaración del cuerpo del procedimiento " + obj.toString());
				}
			}
			throw new Exception("Procedimientos no declarados (cuerpo)");
		} 
		reconoce("tk&");
		instrucciones();
		codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.STOP));
	}
	public void instrucciones() throws Exception{
		instruccion();
		instruccionesRE();
	}
	public void instruccion() throws Exception{
		//Invocacion o asignación
		if(alexico.getToken_actual().equals("tkid")){
			String lexid = alexico.getLexema();
			if(GestorTS.getInstancia().existeID(lexid)){
				if(GestorTS.getInstancia().getDetalles(lexid).getTipo().getTipobase().equals("TipoProc")){
					instruccionInvocacion();
				}else{
					instruccionAsignacion();
				}
			}
		}else{
			//Lectura
			if(alexico.getToken_actual().equals("tkin")){
				instruccionLectura();
			}else{
				//Escritura
				if(alexico.getToken_actual().equals("tkout")){
					instruccionEscritura();
				}else{
					//Alternativa
					if(alexico.getToken_actual().equals("tkif")){
						instruccionAlternativa();
					}else{
						//Mientras
						if(alexico.getToken_actual().equals("tkwhile")){
							instruccionMientras();
						}else{
							//Iteraciones
							if(alexico.getToken_actual().equals("tkfor")){
								instruccionIteraciones();
							}else{
								//ReservaMem
								if(alexico.getToken_actual().equals("tknew")){
									instruccionReservaMem();
								}else{
									//LiberaMem
									if(alexico.getToken_actual().equals("tkdispose")){
										instruccionLiberaMem();
									}else{
										/**Bloque definido entre llaves
										if(alexico.getToken_actual().equals("tk{")){
											instruccionBloque();
										}else{*/
											GestorErrores.agregaError(12, alexico.getFila(), alexico.getColumna(), "Se requiere identificador para el procedimiento");
											throw new Exception("Token inesperado");
										
									}
								}
							}
						}
					}
				}
			}
		}
	}
	/**
	private void instruccionBloque() throws Exception {
		reconoce("tk{");
		instrucciones();
		reconoce("tk}");
	}
	*/
	private void instruccionReservaMem() throws Exception {
		reconoce("tknew");
		String lex = alexico.getLexema();
		Tipo temp = mem();
		if(temp.getTipo().equals(TipoDec.TipoPuntero)){
			this.etq = this.etq + 2;
			this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_IND));
			if(temp.getTipobase().getTipo().equals(TipoDec.TipoReferencia)){
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.NEW,GestorTS.getInstancia().getDetalles(temp.getTipobase().getId()).getTipo().getTamanyo()));
			}else{
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.NEW,temp.getTipobase().getTamanyo()));
			}
			this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_IND));
		}else{
			GestorErrores.agregaError(16, alexico.getFila(), alexico.getColumna(), "Se esperaba que el id " + lex + " representase un puntero");
			throw new Exception("Se esperaba un puntero");
		}
	}
	private void instruccionLiberaMem() throws Exception {
		reconoce("tkdispose");
		String lex = alexico.getLexema();
		Tipo temp = mem();
		if(temp.getTipo().equals(TipoDec.TipoPuntero)){
			this.etq = this.etq + 2;
			this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_IND));
			if(temp.getTipobase().getTipo().equals(TipoDec.TipoReferencia)){
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DEL,GestorTS.getInstancia().getDetalles(temp.getTipobase().getId()).getTipo().getTamanyo()));
			}else{
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DEL,temp.getTipobase().getTamanyo()));
			}
		}else{
			GestorErrores.agregaError(16, alexico.getFila(), alexico.getColumna(), "Se esperaba que el id " + lex + " representase un puntero");
			throw new Exception("Se esperaba un puntero");
		}
	}
	private Tipo mem() throws Exception{
		Tipo tipovar = null;
		String lexid = alexico.getLexema();
		if(GestorTS.getInstancia().existeID(lexid) && 
			GestorTS.getInstancia().getDetalles(lexid).getClase().equals(Clase.var)){
			if(!GestorTS.getInstancia().refErronea(GestorTS.getInstancia().getTipo(lexid))){
				tipovar = GestorTS.getInstancia().getTipo(lexid);
				accesoVar(GestorTS.getInstancia().getDetalles(lexid));
				this.etq = longAccesoVar(GestorTS.getInstancia().getDetalles(lexid));
				tipovar = memRE(tipovar,lexid);
			}else{
				GestorErrores.agregaError(24, alexico.getFila(), alexico.getColumna());
				throw new Exception("Referencia erronea");
			}
		}else{
			GestorErrores.agregaError(17, alexico.getFila(), alexico.getColumna(), "La variable " + lexid + " no ha sido declarada");
			throw new Exception("Variable no declarada");
		}
		return tipovar;
	}
	private Tipo memRE(Tipo temp,String id) throws Exception {
		Tipo tmem = temp;
		//mem->
		if(alexico.getToken_actual().equals("tk->")){
			reconoce("tk->");
			Tipo tmem2 = memRE(tmem,id);
			if(tmem.getTipo().equals(TipoDec.TipoReferencia)){
				if(!GestorTS.getInstancia().refErronea(tmem)){
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_IND));
					this.etq = this.etq + 1;
					tmem = tmem2;					
				}else{
					GestorErrores.agregaError(24, alexico.getFila(), alexico.getColumna());
					throw new Exception("Referencia erronea");
				}
			}else{
				GestorErrores.agregaError(25, alexico.getFila(), alexico.getColumna());
				throw new Exception("Tipo incompatible. Se requeria un tipo referencia");
			}			
		}else{
			//mem[exp]
			if(alexico.getToken_actual().equals("tk[")){
				reconoce("tk[");
				boolean par = false;
				Object[] exp = Expresion(par);
				Tipo tipoexp = (Tipo)exp[0];
				reconoce("tk]");
				if(GestorTS.getInstancia().getTipo(id).getTipo().equals(TipoDec.TipoArray) &&
					tipoexp.getTipo().equals(TipoDec.TipoNatural)){
					if(!GestorTS.getInstancia().refErronea(tmem)){
						this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA,GestorTS.getInstancia().getTipo(id).getTamanyo()));
						this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.MULT));
						this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
						this.etq = this.etq + 3;
						Tipo corchete = memRE(temp,id);
						tmem = corchete;
					}else{
						GestorErrores.agregaError(24, alexico.getFila(), alexico.getColumna());
						throw new Exception("Referencia erronea");
					}
				}else{
					GestorErrores.agregaError(21, alexico.getFila(), alexico.getColumna());
					throw new Exception("Tipos incompatibles");
				}				
			}else{
				//mem.id
				if(alexico.getToken_actual().equals("tk.")){
					reconoce("tk.");
					String id2 = alexico.getLexema();
					if(tmem.getTipo().equals(TipoDec.TipoRegistro) && GestorTS.getInstancia().campoExiste(GestorTS.getInstancia().getTipo(id).getCampos(), id2) ){
						if(!GestorTS.getInstancia().refErronea(GestorTS.getInstancia().getTipo(id).getCampo(id2).getTipo())){
							this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA,GestorTS.getInstancia().getTipo(id).getCampo(id2).getDesp()));
							this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
							this.etq = this.etq + 2;
							Tipo punto = memRE(temp,id);
							tmem = punto;
						}else{
							GestorErrores.agregaError(24, alexico.getFila(), alexico.getColumna());
							throw new Exception("Referencia erronea");
						}
					}else{
						GestorErrores.agregaError(21, alexico.getFila(), alexico.getColumna());
						throw new Exception("Tipos incompatibles");
					}					
				}else{
					//id
					if(alexico.getToken_actual().equals("tkid")){
						String lex = alexico.getLexema();
						if(GestorTS.getInstancia().existeID(lex) && GestorTS.getInstancia().getClase(lex).equals(Clase.var)){
							if(!GestorTS.getInstancia().refErronea(GestorTS.getInstancia().getTipo(lex))){
								accesoVar(GestorTS.getInstancia().getDetalles(lex));
								this.etq = longAccesoVar(GestorTS.getInstancia().getDetalles(lex));
								Tipo lexemtipo = memRE(temp,id);
								tmem = lexemtipo;
							}else{
								GestorErrores.agregaError(24, alexico.getFila(), alexico.getColumna());
								throw new Exception("Referencia erronea");
							}
						}else{
							GestorErrores.agregaError(17, alexico.getFila(), alexico.getColumna(),"Variable" + lex + " no declarada");
							throw new Exception("Variable" + lex + " no declarada");
						}
						reconoce("tkid");
					}
				}
			}
		}
		return tmem;
	}

	private Object[] Expresion(boolean parh) throws Exception {
		//Object[0] tipo && Object[1] modo
		Object[] exp1 = ExpresionNivel1(parh);
		Tipo exptipo = (Tipo)exp1[0];
		String expmodo = (String)exp1[1];
		Object[] exp2 = ExpresionFX(exptipo,expmodo);
		return exp2;
	}
	private Object[] ExpresionFX(Tipo tip, String mod) throws Exception {
		Tipo exptipo = tip;
		String expmodo = mod;
		//Object[0] tipo && Object[1] modo		
		Object[] exp = new Object[]{exptipo,expmodo};
		OperacionesMaquinaP op = op0();
		if(op != null){
			boolean parh2 = false;
			exp[1] = "valor";
			Object[] exp1 = ExpresionNivel1(parh2);
			Tipo exptipo1 = (Tipo) exp[0];
			if(exptipo.getTipo().equals(TipoDec.TipoNatural) || exptipo.getTipo().equals(TipoDec.TipoInteger)
			|| exptipo.getTipo().equals(TipoDec.TipoFloat)){
				if(exptipo.getTipo().equals(TipoDec.TipoFloat) || exptipo1.getTipo().equals(TipoDec.TipoFloat)){
					exp[0] = new Tipo(TipoDec.TipoFloat,1);
				}else{
					if(exptipo.getTipo().equals(TipoDec.TipoInteger) || exptipo1.getTipo().equals(TipoDec.TipoInteger)){
						exp[0] = new Tipo(TipoDec.TipoInteger,1);
					}else{
						if(exptipo.getTipo().equals(TipoDec.TipoNatural) || exptipo1.getTipo().equals(TipoDec.TipoNatural)){
							exp[0] = new Tipo(TipoDec.TipoNatural,1);
						}else{
							exp[0] = new TError();
						}
					}
				}
			}else{
				if(exptipo.getTipo().equals(TipoDec.TipoCharacter)){
					if(exptipo1.getTipo().equals(TipoDec.TipoCharacter)){
						exp[0] = exptipo1;
					}else{
						exp[0] = new TError();
					}
				}else{
					if(exptipo.getTipo().equals(TipoDec.TipoBoolean)){
						if(exptipo1.getTipo().equals(TipoDec.TipoBoolean)){
							exp[0] = exptipo1;
						}else{
							exp[0] = new TError();
						}
					}else{
						exp[0] = new TError();
					}
				}
			}
			if(op.equals(OperacionesMaquinaP.MENOR)){
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.MENOR));
			}else{
				if(op.equals(OperacionesMaquinaP.MAYOR)){
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.MAYOR));
				}else{
					if(op.equals(OperacionesMaquinaP.MENOR_IGUAL)){
						this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.MENOR_IGUAL));
					}else{
						if(op.equals(OperacionesMaquinaP.MAYOR_IGUAL)){
							this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.MAYOR_IGUAL));
						}else{
							if(op.equals(OperacionesMaquinaP.IGUAL)){
								this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.IGUAL));
							}else{
								if(op.equals(OperacionesMaquinaP.DISTINTO)){
									this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DISTINTO));
								}
							}
						}
					}
				}
			}
			this.etq = this.etq + 1;
		}
		return exp;
	}
	private OperacionesMaquinaP op0() throws Exception {
		OperacionesMaquinaP op = null;
		if(alexico.getToken_actual().equals("tk<")){
			op = OperacionesMaquinaP.MENOR;
			reconoce("tk<");
		}else{
			if(alexico.getToken_actual().equals("tk>")){
				op = OperacionesMaquinaP.MAYOR;
				reconoce("tk>");
			}else{
				if(alexico.getToken_actual().equals("tk<=")){
					op = OperacionesMaquinaP.MENOR_IGUAL;
					reconoce("tk<=");
				}else{
					if(alexico.getToken_actual().equals("tk>=")){
						op = OperacionesMaquinaP.MAYOR_IGUAL;
						reconoce("tk>=");
					}else{
						if(alexico.getToken_actual().equals("tk=")){
							op = OperacionesMaquinaP.IGUAL;
							reconoce("tk=");
						}else{
							if(alexico.getToken_actual().equals("tk=/=")){
								op = OperacionesMaquinaP.DISTINTO;
								reconoce("tk=/=");
							}
						}
					}
				}
			}
		}
		return op;
	}
	private Object[] ExpresionNivel1(boolean parh) throws Exception {
		Tipo extipo = null;
		String modo = null;
		//Object[0] tipo && Object[1] modo
		Object[] expn2 = ExpresionNivel2(parh); 
		extipo = (Tipo) expn2[0];
		modo = (String) expn2[1];
		Object[] exp3 = ExpresionNivel1RE(extipo,modo);
		return exp3;
	}
	private Object[] ExpresionNivel1RE(Tipo tip, String mod) throws Exception {
		//Object[0] tipo && Object[1] modo
		Object[] exp1 = new Object[]{tip,mod};
		Tipo exptipo1 = tip;
		OperacionesMaquinaP op = op1();
		if(op != null){
			boolean parh2 = false;
			int etqAux = 0;
			if(op.equals(OperacionesMaquinaP.O_LOGICA)){
				this.codigo.add(new InstruccionesMaquinaP(null));
				etqAux = this.etq;
				this.etq = this.etq + 1;
			}
			Object[] exp2 = ExpresionNivel2(parh2);
			Tipo exptipo2 = (Tipo)exp2[0];
			if(op.equals(OperacionesMaquinaP.SUMA) || op.equals(OperacionesMaquinaP.RESTA)){
				if(exptipo1.getTipo().equals(TipoDec.TipoError) || exptipo2.getTipo().equals(TipoDec.TipoError)
				|| exptipo1.getTipo().equals(TipoDec.TipoBoolean) || exptipo2.getTipo().equals(TipoDec.TipoBoolean)
				|| exptipo1.getTipo().equals(TipoDec.TipoCharacter) || exptipo2.getTipo().equals(TipoDec.TipoCharacter)){
					exp1[0] = new TError();
				}else{
					if(exptipo1.getTipo().equals(TipoDec.TipoFloat) || exptipo2.getTipo().equals(TipoDec.TipoFloat)){
						exp1[0] = new Tipo(TipoDec.TipoFloat,1);
					}else{
						if(exptipo1.getTipo().equals(TipoDec.TipoInteger) || exptipo2.getTipo().equals(TipoDec.TipoInteger)){
							exp1[0] = new Tipo(TipoDec.TipoInteger,1);
						}else{
							if(exptipo1.getTipo().equals(TipoDec.TipoNatural) || exptipo2.getTipo().equals(TipoDec.TipoNatural)){
								exp1[0] = new Tipo(TipoDec.TipoNatural,1);
							}else{
								exp1[0] = new TError();
							}
						}
					}
				}
			}else{
				if(op.equals(OperacionesMaquinaP.O_LOGICA)){
					if(exptipo1.getTipo().equals(TipoDec.TipoBoolean) && exptipo2.getTipo().equals(TipoDec.TipoBoolean)){
						exp1[0] = exptipo1;
					}else{
						exp1[0] = new TError();
					}
				}
			}
			if(op.equals(OperacionesMaquinaP.O_LOGICA)){
				this.codigo.insertElementAt(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_V,this.etq), etqAux);
			}else{
				if(op.equals(OperacionesMaquinaP.SUMA)){
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
					this.etq = this.etq + 1;
				}else{
					if(op.equals(OperacionesMaquinaP.RESTA)){
						this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.RESTA));
						this.etq = this.etq + 1;
					}
				}
			}
			Object[] exp3 = ExpresionNivel1RE((Tipo)exp1[0], (String)exp1[1]);
			exp1 = exp3;
		}
		return exp1;
	}
	
	private OperacionesMaquinaP op1() throws Exception {
		OperacionesMaquinaP op = null;
		if(alexico.getToken_actual().equals("tk+")){
			op = OperacionesMaquinaP.SUMA;
			reconoce("tk+");
		}else{
			if(alexico.getToken_actual().equals("tk-")){
				op = OperacionesMaquinaP.RESTA;
				reconoce("tk-");
			}else{
				if(alexico.getToken_actual().equals("tkor")){
					op = OperacionesMaquinaP.O_LOGICA;
					reconoce("tkor");
				}
			}
		}
		return op;
	}
	
	private Object[] ExpresionNivel2(boolean parh) throws Exception {
		Tipo extipo = null;
		String modo = null;
		//Object[0] tipo && Object[1] modo
		Object[] expn3 = ExpresionNivel3(parh);
		extipo = (Tipo) expn3[0];
		modo = (String) expn3[1];
		Object[] expn2fx = ExpresionNivel2RE(extipo,modo);
		return expn2fx;
	}
	private Object[] ExpresionNivel2RE(Tipo tip, String mod) throws Exception {
		Tipo extipo = tip;
		String modo = mod;
		//Object[0] tipo && Object[1] modo
		Object[] expn2 = new Object[]{extipo,modo};
		OperacionesMaquinaP opmp = op2();
		if(opmp != null){
			boolean parh2 = false;
			int etqAux = 0;
			if(opmp.equals(OperacionesMaquinaP.Y_LOGICA)){
				this.codigo.add(new InstruccionesMaquinaP(null));
				etqAux = this.etq;
				this.etq = this.etq + 1;
			}
			Object[] exp3 = ExpresionNivel3(parh2);
			Tipo extipo2 = (Tipo) exp3[0];
			if(opmp.equals(OperacionesMaquinaP.MULT) || opmp.equals(OperacionesMaquinaP.DIVISION)){
				if(extipo.getTipo().equals(TipoDec.TipoError) || extipo2.getTipo().equals(TipoDec.TipoError) ||
				extipo.getTipo().equals(TipoDec.TipoBoolean) || extipo2.getTipo().equals(TipoDec.TipoBoolean)
				|| extipo.getTipo().equals(TipoDec.TipoCharacter) || extipo2.getTipo().equals(TipoDec.TipoCharacter)){
					expn2[0] = new TError();
				}else{
					if(extipo.getTipo().equals(TipoDec.TipoFloat) || extipo2.getTipo().equals(TipoDec.TipoFloat)){
						expn2[0] = new Tipo(TipoDec.TipoFloat,1);
					}else{
						if(extipo.getTipo().equals(TipoDec.TipoInteger) || 
						extipo2.getTipo().equals(TipoDec.TipoInteger)){
							expn2[0] = new Tipo(TipoDec.TipoInteger,1);
						}else{
							if(extipo.getTipo().equals(TipoDec.TipoNatural) 
							|| extipo2.getTipo().equals(TipoDec.TipoNatural)){
								expn2[0] = new Tipo(TipoDec.TipoNatural,1);
							}else{
								expn2[0] = new TError();
							}
						}
					}
				}
			}else{
				if(opmp.equals(OperacionesMaquinaP.MODULO)){
					if(extipo2.getTipo().equals(TipoDec.TipoNatural) && (
					extipo.getTipo().equals(TipoDec.TipoNatural) || extipo.getTipo().equals(TipoDec.TipoInteger))){
						expn2[0] = extipo;
					}else{
						expn2[0] = new TError();
					}
				}else{
					if(opmp.equals(OperacionesMaquinaP.Y_LOGICA)){
						if(extipo2.getTipo().equals(TipoDec.TipoBoolean) && 
						extipo.getTipo().equals(TipoDec.TipoBoolean)){
							expn2[0] = extipo;
						}else{
							expn2[0] = new TError();
						}
					}
				}
			}
			if(opmp.equals(OperacionesMaquinaP.Y_LOGICA)){
				this.codigo.insertElementAt(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_F,this.etq + 1), etqAux);
			}else{
				if(opmp.equals(OperacionesMaquinaP.MULT)){
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.MULT));
					this.etq = this.etq + 1;
				}else{
					if(opmp.equals(OperacionesMaquinaP.DIVISION)){
						this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DIVISION));
						this.etq = this.etq + 1;
					}else{
						if(opmp.equals(OperacionesMaquinaP.MODULO)){
							this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.MODULO));
							this.etq = this.etq + 1;
						}
					}
				}
			}
			Object[] exp4 = ExpresionNivel2RE((Tipo)expn2[0], (String)expn2[1]);
			expn2 = exp4;
		}
		return expn2;
	}
	private OperacionesMaquinaP op2() throws Exception {
		OperacionesMaquinaP operador = null;
		if(alexico.getToken_actual().equals("tk*")){
			operador = OperacionesMaquinaP.MULT;
			reconoce("tk*");
		}else{
			if(alexico.getToken_actual().equals("tk/")){
				operador = OperacionesMaquinaP.DIVISION;
				reconoce("tk/");
			}else{
				if(alexico.getToken_actual().equals("tk%")){
					operador = OperacionesMaquinaP.MODULO;
					reconoce("tk%");
				}else{
					if(alexico.getToken_actual().equals("tkand")){
						operador = OperacionesMaquinaP.Y_LOGICA;
						reconoce("tkand");
					}
				}
			}
		}
		return operador;
	}
	private Object[] ExpresionNivel3(boolean parh) throws Exception {
		Tipo extipo = null;
		String modo = null;
		//Object[0] tipo && Object[1] modo
		Object[] expn4 = ExpresionNivel4(parh);
		extipo = (Tipo) expn4[0];
		modo = (String) expn4[1];
		Object[] expn3fx = ExpresionNivel3FX(extipo,modo);
		return expn3fx;
	}
	private Object[] ExpresionNivel3FX(Tipo tip3, String modo3) throws Exception {
		Tipo extipo = tip3;
		String modo = modo3;
		//Object[0] tipo && Object[1] modo
		Object[] expn3fx = new Object[]{extipo,modo};
		OperacionesMaquinaP temopmp = op3();
		if(temopmp != null){
			expn3fx[1] = "valor";
			boolean parh2 = false;
			Object[] expn3fx2 = ExpresionNivel3(parh2);
			Tipo exfx3 = (Tipo)expn3fx2[0];
			if(extipo.equals(TipoDec.TipoError) || !extipo.equals(TipoDec.TipoNatural) ||
					exfx3.equals(TipoDec.TipoError) || !exfx3.equals(TipoDec.TipoNatural)){
				expn3fx[0] = new TError();
			}else{
				expn3fx[0] = new Tipo(TipoDec.TipoNatural,1);
			}
			if(temopmp.equals(OperacionesMaquinaP.DESP_IZQ)){
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESP_IZQ));
			}else{
				if(temopmp.equals(OperacionesMaquinaP.DESP_DER)){
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESP_DER));
				}else{
					GestorErrores.agregaError(18, alexico.getFila(), alexico.getColumna(), "Operador inadecuado de expresiones de nivel 4");
					throw new Exception("Operador inadecuado");
				}
			}
			this.etq = this.etq + 1;
		}
		return expn3fx;
	}
	private OperacionesMaquinaP op3() throws Exception {
		OperacionesMaquinaP optemp = null;
		if(alexico.getToken_actual().equals("tk<<")){
			optemp = OperacionesMaquinaP.DESP_IZQ;
			reconoce("tk<<");
		}else{
			if(alexico.getToken_actual().equals("tk>>")){
				optemp = OperacionesMaquinaP.DESP_DER;
				reconoce("tk>>");
			}
		}
		return optemp;
	}
	private Object[] ExpresionNivel4(boolean parh) throws Exception {
		Tipo extipo = null;
		String modo = null;
		//Object[0] tipo && Object[1] modo
		Object[] expn4 = null;
		if(alexico.getToken_actual().equals("tknot") ||
			//Signo menos
			alexico.getToken_actual().equals("tk-") ||
			alexico.getToken_actual().equals("tkcastfloat") ||
			alexico.getToken_actual().equals("tkcastint") ||
			alexico.getToken_actual().equals("tkcastnat") ||
			alexico.getToken_actual().equals("tkcastchar")){
				expn4 = ExpresionNivel4ConOp(parh);
		}else{
			//Valor absoluto
			if(alexico.getToken_actual().equals("tk|")){
				expn4 = ExpresionNivel4Abs(parh);
			}else{
				if(alexico.getToken_actual().equals("tknat") ||
						alexico.getToken_actual().equals("tkreal") ||
						alexico.getToken_actual().equals("tktrue") ||
						alexico.getToken_actual().equals("tkfalse") ||
						alexico.getToken_actual().equals("tkchar") ||
						alexico.getToken_actual().equals("tknull")){
					expn4 = ExpresionNivel4Literal(parh);
				}else{
					if(alexico.getToken_actual().equals("tk(")){
						expn4 = ExpresionNivel4Parentesis(parh);
					}else{
						if(alexico.getToken_actual().equals("tkid")){
							expn4 = ExpresionNivel4Mem(parh);
						}else{
							expn4 = new Object[]{null,""};
						}
					}
				}
			}
		}
		return expn4;
	}
	private Object[] ExpresionNivel4Mem(boolean parh) throws Exception {
		String modo = "valor";
		Tipo tempmem = mem();
		if(GestorTS.getInstancia().compatibles(tempmem, new Tipo(TipoDec.TipoNatural,1)) &&
			GestorTS.getInstancia().compatibles(tempmem, new Tipo(TipoDec.TipoInteger,1)) &&
			GestorTS.getInstancia().compatibles(tempmem, new Tipo(TipoDec.TipoFloat,1)) &&
			GestorTS.getInstancia().compatibles(tempmem, new Tipo(TipoDec.TipoBoolean,1)) &&
			!parh){
			this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_IND));
			this.etq = this.etq + 1;
		}else{
			modo = "variable";
		}
		//Object[0] tipo && Object[1] modo
		return new Object[]{tempmem, modo};
	}
	private Object[] ExpresionNivel4Parentesis(boolean parh) throws Exception {
		reconoce("tk(");
		//Object[0] tipo && Object[1] modo
		Object[] exp = Expresion(parh);
		reconoce("tk)");
		return exp;
	}
	private Object[] ExpresionNivel4Literal(boolean parh) throws Exception {
		Tipo exptipo = null;
		String modo = "valor";
		String lexema = alexico.getLexema();
		exptipo = Literal();
		//Object[0] tipo && Object[1] modo
		return new Object[]{exptipo,modo};
	}
	private Tipo Literal() throws Exception {
		Tipo temp = null;
		if(alexico.getToken_actual().equals("tknat")){
			this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA,alexico.getLexema()));
			reconoce("tknat");
			temp = new Tipo(TipoDec.TipoNatural,1);
		}else{
			if(alexico.getToken_actual().equals("tkreal")){
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA,alexico.getLexema()));
				reconoce("tkreal");
				temp = new Tipo(TipoDec.TipoFloat,1);
			}else{
				if(alexico.getToken_actual().equals("tktrue")){
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA,alexico.getLexema()));
					reconoce("tktrue");
					temp = new Tipo(TipoDec.TipoBoolean,1);
				}else{
					if(alexico.getToken_actual().equals("tkfalse")){
						this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA,alexico.getLexema()));
						reconoce("tkfalse");
						temp = new Tipo(TipoDec.TipoBoolean,1);
					}else{
						if(alexico.getToken_actual().equals("tkchar")){
							this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA,alexico.getLexema()));
							reconoce("tkchar");
							temp = new Tipo(TipoDec.TipoCharacter,1);
						}else{
							if(alexico.getToken_actual().equals("tknull")){
								this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA,alexico.getLexema()));
								reconoce("tknull");
								temp = new Tipo(TipoDec.TipoNull,1);
							}else{
								GestorErrores.agregaError(12, alexico.getFila(), alexico.getColumna(), "Token inesperado. Operador de expresiones de nivel 4");
								throw new Exception("Token inesperado");
							}
						}
					}
				}
			}
		}
		this.etq = this.etq + 1;
		return temp;
	}
	private Object[] ExpresionNivel4Abs(boolean parh) throws Exception {
		reconoce("tk|");
		Tipo exptipo = null;
		String modo = "valor";
		//Object[0] tipo && Object[1] modo
		Object[] exp = Expresion(parh);
		reconoce("tk|");
		if(((Tipo)exp[0]).getTipo().equals(TipoDec.TipoFloat)){
			exptipo = new Tipo(TipoDec.TipoFloat, 1);
		}else{
			if(((Tipo)exp[0]).getTipo().equals(TipoDec.TipoInteger) 
				|| ((Tipo)exp[0]).getTipo().equals(TipoDec.TipoNatural)){
				exptipo = new Tipo(TipoDec.TipoNatural,1);
			}else{
				exptipo = new TError();
			}
		}
		this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.ABS));
		this.etq = this.etq + 1;
		return new Object[]{exptipo,modo};
	}
	private Object[] ExpresionNivel4ConOp(boolean parh) throws Exception {
		OperacionesMaquinaP op = op4();
		Tipo exptipo = null;
		String modo = "valor";
		//Object[0] tipo && Object[1] modo
		Object[] expn4 = ExpresionNivel4(parh);	
		if(((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoError)){
			exptipo = new TError();
		}else{
			if(op.equals(OperacionesMaquinaP.NEG_LOGICA)){
				if(((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoBoolean)){
					exptipo = new Tipo(TipoDec.TipoBoolean, 1);
				}else{
					exptipo = new TError();
				}
			}else{
				if(op.equals(OperacionesMaquinaP.SIGNO_MENOS)){
					if(((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoFloat)){
						exptipo = new Tipo(TipoDec.TipoFloat, 1);
					}else{
						if(((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoInteger) || 
							((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoNatural)){
							exptipo = new Tipo(TipoDec.TipoInteger, 1);
						}else{
							exptipo = new TError();
						}
					}
				}else{
					if(op.equals(OperacionesMaquinaP.CAST_FLOAT)){
						if(((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoFloat) ||
							((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoInteger) ||
							((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoNatural) ||
							((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoCharacter)){
							exptipo = new Tipo(TipoDec.TipoFloat, 1);
						}else{
							exptipo = new TError();
						}
					}else{
						if(op.equals(OperacionesMaquinaP.CAST_INT)){
							if(((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoFloat) ||
								((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoInteger) ||
								((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoNatural) ||
								((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoCharacter)){
								exptipo = new Tipo(TipoDec.TipoInteger, 1);
							}else{
								exptipo = new TError();
							}
						}else{
							if(op.equals(OperacionesMaquinaP.CAST_NAT)){
								if(((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoNatural) ||
									((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoCharacter)){
									exptipo = new Tipo(TipoDec.TipoNatural, 1);
								}else{
									exptipo = new TError();
								}
							}else{
								if(op.equals(OperacionesMaquinaP.CAST_CHAR)){
									if(((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoNatural) ||
										((Tipo)expn4[0]).getTipobase().equals(TipoDec.TipoCharacter)){
										exptipo = new Tipo(TipoDec.TipoCharacter, 1);
									}else{
										exptipo = new TError();
									}
								}else{
									GestorErrores.agregaError(18, alexico.getFila(), alexico.getColumna(), "Operador inadecuado de expresiones de nivel 4");
									throw new Exception("Operador inadecuado");
								}
							}
						}
					}
				}
			}
			if(op.equals(OperacionesMaquinaP.NEG_LOGICA)){
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.NEG_LOGICA));
			}else{
				if(op.equals(OperacionesMaquinaP.SIGNO_MENOS)){
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SIGNO_MENOS));
				}else{
					if(op.equals(OperacionesMaquinaP.CAST_FLOAT)){
						this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.CAST_FLOAT));
					}else{
						if(op.equals(OperacionesMaquinaP.CAST_INT)){
							this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.CAST_INT));
						}else{
							if(op.equals(OperacionesMaquinaP.CAST_NAT)){
								this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.CAST_NAT));
							}else{
								if(op.equals(OperacionesMaquinaP.CAST_CHAR)){
									this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.CAST_CHAR));
								}else{
									GestorErrores.agregaError(18, alexico.getFila(), alexico.getColumna(), "Operador inadecuado de expresiones de nivel 4");
									throw new Exception("Operador inadecuado");
								}
							}
						}
					}
				}
			}
			this.etq = this.etq + 1;
		}
		return new Object[]{exptipo,modo};
	}
	private OperacionesMaquinaP op4() throws Exception {
		OperacionesMaquinaP optemp = null;
		if(alexico.getToken_actual().equals("tknot")){
			optemp = OperacionesMaquinaP.NEG_LOGICA;
			reconoce("tknot");
		}else{
			if(alexico.getToken_actual().equals("tk-")){
				optemp = OperacionesMaquinaP.SIGNO_MENOS;
				reconoce("tk-");
			}else{
				if(alexico.getToken_actual().equals("tkcastfloat")){
					optemp = OperacionesMaquinaP.CAST_FLOAT;
					reconoce("tkcastfloat");
				}else{
					if(alexico.getToken_actual().equals("tkcastint")){
						optemp = OperacionesMaquinaP.CAST_INT;
						reconoce("tkcastint");
					}else{
						if(alexico.getToken_actual().equals("tkcastnat")){
							optemp = OperacionesMaquinaP.CAST_NAT;
							reconoce("tkcastnat");
						}else{
							if(alexico.getToken_actual().equals("tkcastchar")){
								optemp = OperacionesMaquinaP.CAST_CHAR;
								reconoce("tkcastchar");
							}
						}
					}
				}
			}
		}
		return optemp;
	}
	private void instruccionIteraciones() throws Exception {
		reconoce("tkfor");
		int etqinicio = this.etq;
		int etqAux = 0;
		String lexvar = alexico.getLexema();
		Tipo tipovar = mem();
		reconoce("tk=");
		boolean par = false;
		Object[] exp1 = Expresion(par);
		Tipo tipoexp1 = (Tipo) exp1[0];
		reconoce("tkto");
		if(tipovar.getTipo().equals(TipoDec.TipoNatural) || tipovar.getTipo().equals(TipoDec.TipoInteger)){
			if(tipoexp1.getTipo().equals(TipoDec.TipoNatural) || tipoexp1.getTipo().equals(TipoDec.TipoInteger)){
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_DIR,GestorTS.getInstancia().getDir(lexvar)));
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_DIR,GestorTS.getInstancia().getDir(lexvar)));
				this.etq = this.etq + 2;
				Object[] exp2 = Expresion(par);
				Tipo tipoexp2 = (Tipo)exp2[0];
				if(tipoexp2.getTipo().equals(TipoDec.TipoNatural) || tipoexp2.getTipo().equals(TipoDec.TipoInteger)){
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.MENOR_IGUAL));
					this.codigo.add(new InstruccionesMaquinaP(null));
					this.etq = this.etq + 2;
					etqAux = this.etq;
					reconoce("tkdo");
					bloque();
					this.codigo.insertElementAt(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_F,this.etq), etqAux);
					this.etq = this.etq + 5;
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA,GestorTS.getInstancia().getDir(lexvar)));
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA,1));
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.SUMA));
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_DIR,GestorTS.getInstancia().getDir(lexvar)));
					this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_A,etqinicio));					
				}else{
					GestorErrores.agregaError(22, alexico.getFila(), alexico.getColumna(), "La expresion a asignar a la variable debe ser de tipo entera o natural");
					throw new Exception("La expresion a asignar a la variable debe ser de tipo entera o natural");
				}			
			}else{
				GestorErrores.agregaError(22, alexico.getFila(), alexico.getColumna(), "La expresion a asignar a la variable debe ser de tipo entera o natural");
				throw new Exception("La expresion a asignar a la variable debe ser de tipo entera o natural");
			}
		}else{
			GestorErrores.agregaError(22, alexico.getFila(), alexico.getColumna(), "La variable debe ser de tipo entera o natural");
			throw new Exception("La variable debe ser de tipo entera o natural");
		}
	}
	private void bloque() throws Exception {
		if(alexico.getToken_actual().endsWith("tk{")){
			reconoce("tk{");
			this.nivel = this.nivel + 1;
			instrucciones();
			this.nivel = this.nivel - 1;
			reconoce("tk}");
		}else{
			this.nivel = this.nivel + 1;
			prologo(this.nivel, this.dir_n[this.nivel]);
			this.etq = this.etq + longPrologo;
			instruccion();
			epilogo(this.nivel);
			this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_IND));
			this.etq = this.etq + longEpilogo + 1;
		}
	}
	private void instruccionMientras() throws Exception {
		reconoce("tkwhile");
		boolean par = false;
		int etqInicio = this.etq;
		int etqAux = 0;
		Object[] exp1 = Expresion(par);
		Tipo exptipo = (Tipo)exp1[0];
		reconoce("tkdo");
		if(exptipo.getTipo().equals(TipoDec.TipoBoolean)){
			this.codigo.add(new InstruccionesMaquinaP(null));
			this.etq = this.etq + 1;
			etqAux = this.etq;
			bloque();
			this.codigo.insertElementAt(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_F,this.etq + 1), etqAux);
			this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_A,etqInicio));
		}else{
			GestorErrores.agregaError(23, alexico.getFila(), alexico.getColumna(), "La expresion debe ser de tipo booleana");
			throw new Exception("La expresion debe ser de tipo booleana");
		}
	}
	private void instruccionAlternativa() throws Exception {
		int etqAux = 0;
		reconoce("tkif");
		boolean par = false;
		Object[] exp = Expresion(par);
		Tipo tipoexp = (Tipo)exp[0];
		reconoce("tkthen");
		if(tipoexp.getTipo().equals(TipoDec.TipoBoolean)){
			this.codigo.add(new InstruccionesMaquinaP(null));
			this.etq = this.etq + 1;
			etqAux = this.etq;
			bloque();
			this.codigo.insertElementAt(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_F,this.etq + 1), etqAux);
			this.codigo.add(new InstruccionesMaquinaP(null));
			this.etq = this.etq + 1;
			etqAux = this.etq;
			altelse();
			this.codigo.insertElementAt(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_A,this.etq + 1), etqAux);	
		}else{
			GestorErrores.agregaError(23, alexico.getFila(), alexico.getColumna(), "La expresion debe ser de tipo booleana");
			throw new Exception("La expresion debe ser de tipo booleana");
		}	
	}
	private void altelse() throws Exception {
		if(alexico.getToken_actual().equals("tkelse")){
			reconoce("tkelse");
			bloque();			
		}
	}
	private void instruccionEscritura() throws Exception {
		reconoce("tkout");
		reconoce("tk(");
		boolean par = false;
		Object[] exp1 = Expresion(par);
		this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.ESCRIBIR));
		this.etq = this.etq + 1;
		reconoce("tk)");		
	}
	private void instruccionLectura() throws Exception {
		reconoce("tkin");
		reconoce("tk(");
		String lexid = alexico.getLexema();
		Tipo tipoid = mem();
		if(GestorTS.getInstancia().existeID(lexid)){
			this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.LEER,GestorTS.getInstancia().getDir(lexid)));
			this.etq = this.etq + 1;
			reconoce("tk)");
		}else{
			GestorErrores.agregaError(17, alexico.getFila(), alexico.getColumna(), "La variable " + lexid + " no ha sido declarada");
			throw new Exception("Variable no declarada");
		}
	}
	private void instruccionAsignacion() throws Exception {
		String lexid = alexico.getLexema();
		Tipo tipoid = mem();
		reconoce("tk:=");
		boolean par = false;
		Object[] exp = Expresion(par);
		Tipo tipoexp = (Tipo)exp[0];
		if(GestorTS.getInstancia().compatibles(tipoid, tipoexp)){
			if(GestorTS.getInstancia().compatibles(tipoid, new Tipo(TipoDec.TipoNatural,1)) ||
			GestorTS.getInstancia().compatibles(tipoid, new Tipo(TipoDec.TipoInteger,1)) ||
			GestorTS.getInstancia().compatibles(tipoid, new Tipo(TipoDec.TipoFloat,1)) ||
			GestorTS.getInstancia().compatibles(tipoid, new Tipo(TipoDec.TipoBoolean,1)) ||
			GestorTS.getInstancia().compatibles(tipoid, new Tipo(TipoDec.TipoCharacter,1))){
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.DESAPILA_IND));
			}else{
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.MUEVE,tipoexp.getTamanyo()));
			}
			this.etq = this.etq + 1;
		}else{
			GestorErrores.agregaError(21, alexico.getFila(), alexico.getColumna(), "El tipo de la variable " + lexid + " es incompatible con el tipo de la expresion que se le desea asignar");
			throw new Exception("Tipos incompatibles en asignacion");
		}		
	}

	private void instruccionInvocacion() throws Exception {
		String lexproc = alexico.getLexema();
		if(GestorTS.getInstancia().existeID(lexproc) && 
		GestorTS.getInstancia().getDetalles(lexproc).getTipo().getTipo().equals(TipoDec.TipoProc)){
			reconoce("tkid");
			apilaRet(this.etq);
			this.etq = this.etq + 1;
			this.parametros = GestorTS.getInstancia().getDetalles(lexproc).getTipo().getParametros();
			listaexp();
			if(this.forward.contains(lexproc)){				
				parcheoEtiquetasForward(lexproc,this.etq);
			}else{
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_A,GestorTS.getInstancia().getDetalles(lexproc).getDir()));
			}
			this.etq = this.etq + 1;
		}else{
			GestorErrores.agregaError(19, alexico.getFila(), alexico.getColumna(), "Procedimiento " + lexproc + " no declarado");
			throw new Exception("Procedimiento inexistente");
		}		
	}
	private void listaexp() throws Exception {
		if(alexico.getToken_actual().equals("tk(")){
			reconoce("tk(");
			inicioPaso();
			this.etq = this.etq + longInicioPaso;
			int nparams = expresiones();
			if(nparams != this.parametros.size()){
				GestorErrores.agregaError(20, alexico.getFila(), alexico.getColumna(), "Numero de parametros incorrecto, del procedimiento invocado");
				throw new Exception("Invocacion de procedimiento sin sus correspondientes parametros");
			}	
			finPaso();
			this.etq = this.etq + longFinPaso;
			reconoce("tk)");
		}else{
			if(this.parametros.size() > 0){
				GestorErrores.agregaError(20, alexico.getFila(), alexico.getColumna(), "Falta los parametros del procedimiento invocado");
				throw new Exception("Invocacion de procedimiento sin sus correspondientes parametros");
			}
		}
	}
	private int expresiones() throws Exception {
		int nparams = 0;
		boolean parh = this.parametros.get(0).getModo().equals("var");
		Object[] exp = Expresion(parh);
		Tipo exptipo = (Tipo)exp[0];
		if(!GestorTS.getInstancia().compatibles(this.parametros.get(0).getTipobase(), exptipo)){
			GestorErrores.agregaError(21, alexico.getFila(), alexico.getColumna(), "Tipos incompatibles");
			throw new Exception("Tipos incompatibles");
		}else{
			nparams = 1;
			this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.COPIA));
			pasoParametro(this.parametros.get(0).getModo(), this.parametros.get(0));
			this.etq = this.etq + 1 + longPasoParametro;
			nparams = expresionesRE(nparams);
		}
		return nparams;
	}
	private int expresionesRE(int nparams) throws Exception {
		int nparamsh = nparams;
		if(alexico.getToken_actual().equals("tk,")){
			reconoce("tk,");
			boolean parh = this.parametros.get(nparams).getModo().equals("var");
			Object[] exp = Expresion(parh);
			Tipo exptipo = (Tipo)exp[0];
			this.etq = this.etq + 1 + longPasoParametro;
			nparamsh = nparamsh + 1;
			if(!GestorTS.getInstancia().compatibles(this.parametros.get(nparams),exptipo)){
				GestorErrores.agregaError(21, alexico.getFila(), alexico.getColumna(), "Tipos incompatibles");
				throw new Exception("Tipos incompatibles");
			}else{
				this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.COPIA));
				pasoParametro(this.parametros.get(nparams).getModo(), this.parametros.get(nparams));
				this.etq = this.etq + 1 + longPasoParametro;
			}
		}
		return expresionesRE(nparamsh);
	}
	public void instruccionesRE() throws Exception{
		if(alexico.getToken_actual().equals("tk;")){
			reconoce("tk;");
			instruccion();
			instruccionesRE();
		}
	}
		
	public void declaraciones()throws Exception{
		declaracion();
		declaracionesRE();
	}
	public void declaracion()throws Exception{
		if(alexico.getToken_actual().equals("tkprocedure")){
			declaracionProc();
		}else{
			if(alexico.getToken_actual().equals("tktipo")){
				declaracionTipo();
			}else{
				if(alexico.getToken_actual().equals("tkid") || 
				alexico.getToken_actual().equals("tkboolean") || 
				alexico.getToken_actual().equals("tkcharacter") || 
				alexico.getToken_actual().equals("tknatural") ||
				alexico.getToken_actual().equals("tkinteger") ||
				alexico.getToken_actual().equals("tkfloat")){
					declaracionVar();
				}else{
					GestorErrores.agregaError(12, alexico.getFila(), alexico.getColumna(), "Se requiere procedure, tipo, float, natural, integer, character, boolean o cualquier otro identificador de tipo");
					throw new Exception("Token inesperado");
				}
			}
		}
			
	}
	public void declaracionesRE() throws Exception{
		if(alexico.getToken_actual().equals("tk;")){
			reconoce("tk;");
			if(!alexico.getToken_actual().equals("tk&")){
				declaracion();
				declaracionesRE();
			}
		}
	}
	public void declaracionVar() throws Exception{
		Tipo temp = tipo();
		if(alexico.getToken_actual().equals("tkid")){
			String lexema_var = alexico.getLexema();
			reconoce("tkid");
			Detalles detvar = new Detalles(lexema_var, Clase.var, null, temp, this.nivel);
			if(GestorTS.getInstancia().existeID(lexema_var) && GestorTS.getInstancia().getDetalles(lexema_var).getNivel() == this.nivel){
				GestorErrores.agregaError(14, alexico.getFila(), alexico.getColumna(), "Se ha duplicado la declaración del procedicimento " + lexema_var );
				throw new Exception("Variable duplicada");
			}
			GestorTS.getInstancia().añadeID(lexema_var, Clase.var, detvar.getDir(), detvar.getTipo(), detvar.getNivel());
		}else{
			GestorErrores.agregaError(12, alexico.getFila(), alexico.getColumna(), "Se requiere identificador para el procedimiento");
			throw new Exception("Token inesperado");
		}
	}
	public void declaracionTipo() throws Exception{
		reconoce("tktipo");
		if(alexico.getToken_actual().equals("tkid")){
			String lexema_tipo = alexico.getLexema();
			reconoce("tkid");
			reconoce("tk=");
			Tipo temp = tipo();
			Detalles dettipo = new Detalles(lexema_tipo, Clase.tipo, null, temp, this.nivel);
			if(GestorTS.getInstancia().existeID(lexema_tipo) && GestorTS.getInstancia().getDetalles(lexema_tipo).getNivel() == this.nivel){
				GestorErrores.agregaError(14, alexico.getFila(), alexico.getColumna(), "Se ha duplicado la declaración del procedicimento " + lexema_tipo );
				throw new Exception("Tipo duplicado");
			}
			GestorTS.getInstancia().añadeID(lexema_tipo, Clase.tipo, dettipo.getDir(), dettipo.getTipo(), dettipo.getNivel());
			if(this.pendientes.contains(lexema_tipo))
				this.pendientes.remove(this.pendientes.indexOf(lexema_tipo));
		}else{
			GestorErrores.agregaError(12, alexico.getFila(), alexico.getColumna(), "Se requiere identificador para el procedimiento");
			throw new Exception("Token inesperado");
		}
	}
	public void declaracionProc() throws Exception{
		int inicio = 0;
		reconoce("tkprocedure");
		if(alexico.getToken_actual().equals("tkid")){
			String lexema_proc = alexico.getLexema();
			//Se añade el nuevo ambito
			GestorTS.getInstancia().creaTS();
			//Aumentamos el nivel
			this.nivel = this.nivel + 1;
			//Restaurar en caso de forward
			int dir_aux = this.dir_n[this.nivel];
			//Se maximiza el valor del animadamiento
			if(this.nivel >= this.anidamiento)
				this.anidamiento = this.nivel;
			reconoce("tkid");
			//Busqueda de parametros
			listaParametros();
			Vector<TParam> param_aux = (Vector<TParam>) this.parametros.clone();
			GestorTS.getInstancia().añadeID(lexema_proc, Clase.proc, null, new TProc(0, param_aux, Clase.proc, null), this.nivel);
			this.parametros.clear();
			//declaracionProcFX
			if(alexico.getToken_actual().equals("tkforward")){
				if(GestorTS.getInstancia().existeID(lexema_proc) && GestorTS.getInstancia().getDetalles(lexema_proc).getNivel() == this.nivel){
					GestorErrores.agregaError(14, alexico.getFila(), alexico.getColumna(), "Se ha duplicado la declaración del procedicimento " + lexema_proc );
					throw new Exception("Procedimiento duplicado");
				}
				this.forward.put(lexema_proc, true);
				this.dir_n[this.nivel] = dir_aux;
			}else{
				inicio = cuerpo(lexema_proc);
				if(GestorTS.getInstancia().existeID(lexema_proc) && GestorTS.getInstancia().getDetalles(lexema_proc).getNivel() == this.nivel){
					GestorErrores.agregaError(14, alexico.getFila(), alexico.getColumna(), "Se ha duplicado la declaración del procedicimento " + lexema_proc );
					throw new Exception("Procedimiento duplicado");
				}
				parcheoEtiquetasForward(lexema_proc,inicio);
			}
			GestorTS.getInstancia().borraTS();
			this.nivel = this.nivel - 1;
			if(GestorTS.getInstancia().existeID(lexema_proc) && GestorTS.getInstancia().getDetalles(lexema_proc).getNivel() == this.nivel){
				GestorErrores.agregaError(14, alexico.getFila(), alexico.getColumna(), "Se ha duplicado la declaración del procedicimento " + lexema_proc );
				throw new Exception("Procedimiento duplicado");
			}
			GestorTS.getInstancia().añadeID(lexema_proc, Clase.proc, this.nivel + 1, new TProc(0, param_aux, Clase.proc, null), inicio);
		}else{
			GestorErrores.agregaError(12, alexico.getFila(), alexico.getColumna(), "Se requiere identificador para el procedimiento");
			throw new Exception("Token inesperado");
		}
	}
	//TODO LinkedList recomendado, probar con Integer segun el desarrollo
	public void parcheoEtiquetasForward(String lexproc, int dir){
		LinkedList<Integer> direc = this.procConForwardDone.get(lexproc);
		this.forward.remove(lexproc);
		for(Integer i: direc){
			this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.IR_A, dir));
		}
		this.procConForwardDone.put(lexproc, null);
	}
	public int cuerpo(String lexema) throws Exception{
		int inicio = 0;
		int tam_local = this.dir_n[this.nivel];
		reconoce("tk{");
		if(!alexico.getToken_actual().equals("tk&")){
			declaraciones();
		}
		inicio = this.etq;
		Detalles tempcuerpo = GestorTS.getInstancia().getDetalles(lexema);
		tempcuerpo.setDir(inicio);
		this.etq = this.etq + longPrologo;
		prologo(this.nivel, this.dir_n[this.nivel]);
		reconoce("tk&");
		instrucciones();
		reconoce("tk}");
		this.etq = this.etq + longEpilogo + 1;
		epilogo(this.nivel);
		this.codigo.add(new InstruccionesMaquinaP(OperacionesMaquinaP.APILA_IND));
		return inicio;
	}
	public void listaParametros() throws Exception{
		if(alexico.getToken_actual().equals("tk(")){
			parametros();
			if(!alexico.getToken_actual().equals("tk)")){
				GestorErrores.agregaError(12, alexico.getFila(), alexico.getColumna(), "Se requiere cerrar el )");
				throw new Exception("Token inesperado");
			}else{
				reconoce("tk)");
			}
		}
	}
	public void parametros() throws Exception{
		reconoce("tk(");
		if(!alexico.getToken_actual().equals("tk)")){
			parametro();
			parametrosRE();
		}
	}
	public void parametrosRE() throws Exception{
		if(alexico.getToken_actual().equals("tk,")){
			reconoce("tk,");
			parametro();
			parametrosRE();
		}else{
			if(!alexico.getToken_actual().equals("tk)")){
				GestorErrores.agregaError(12, alexico.getFila(), alexico.getColumna(), "Se requiere identificador para el procedimiento");
				throw new Exception("Token inesperado");
			}
		}
	}
	public void parametro() throws Exception{
		String lexparam;
		Detalles det;
		int tam = 0;
		if(esVar()){
			Tipo tipovp = tipo();
			lexparam = alexico.getLexema();
			reconoce("tkid");
			parametros.add(new TParam(tipovp, "variable", this.dir_n[this.nivel]));
			det = new Detalles(lexparam, Clase.pvar, this.dir_n[this.nivel], tipovp, this.nivel);
			tam = 1;
		}else{
			Tipo tipop = tipo();
			tam = tipop.getTamanyo();
			lexparam = alexico.getLexema();
			reconoce("tkid");
			parametros.add(new TParam(tipop, "valor", this.dir_n[this.nivel]));
			det = new Detalles(lexparam, Clase.var, this.dir_n[this.nivel], tipop, this.nivel);
		}
		if(GestorTS.getInstancia().existeID(lexparam) && GestorTS.getInstancia().getDetalles(lexparam).getNivel() == this.nivel){
			GestorErrores.agregaError(14, alexico.getFila(), alexico.getColumna(), "Se ha duplicado " + alexico.getLexema());
			throw new Exception("Identificador duplicado");
		}
		det.setDir(0);
		GestorTS.getInstancia().añadeID(lexparam, det.getClase(), det.getDir(), det.getTipo(), det.getNivel());
		this.dir_n[this.nivel] = this.dir_n[this.nivel] + tam;
	}
	public Tipo tipo() throws Exception{
		Tipo obj = null;
		if(alexico.getToken_actual().equals("tkid")){
			obj = tipoId();
		}else{			
			if(alexico.getToken_actual().equals("tkboolean")){
				obj = tipoBoolean();
			}else{
				if(alexico.getToken_actual().equals("tkcharacter")){
					obj = tipoCaracter();
				}else{
					if(alexico.getToken_actual().equals("tknatural")){
						obj = tipoNatural();
					}else{
						if(alexico.getToken_actual().equals("tkinteger")){
							obj = tipoEntero();
						}else{
							if(alexico.getToken_actual().equals("tkfloat")){
								obj = tipoReal();
							}else{
								if(alexico.getToken_actual().equals("tkfloat")){
									obj = tipoReal();
								}else{
									if(alexico.getToken_actual().equals("tkarray")){
										obj = tipoArray();
									}else{
										if(alexico.getToken_actual().equals("tkpointer")){
											obj = tipoPuntero();
										}else{
											if(alexico.getToken_actual().equals("tkrecord")){
												obj = tipoRecord();
											}else{
												GestorErrores.agregaError(11, alexico.getFila(), alexico.getColumna(), "Se esperaba un token representante de Tipo en lugar de " + alexico.getToken_actual());
												throw new Exception("Token inesperado");
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return obj;
	}
	public TRegistro tipoRecord() throws Exception{
		reconoce("tkrecord");
		reconoce("tk{");
		Vector<Campo> camp = new Vector<Campo>();
		int tam = 0;
		//Object[0] Vector<Campo> Object[1] tam
		Object[] result_camp = campos(camp,tam);	
		reconoce("tk}");
		return new TRegistro((Vector<Campo>)result_camp[0], (Integer)result_camp[1]);
	}
	public Object[] campos(Vector<Campo> camp, int tam) throws Exception{
		//Object[0] Vector<Campo> Object[1] tam
		Object[] result_camp = campo(camp,tam);
		Object[] result_camp1 = campoRE((Vector<Campo>)result_camp[0],(Integer)result_camp[1]);
		//Object[0] Vector<Campo> Object[1] tam
		return result_camp1;
	}
	private Object[] campoRE(Vector<Campo> campis, Integer tam) throws Exception {
		Object[] reg = new Object[]{campis,tam};
		if(alexico.getToken_actual().equals("tk;")){
			reconoce("tk;");
			Object[] reg2 = campo(campis,tam);
			Vector<Campo> camp2 = (Vector<Campo>) reg2[0];
			int tam2 = (Integer)reg2[1];
			Object[] reg3 = campoRE(camp2,tam2);
			reg = reg3;
		}
		return reg;
	}
	private Object[] campo(Vector<Campo> camp, int tam) throws Exception {
		Tipo tipocam = tipo();
		int tamcamp = 0;
		Vector<Campo> veccamp = null;
		if(alexico.getToken_actual().equals("tkid")){
			String lexcamp = alexico.getLexema();
			if(GestorTS.getInstancia().campoExiste(camp, lexcamp)){
				GestorErrores.agregaError(26, alexico.getFila(), alexico.getColumna(), "El campo " + lexcamp + "ya ha sido declarado");
				throw new Exception("Campo duplicado");
			}else{
				tamcamp = tam + tipocam.getTamanyo();
				veccamp = (Vector<Campo>) camp.clone();
				veccamp.add(new Campo(lexcamp, tipocam, tipocam.getTamanyo()));
			}
		}	
		return new Object[]{veccamp,tamcamp};
	}
	public TPuntero tipoPuntero() throws Exception{
		reconoce("tkpointer");	
		return new TPuntero(tipo());
	}
	public TArray tipoArray() throws Exception{
		reconoce("tkarray");
		Integer nelems = null;
		TArray temp = null;
		reconoce("tk[");
		if(alexico.getToken_actual().equals("tknatural")){
			nelems = Integer.valueOf(alexico.getLexema());
			reconoce("tknatural");
		}
		reconoce("tk]");
		reconoce("tkof");
		temp = new TArray(nelems, tipo());
		return temp;
	}
	public TReferencia tipoId() throws Exception{
		String lexid = alexico.getLexema();
		TReferencia temp = null;
		if(GestorTS.getInstancia().existeID(lexid)){
			temp = new TReferencia(GestorTS.getInstancia().getDetalles(lexid).getTipo().getTamanyo(),lexid);
		}else{
			this.pendientes.add(lexid);
			temp = new TReferencia(1, lexid);
		}
		reconoce("tkid");
		return temp;
	}
	public Tipo tipoReal() throws Exception{
		reconoce("tkfloat");
		return new Tipo(TipoDec.TipoFloat,1);
	}
	public Tipo tipoEntero() throws Exception{
		reconoce("tkinteger");
		return new Tipo(TipoDec.TipoInteger,1);
	}
	public Tipo tipoBoolean() throws Exception{
		reconoce("tkboolean");
		return new Tipo(TipoDec.TipoBoolean,1);
	}
	public Tipo tipoCaracter() throws Exception{
		reconoce("tkcharacter");
		return new Tipo(TipoDec.TipoCharacter,1);
	}
	public Tipo tipoNatural() throws Exception{
		reconoce("tknatural");
		return new Tipo(TipoDec.TipoNatural,1);
	}
	public boolean esVar() throws Exception{
		boolean var = false;
		if(alexico.getToken_actual().equals("tkvar")){			
			reconoce("tkvar");
			var = true;
		}
		return var;
	}

	
	
	
}
