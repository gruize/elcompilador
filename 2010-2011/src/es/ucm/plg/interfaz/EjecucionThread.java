package es.ucm.plg.interfaz;

import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import es.ucm.plg.interprete.Interprete;

public class EjecucionThread extends Thread {

	Reader reader;
	Writer writer;
	boolean debug;

	public EjecucionThread(Reader reader, Writer writer, boolean debug) {
		this.reader = reader;
		this.writer = writer;
		this.debug = debug;
	}

	@Override
	public void run() {
		try {
			Interprete interprete = new Interprete(debug, reader,
					new PrintWriter(System.out, true));
			File f2 = new File("/home/naos/test/codigoP.bc");
			interprete.leerPrograma(f2);
			interprete.ejecutarPrograma();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
