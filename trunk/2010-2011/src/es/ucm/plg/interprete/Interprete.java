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
import es.ucm.plg.interprete.memoria.Hueco;
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

	public Interprete(int longMem, boolean depuracion, InputStream in,
			PrintWriter salida) {
		this(longMem, depuracion, new InputStreamReader(in), salida);
	}

	public Interprete(int longMem, boolean depuracion) {
		this(longMem, depuracion, new InputStreamReader(System.in),
				new PrintWriter(System.out, true));
	}

	public Interprete(boolean depuracion) {
		this(1000, depuracion);
	}

	public Interprete(boolean depuracion, InputStream entrada,
			PrintWriter salida) {
		this(1000, depuracion, entrada, salida);
	}

	public Interprete(boolean depuracion, Reader reader, PrintWriter salida) {
		this(1000, depuracion, reader, salida);
	}

	public void leerPrograma(File f) throws InterpreteExcepcion {
		LectorPila lector = new LectorPila();
		programa = lector.leerPrograma(f);
		pila = new ArrayDeque<DatoPila>();
	}

	public String mostrarEstado() {

		sb.delete(0, sb.length()); // se resetea

		boolean tiene_algo = false;
		sb.append("Contenido de la memoria:\n");
		for (int i = 0; i < memoria.getMemoria().length; i++) {
			if ((memoria.getMemoria()[i] != null) && 
					(memoria.getMemoria()[i].getEntero() != Integer.MIN_VALUE)) {
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

	public void ejecutarPrograma() throws InterpreteExcepcion {
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
			throw new InterpreteExcepcion("Ejecución del programa",
					"Error al leer el programa");
		} catch (NullPointerException e) {
			throw new InterpreteExcepcion("Ejecución del programa",
					"Programa no iniciado");
		}
	}

	public ArrayDeque<DatoPila> getPila() {
		return pila;
	}

	public boolean isParar() {
		return parar;
	}

	public void setParar(boolean parar) {
		this.parar = parar;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		if (cp == programa.size())
			setParar(true);
		this.cp = cp;
	}

	public Memoria getMemoria() {
		return memoria;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public ArrayList<InstruccionInterprete> getPrograma() {
		return programa;
	}

	public Integer reservar(Integer tam) {
		Iterator<Hueco> iterator = memoria.getHuecos().iterator();
		Hueco huecoAux;
		int direccion = -1;

		while (iterator.hasNext()) {
			huecoAux = iterator.next();
			if (huecoAux.getTam() > tam) {
				direccion = huecoAux.getDir() + huecoAux.getTam() - tam;
				huecoAux.setTam(huecoAux.getTam() - tam);
				break;
			} else if (huecoAux.getTam() == tam) {
				direccion = huecoAux.getDir();
				memoria.getHuecos().remove(huecoAux);
				break;
			}
		}
		return direccion;
	}

	public void liberar(Integer dir, Integer tam) {
		{
			Iterator<Hueco> iterator = memoria.getHuecos().iterator();
			Hueco huecoAux = null;
			Hueco pred;

			while (iterator.hasNext()) {
				pred = huecoAux;
				huecoAux = iterator.next();
				if (huecoAux.getDir() + huecoAux.getTam() == dir) {

					huecoAux.setTam(huecoAux.getTam() + tam);
					if (pred != null) {
						int tamanyo = pred.getTam();

						if (huecoAux.getDir() + huecoAux.getTam() == pred
								.getDir()) {
							memoria.getHuecos().remove(pred);
							huecoAux.setTam(huecoAux.getTam() + tamanyo);
						}
					}
					return;
				}
			}
			
			iterator = memoria.getHuecos().iterator();
			while (iterator.hasNext()) {
				huecoAux = iterator.next();
				if (huecoAux.getDir() == dir + tam) {
					huecoAux.setDir(dir);
					huecoAux.setTam(huecoAux.getTam() + tam);			
					return;
				}
			}

			memoria.getHuecos().add(new Hueco(dir, tam));

		}
	}
}
