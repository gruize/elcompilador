package es.ucm.plg.interprete;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

import es.ucm.plg.interprete.datoPila.DatoPila;
import es.ucm.plg.interprete.memoria.Memoria;


public class Interprete {

	private ArrayDeque<DatoPila> pila;
	private ArrayList<InstruccionInterprete> programa;
	private Memoria memoria;
	private boolean parar;
	private int cp;
	private boolean modoDepuracion;
	private StringBuilder sb;
	private BufferedReader reader;
	private PrintWriter writer;

	/**
	 * Crea un interprete con tantas posiciones de memoria como se le indique
	 * 
	 * @param longMem
	 *            Tamanyo de la memoria
	 * @param depuracion
	 *            Indica si debe mostrar traza o no
	 */
	public Interprete(int longMem, boolean depuracion, Reader reader,
			PrintWriter salida) {
		programa = null;
		pila = null;
		memoria = new Memoria(longMem);
		modoDepuracion = depuracion;
		if (depuracion)
			sb = new StringBuilder(100);
		else
			sb = null;
		this.reader = new BufferedReader(reader);
		writer = salida;
	}

	/**
	 * Crea un interprete con tantas posiciones de memoria como se le indique
	 * 
	 * @param longMem
	 *            Tamanyo de la memoria
	 * @param depuracion
	 *            Indica si debe mostrar traza o no
	 * @param in
	 *            Stream de entrada de datos en las instrucciones de lectura
	 * @param salida
	 *            Salida para los datos de salida
	 */
	public Interprete(int longMem, boolean depuracion, InputStream in,
			PrintWriter salida) {
		this(longMem, depuracion, new InputStreamReader(in), salida);
	}

	/**
	 * Crea un interprete con tantas posiciones de memoria como se le indique
	 * 
	 * @param longMem
	 *            Tamanyo de la memoria
	 * @param depuracion
	 *            Indica si debe mostrar traza o no
	 */
	public Interprete(int longMem, boolean depuracion) {
		this(longMem, depuracion, new InputStreamReader(System.in),
				new PrintWriter(System.out, true));
	}

	/**
	 * Crea un interprete con un tamanyo de memoria por defecto (1000)
	 * 
	 * @param depuracion
	 *            Indica si debe mostrar traza o no
	 */
	public Interprete(boolean depuracion) {
		this(1000, depuracion);
	}

	/**
	 * Crea un interprete con un tamanyo de memoria por defecto (1000)
	 * 
	 * @param depuracion
	 *            Indica si debe mostrar traza o no
	 * @param entrada
	 *            Stream de entrada de datos en las instrucciones de lectura
	 * @param salida
	 *            Salida para los datos de salida
	 */
	public Interprete(boolean depuracion, InputStream entrada,
			PrintWriter salida) {
		this(1000, depuracion, entrada, salida);
	}

	/**
	 * Crea un interprete con un tamanyo de memoria por defecto (1000)
	 * 
	 * @param depuracion
	 *            Indica si debe mostrar traza o no
	 * @param reader
	 *            Lector de entrada de datos en las instrucciones de lectura
	 * @param salida
	 *            Salida para los datos de salida
	 */
	public Interprete(boolean depuracion, Reader reader, PrintWriter salida) {
		this(1000, depuracion, reader, salida);
	}

	/**
	 * Dado un fichero, crea un LectorPila del que obtiene un programa valido
	 * @param f  el fichero binario fuente
	 * @throws InterpreteExcepcion 
	 */
	public void leerPrograma(File f) throws InterpreteExcepcion{
		LectorPila lector = new LectorPila();		
		programa = lector.leerPrograma(f);
		pila = new ArrayDeque<DatoPila>();
	}

	
	/**
	 * Muestra el estado de la maquina P en un momento dado
	 * 
	 * @return El estado de la maquina P
	 */
	public String mostrarEstado() {

		sb.delete(0, sb.length()); // se resetea

		boolean tiene_algo = false;
		sb.append("Contenido de la memoria:\n");
		for (int i = 0; i < memoria.getMemoria().length; i++) {
			if (memoria.getMemoria()[i] != null) {
				sb.append("\t" + i + ") " + memoria.getMemoria()[i] + "\n");
				tiene_algo = true;
			}
		}
		if (!tiene_algo) {
			sb.append("Vacia\n");
		}

		tiene_algo = false;
		sb.append("Contenido de la pila:\n");
		Iterator<DatoPila> it = pila.iterator();
		int i = 0;
		while (it.hasNext()) {
			sb.append("\t" + i + ") " + it.next() + "\n");
			tiene_algo = true;
			i++;
		}
		if (!tiene_algo) {
			sb.append("Vacia\n");
		}

		if (getCp() < programa.size()) 
			sb.append("\nProxima instruccion: (" + getCp() + ") "
					+ programa.get(getCp()).toString() + "\n");
		else
			sb.append("\nFin del programa\n");
		
		return new String(sb);
	}

	/**
	 * Ejecuta el programa que se haya leido con anterioridad
	 * @throws InterpreteExcepcion 
	 */
	public void ejecutarPrograma() throws InterpreteExcepcion{
		try {
			if (programa == null)
				throw new NullPointerException();
			setCp(0);
			setParar(false);
			while (!isParar()) {
				if (modoDepuracion) {
					writer.print(mostrarEstado());
					writer.flush();
					reader.readLine();					
					writer.println();
					writer.flush();
				}
				if ((programa.get(getCp())).ejecutate(this))
					setCp(cp + 1);
			}
		} catch (IOException e) {
			throw new InterpreteExcepcion("Ejecución del programa", "Error al leer el programa");
		} catch (NullPointerException e){
			throw new InterpreteExcepcion("Ejecución del programa", "Programa no iniciado");
		}
	}

	/**
	 * @return la pila
	 */
	public ArrayDeque<DatoPila> getPila() {
		return pila;
	}

	/**
	 * @return si se esta o no con la maquina parada
	 */
	public boolean isParar() {
		return parar;
	}

	/**
	 * @param true si la maquina debe pararse
	 */
	public void setParar(boolean parar) {
		this.parar = parar;
	}

	/**
	 * @return el contador de programa (la posicion de la instruccion que se
	 *         esta ejecutando en este momento)
	 */
	public int getCp() {
		return cp;
	}

	/**
	 * Fija el contador de programa a un valor dado
	 * 
	 * @param cp
	 *            nuevo contador de programa
	 */
	public void setCp(int cp) {
		if (cp == programa.size())
			setParar(true);
		this.cp = cp;
	}

	/**
	 * @return la memoria de la maquina
	 */
	public Memoria getMemoria() {
		return memoria;
	}

	/**
	 * @return the reader
	 */
	public BufferedReader getReader() {
		return reader;
	}

	/**
	 * @return the writer
	 */
	public PrintWriter getWriter() {
		return writer;
	}

	public ArrayList<InstruccionInterprete> getPrograma() {
		return programa;
	}

	public Integer reservar(Integer tam) {
		// TODO Auto-generated method stub
		return null;
	}

	public void liberar(Integer dir, Integer tam) {
		// TODO Auto-generated method stub
		
	}	
	
}
