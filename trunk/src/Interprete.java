import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

import tablaDeSimbolos.util.Tipos.TipoDec;
import util.Memoria;
import util.OperacionesMaquinaP;
import util.InstruccionesMaquinaP;

public class Interprete {

	private Memoria mem;
	private int contador;
	private boolean debug;
	private Vector<InstruccionesMaquinaP> codigo;
	private Stack<Object> pila;
	private int antiguo_contador;
	
	
	public Interprete(Vector<InstruccionesMaquinaP> codigo) {
		this.codigo = codigo;
		this.mem = new Memoria();
	}
	
	public Memoria getMem() {
		return mem;
	}

	public void setMem(Memoria mem) {
		this.mem = mem;
	}

	public int getAntiguo_contador() {
		return antiguo_contador;
	}

	public void setAntiguo_contador(int antiguo_contador) {
		this.antiguo_contador = antiguo_contador;
	}

	public int getContador() {
		return contador;
	}

	public void setContador(int contador) {
		this.contador = contador;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Vector<InstruccionesMaquinaP> getCodigo() {
		return codigo;
	}

	public void setCodigo(Vector<InstruccionesMaquinaP> codigo) {
		this.codigo = codigo;
	}

	public Stack<Object> getPila() {
		return pila;
	}

	public void setPila(Stack<Object> pila) {
		this.pila = pila;
	}
	
	public boolean esReal(Object obj){
		boolean real = false;
		if ( (String.valueOf((String) obj).indexOf(".") > 0) || 
				(String.valueOf((String) obj).indexOf("e") > 0) || 
				(String.valueOf((String) obj).indexOf("E") > 0))
			real = true;
		return real;
	}
	
	public boolean esEntero(Object obj){
		boolean entero = false;
		if(!esReal(obj) && ((String.valueOf((String) obj).indexOf("+") > 0) ||
				(String.valueOf((String) obj).indexOf("-",1) > 0)))
			entero = true;
		return entero;
	}
	
	public boolean esNatural(Object obj){
		boolean natural = false;
		if(!esReal(obj) && !esEntero(obj) && !esCaracter(obj) && !esBooleano(obj))
			natural = true;
		return natural;
	}
	
	public boolean esCaracter(Object obj){
		boolean caracter = false;
		if(!esReal(obj) && !esEntero(obj) && (String.valueOf((String) obj).indexOf("'") > 0))
			caracter = true;
		return caracter;
	}
	
	public boolean esBooleano(Object obj){
		boolean bool = false;
		if(String.valueOf((String)obj).equals("true") || String.valueOf((String)obj).equals("false"))
			bool = true;
		return bool;
	}
	
	public float obtenerValor(Object obj){
		float valor = 0;
		//Valor booleano
		if(esBooleano(obj)){
			if(String.valueOf((String)obj).equals("true"))
				valor = 1;
			else
				valor = 0;
		}else{
			//Valor real
			if(esReal(obj)){
				valor = Float.valueOf((String) obj);
			}else{
				//Valor caracter
				if(esCaracter(obj))
					valor = String.valueOf((String) obj).codePointAt(1);
				else
					//Valor entero o natural
					valor = Float.valueOf((String) obj);
			}
		}		
		return valor;
	}
	
	public void ejecuta(boolean ejecucion)throws Exception{
		this.debug = ejecucion;
		this.pila = new Stack<Object>();
		this.contador = 0;
		Object cima = null;
		Object subcima = null;
		Number resul_number = null;
		Boolean resul_bool = null;
		String resultado = null;
		InputStreamReader lectura_debug = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(lectura_debug);
		if(debug){
			System.out.println("\n\nEstado inicial de la máquina:\n");
			System.out.println(mem.escribir());
			reader.readLine();
		}
		while(this.contador < this.codigo.size()){
			switch(getCodigo().get(getContador()).getOp()){
			case MENOR:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{	
					//Caracteres
					if(esCaracter(cima) && esCaracter(subcima)){
						resul_bool = obtenerValor(subcima) < obtenerValor(cima); 
					}else{
						//Booleanos
						if(esBooleano(cima) && esBooleano(subcima))
							resul_bool = obtenerValor(subcima) < obtenerValor(cima);
						else{
							//Numerico
							resul_bool = obtenerValor(subcima) < obtenerValor(cima);
						}
					}
					resultado = String.valueOf(resul_bool);
					getPila().push(resultado);
					contador++;
				}
				break;
			case MAYOR:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{	
					//Caracteres
					if(esCaracter(cima) && esCaracter(subcima)){
						resul_bool = obtenerValor(subcima) > obtenerValor(cima); 
					}else{
						//Booleanos
						if(esBooleano(cima) && esBooleano(subcima))
							resul_bool = obtenerValor(subcima) > obtenerValor(cima);
						else{
							//Numerico
							resul_bool = obtenerValor(subcima) > obtenerValor(cima);
						}
					}
					resultado = String.valueOf(resul_bool);
					getPila().push(resultado);
					contador++;
				}				
				break;
			case MENOR_IGUAL:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{	
					//Caracteres
					if(esCaracter(cima) && esCaracter(subcima)){
						resul_bool = obtenerValor(subcima) <= obtenerValor(cima); 
					}else{
						//Booleanos
						if(esBooleano(cima) && esBooleano(subcima))
							resul_bool = obtenerValor(subcima) <= obtenerValor(cima);
						else{
							//Numerico
							resul_bool = obtenerValor(subcima) <= obtenerValor(cima);
						}
					}
					resultado = String.valueOf(resul_bool);
					getPila().push(resultado);
					contador++;
				}			
				break;
			case MAYOR_IGUAL:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{	
					//Caracteres
					if(esCaracter(cima) && esCaracter(subcima)){
						resul_bool = obtenerValor(subcima) >= obtenerValor(cima); 
					}else{
						//Booleanos
						if(esBooleano(cima) && esBooleano(subcima))
							resul_bool = obtenerValor(subcima) >= obtenerValor(cima);
						else{
							//Numerico
							resul_bool = obtenerValor(subcima) >= obtenerValor(cima);
						}
					}
					resultado = String.valueOf(resul_bool);
					getPila().push(resultado);
					contador++;
				}				
				break;
			case IGUAL:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{	
					//Caracteres
					if(esCaracter(cima) && esCaracter(subcima)){
						resul_bool = obtenerValor(subcima) == obtenerValor(cima); 
					}else{
						//Booleanos
						if(esBooleano(cima) && esBooleano(subcima))
							resul_bool = obtenerValor(subcima) == obtenerValor(cima);
						else{
							//Numerico
							resul_bool = obtenerValor(subcima) == obtenerValor(cima);
						}
					}
					resultado = String.valueOf(resul_bool);
					getPila().push(resultado);
					contador++;
				}				
				break;
			case DISTINTO:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{	
					//Caracteres
					if(esCaracter(cima) && esCaracter(subcima)){
						resul_bool = obtenerValor(subcima) != obtenerValor(cima); 
					}else{
						//Booleanos
						if(esBooleano(cima) && esBooleano(subcima))
							resul_bool = obtenerValor(subcima) != obtenerValor(cima);
						else{
							//Numerico
							resul_bool = obtenerValor(subcima) != obtenerValor(cima);
						}
					}
					resultado = String.valueOf(resul_bool);
					getPila().push(resultado);
					contador++;
				}				
				break;
			case SUMA:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{
					if(esReal(cima)){
						//Operador 1 real
						Float op1 = Float.valueOf((String) cima);
						if(esReal(subcima)){
							Float op2 = Float.valueOf((String) subcima);
							//Ambos reales
							resul_number = op1 + op2;
						}else{
							//Entero o Natural
							Integer op3 = Integer.valueOf((String) subcima);
							resul_number = op1 + op3;
						}
						resultado = String.valueOf(resul_number);
					}else{
						if(esReal(subcima)){
							//Operador 2 real
							Float op4 = Float.valueOf((String) subcima);
							Integer op5 = Integer.valueOf((String) cima);
							resul_number = op4 + op5;
							resultado = String.valueOf(resul_number);
						}else{
							//Los 2 son naturales o enteros, pero Java los asume como enteros
							Integer op6 = Integer.valueOf((String) cima);
							Integer op7 = Integer.valueOf((String) subcima);
							resul_number = op6 + op7;
							resultado = String.valueOf(resul_number);
							if(esEntero(cima) || esEntero(subcima)){
								if(op6 >= 0 && op7 >= 0 && op6 + op7 >= 0){
									resultado = "+" + resultado;
								}					
							}
						} 
					}
					getPila().push(resultado);
					contador++;
				}
				break;
			case RESTA:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{
					if(esReal(cima)){
						//Operador 2 real
						Float op1 = Float.valueOf((String) cima);
						if(esReal(subcima)){
							Float op2 = Float.valueOf((String) subcima);
							//Ambos reales
							resul_number = op2 - op1;
						}else{
							//Entero o Natural
							Integer op3 = Integer.valueOf((String) subcima);
							resul_number = op3 - op1;
						}
						resultado = String.valueOf(resul_number);
					}else{
						if(esReal(subcima)){
							//Operador 1 real
							Float op4 = Float.valueOf((String) subcima);
							Integer op5 = Integer.valueOf((String) cima);
							resul_number = op4 - op5;
							resultado = String.valueOf(resul_number);
						}else{
							//Los 2 son naturales o enteros, pero Java los asume como enteros
							Integer op6 = Integer.valueOf((String) cima);
							Integer op7 = Integer.valueOf((String) subcima);
							resul_number = op7 - op6;
							resultado = String.valueOf(resul_number);
							if(esEntero(cima) || esEntero(subcima)){
								if(op7 - op6 >= 0 ){
									resultado = "+" + resultado;
								}
							}else{
								//Son naturales
								throw new Exception("Ambos son naturales. No existen numeros inferiores al 0");
							}
						} 
					}
					getPila().push(resultado);
					contador++;
				}
				break;
			case MULT:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{
					if(esReal(cima)){
						//Operador 1 real
						Float op1 = Float.valueOf((String) cima);
						if(esReal(subcima)){
							Float op2 = Float.valueOf((String) subcima);
							//Ambos reales
							resul_number = op1 * op2;
						}else{
							//Entero o Natural
							Integer op3 = Integer.valueOf((String) subcima);
							resul_number = op1 * op3;
						}
						resultado = String.valueOf(resul_number);
					}else{
						if(esReal(subcima)){
							//Operador 2 real
							Float op4 = Float.valueOf((String) subcima);
							Integer op5 = Integer.valueOf((String) cima);
							resul_number = op4 * op5;
							resultado = String.valueOf(resul_number);
						}else{
							//Los 2 son naturales o enteros, pero Java los asume como enteros
							Integer op6 = Integer.valueOf((String) cima);
							Integer op7 = Integer.valueOf((String) subcima);
							resul_number = op6 * op7;
							resultado = String.valueOf(resul_number);
							if(esEntero(cima) || esEntero(subcima)){
								if(op6 >= 0 && op7 >= 0 && op6 * op7 >= 0){
									resultado = "+" + resultado;
								}					
							}
						} 
					}
					getPila().push(resultado);
					contador++;
				}
				break;
			case DIVISION:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{
					if(esReal(cima)){
						//Operador 2 real
						Float op1 = Float.valueOf((String) cima);
						if(esReal(subcima)){
							Float op2 = Float.valueOf((String) subcima);
							//Ambos reales
							resul_number = op2 / op1;
						}else{
							//Entero o Natural
							Integer op3 = Integer.valueOf((String) subcima);
							resul_number = op3 / op1;
						}
						resultado = String.valueOf(resul_number);
					}else{
						if(esReal(subcima)){
							//Operador 1 real
							Float op4 = Float.valueOf((String) subcima);
							Integer op5 = Integer.valueOf((String) cima);
							resul_number = op4 / op5;
							resultado = String.valueOf(resul_number);
						}else{
							//Los 2 son naturales o enteros, pero Java los asume como enteros
							Integer op6 = Integer.valueOf((String) cima);
							Integer op7 = Integer.valueOf((String) subcima);
							resul_number = op7 / op6;
							Integer resent = Integer.valueOf(String.valueOf(resul_number));
							resultado = String.valueOf(resent);
							if(esEntero(cima) || esEntero(subcima)){
								if(op6 >= 0 && op7 >= 0 && op7 / op6 >= 0){
									resultado = "+" + resultado;
								}					
							}
						} 
					}
					getPila().push(resultado);
					contador++;
				}
				break;
			case MODULO:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{
					//Primer operando natural o entero y segundo operando natural
					if((esEntero(subcima) || esNatural(subcima)) && esNatural(cima)){
						Integer nat_op2 = Integer.valueOf((String) cima);
						Integer ent_op1 = Integer.valueOf((String) subcima);
						Integer ent_res = ent_op1 % nat_op2;
						resultado = String.valueOf(ent_res);
						if(esEntero(subcima)){
							resultado = "+" + resultado;
						}
						getPila().push(resultado);
						contador++;
					}else{
						throw new Exception("Tipo de valor inadecuado");
					}					
				}				
				break;
			case Y_LOGICA:
				cima = getPila().pop();
				subcima = getPila().pop();
				Boolean b1 = Boolean.valueOf((String)cima);
				Boolean b2 = Boolean.valueOf((String)subcima);
				if(b1.equals(null) && b2.equals(null))
					throw new Exception("Valores a null");
				resultado = String.valueOf(b1 && b2);
				getPila().push(resultado);
				contador++;
				break;
			case O_LOGICA:
				cima = getPila().pop();
				subcima = getPila().pop();
				Boolean b3 = Boolean.valueOf((String)cima);
				Boolean b4 = Boolean.valueOf((String)subcima);
				if(b3.equals(null) && b4.equals(null))
					throw new Exception("Valores a null");
				resultado = String.valueOf(b3 || b4);
				getPila().push(resultado);
				contador++;
				break;
			case NEG_LOGICA:
				cima = getPila().pop();
				Boolean b5 = Boolean.valueOf((String)cima);
				if(b5.equals(null))
					throw new Exception("Valor a null");
				resultado = String.valueOf(!b5);
				getPila().push(resultado);
				contador++;
				break;
			case SIGNO_MENOS:
				cima = getPila().pop();
				if(esReal(cima)){
					resultado = String.valueOf(- Float.valueOf((String) cima));
					getPila().push(resultado);
				}else{
					resultado = String.valueOf(- Integer.valueOf((String) cima));
					getPila().push(resultado);
				}					
				contador++;
				break;
			case DESP_IZQ:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{
					if(esNatural(cima) && esNatural(subcima)){
						Integer des_izq_res = Integer.valueOf((String) subcima) << Integer.valueOf((String) cima);
						resultado = String.valueOf(des_izq_res);
						getPila().push(resultado);
						contador++;
					}else{
						throw new Exception("Tipo de operador inadecuado");
					}					
				}
				break;
			case DESP_DER:
				cima = getPila().pop();
				subcima = getPila().pop();
				if(cima.equals("null") || subcima.equals("null"))
					throw new Exception("Valores a null");
				else{
					if(esNatural(cima) && esNatural(subcima)){
						Integer des_der_res = Integer.valueOf((String) subcima) >> Integer.valueOf((String) cima);
						resultado = String.valueOf(des_der_res);
						getPila().push(resultado);
						contador++;
					}else{
						throw new Exception("Tipo de operador inadecuado");
					}					
				}				
				break;
			case CAST_NAT:
				cima = getPila().pop();
				if(esNatural(cima))
					resultado = String.valueOf(cima);
				else{
					if(esCaracter(cima))
						resultado = String.valueOf(Integer.valueOf(String.valueOf(obtenerValor(cima))));
					else
						throw new Exception("Tipo de operador inadecuado");
				}				
				getPila().push(resultado);
				contador++;				
				break;
			case CAST_INT:
				cima = getPila().pop();
				if(esEntero(cima))
					resultado = String.valueOf(cima);
				else{
					if(esReal(cima))
						resultado = String.valueOf(Integer.valueOf(String.valueOf(obtenerValor(cima))));
					else{
						if(esNatural(cima))
							resultado = String.valueOf("+" + Integer.valueOf((String) cima));
						else{
							if(esCaracter(cima))
								resultado = String.valueOf(Integer.valueOf(String.valueOf("+" + obtenerValor(cima))));
							else
								throw new Exception("Tipo de operador inadecuado");
						}
					}
				}
				getPila().push(resultado);
				contador++;				
				break;
			case CAST_CHAR:
				cima = getPila().pop();
				if(esCaracter(cima))
					resultado = String.valueOf(cima);
				else{
					if(esNatural(cima))
						resultado = String.valueOf((char)((int)Integer.valueOf((String) cima)));
					else{
						throw new Exception("Tipo de operador inadecuado");
					}						
				}					
				getPila().push(resultado);
				contador++;								
				break;
			case CAST_FLOAT:
				cima = getPila().pop();
				if(esReal(cima))
					resultado = String.valueOf(cima);
				else{
					if(esEntero(cima) || esNatural(cima))						
						resultado = String.valueOf(Float.valueOf((String) cima));
					else{
						if(esCaracter(cima))
							resultado = String.valueOf(obtenerValor(cima));
						else{
							throw new Exception("Tipo de operador inadecuado");
						}
					}
				}				
				getPila().push(resultado);
				contador++;			
				break;
			case APILA:
				getPila().push(String.valueOf(getCodigo().get(getContador()).getArg()));
				contador++;
				break;
			case DESAPILA:
				getPila().pop();
				contador++;
				break;
			case APILA_DIR:
				if(getMem().obtenerPosicion(Integer.valueOf(getCodigo().get(contador).getArg().toString())) == null)
					throw new Exception("Posicion de memoria inexistente");
				else{
					pila.push(String.valueOf(getMem().obtenerPosicion(Integer.valueOf(getCodigo().get(contador).getArg().toString()))));
					contador++;
				}
				break;
			case DESAPILA_DIR:
				Integer pos_des_dir = Integer.valueOf(getCodigo().get(contador).getArg().toString());
				getMem().modificarPosicion(pos_des_dir, getPila().pop().toString());
				contador++;
				break;
			case APILA_IND:
				cima = getPila().pop();
				getPila().push(getMem().obtenerPosicion(Integer.valueOf(cima.toString())));
				contador++;
				break;
			case DESAPILA_IND:
				cima = getPila().pop();
				subcima = getPila().pop();
				Integer pos_des_ind = Integer.valueOf(subcima.toString());
				getMem().modificarPosicion(pos_des_ind, cima.toString());
				contador++;
				break;
			case LEER:
				resultado = null;
				if(debug)
					System.out.println("Introduzca el/los datos/s");
				InputStreamReader input = new InputStreamReader(System.in);
				BufferedReader buffer_reader = new BufferedReader(input);
				String leido = buffer_reader.readLine();
				TipoDec tipo_argumento = (TipoDec) getCodigo().get(getContador()).getArg(); 
				if(tipo_argumento == TipoDec.TipoFloat)
					resultado = String.valueOf(Float.valueOf(leido));
				else{
					if(tipo_argumento == TipoDec.TipoInteger)
						resultado = String.valueOf("+" + Integer.valueOf(leido));
					else{
						if(tipo_argumento == TipoDec.TipoNatural && Integer.valueOf(leido) >= 0)
							resultado = String.valueOf(Integer.valueOf(leido));
						else{
							if(tipo_argumento == TipoDec.TipoCharacter)
								resultado = String.valueOf("'" + leido.charAt(0) + "'");
							else{
								if(tipo_argumento == TipoDec.TipoBoolean && (leido.equals("true") || leido.equals("false")))
									resultado = String.valueOf(leido);
								else
									throw new Exception("El valor introducido no corresponde al tipo esperado");
							}
						}
					}
				}
				if(resultado != null)
					getPila().push(resultado);
				contador++;
				break;
			case ESCRIBIR:
				cima = getPila().pop();
				if(debug)
					System.out.println("Salida:");
				System.out.println(String.valueOf((String)cima));
				contador++;
				break;
			case IR_A:
				//Ir a la instruccion que indica el argumento de la instruccion actual
				antiguo_contador = contador;
				contador = Integer.valueOf(getCodigo().get(contador).getArg().toString());
				break;
			case IR_F:
				//Ir a la instruccion que indica el argumento de la instruccion actual
				//si el valor de la cima de la pila es falso, si no, continuar
				cima = getPila().pop();
				antiguo_contador = contador;
				if(String.valueOf((String) cima).equals("false"))
					contador = Integer.valueOf(getCodigo().get(contador).getArg().toString());					
				else
					contador++;
				break;
			case IR_V:
				//Ir a la instruccion que indica el argumento de la instruccion actual
				//si el valor de la cima de la pila es verdadero, si no, continuar
				cima = getPila().pop();
				antiguo_contador = contador;
				if(String.valueOf((String) cima).equals("true"))
					contador = Integer.valueOf(getCodigo().get(contador).getArg().toString());					
				else
					contador++;
				break;
			case IR_IND:
				//Ir a la instruccion que indica la cima de la pila
				cima = getPila().pop();	
				antiguo_contador = contador;
				contador = Integer.valueOf(cima.toString());
				break;
			case MUEVE:
				cima = getPila().pop();
				subcima = getPila().pop();
				for(int i = 0; i < Integer.valueOf(String.valueOf(getCodigo().get(getContador()).getArg())); i++){
					getMem().modificarPosicion(Integer.valueOf(String.valueOf(subcima)) + i,getMem().obtenerPosicion(Integer.valueOf(String.valueOf(cima)) + i));
				}				
				contador++;
				break;
			case COPIA:
				getPila().push(getPila().peek());
				contador++;
				break;
			case NEW:				
				resultado = String.valueOf(getMem().reservar(Integer.valueOf(String.valueOf(getCodigo().get(getContador()).getArg()))));
				getPila().push(resultado);
				contador++;
				break;
			case DEL:				
				getMem().liberar(Integer.valueOf(String.valueOf(getPila().pop())),Integer.valueOf(String.valueOf(getCodigo().get(getContador()).getArg())));
				contador++;
				break;
			case STOP:
				antiguo_contador = contador;
				this.contador = this.codigo.size();
				break;
			}
			if(debug){
				System.out.println();
				if(getCodigo().get(getContador()).getOp() == OperacionesMaquinaP.IR_A ||
					getCodigo().get(getContador()).getOp() == OperacionesMaquinaP.IR_F ||
					getCodigo().get(getContador()).getOp() == OperacionesMaquinaP.IR_V ||
					getCodigo().get(getContador()).getOp() == OperacionesMaquinaP.IR_IND ||
					getCodigo().get(getContador()).getOp() == OperacionesMaquinaP.STOP){
					System.out.println("Instruccion: " + getCodigo().get(getAntiguo_contador()).getOp().toString());
					if(getCodigo().get(getAntiguo_contador()).getArg() != null)
						System.out.println("Argumento: " + getCodigo().get(getAntiguo_contador()).getArg().toString());
				}else{
					System.out.println("Instruccion: " + getCodigo().get(getContador() - 1).getOp().toString());
					if(getCodigo().get(getContador() - 1).getArg() != null)
						System.out.println("Argumento: " + getCodigo().get(getContador() - 1).getArg().toString());
				}
				System.out.println();
				System.out.println("Estado de la pila::::::::::::::::::::::::::::::::::::::::::::::::::::");				
				for(Object obj : pila){
					System.out.println(obj.toString());
				}
				System.out.println();
				System.out.println("Estado de la memoria::::::::::::::::::::::::::::::::::::::::::::::::::");
				escribir();
				reader.readLine();											
			}	
		}
	}
	
	private void escribir() {
		for(Enumeration<Integer> e = getMem().getMemoria().keys(); e.hasMoreElements();){
			Integer elem = e.nextElement();
			System.out.println(elem + ":  " + getMem().obtenerPosicion(elem).toString());			
		}
	}

	public static void main(String[] args) throws Exception {
		boolean modo = false;
		Vector<InstruccionesMaquinaP> cod = null;
		if(args.length == 2){
			if(args[1] == "d" || args[1] == "D")
				modo = true;
			cod = (Vector<InstruccionesMaquinaP>)lectura(args[0]);
		}else{
			if(args.length == 1){
				cod = (Vector<InstruccionesMaquinaP>)lectura(args[0]);
			}else
				System.out.println("Llamada incorrecta (1 o 2 parametros que corresponden al fichero y la confirmacion del modo debug)");
		}
		if(args.length >= 1){
			Interprete inter = new Interprete(cod);
			inter.ejecuta(modo);
		}		
	}
	
	public static Object lectura(String ruta) throws Exception {
		Object obj = null;
		FileInputStream archivo = new FileInputStream (ruta);
		ObjectInputStream objeto = new ObjectInputStream (archivo);
		obj = objeto.readObject();
		objeto.close();
		archivo.close();	
		return obj;
	}
}
