package interprete.test;

import interprete.EscritorPila;
import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;
import interprete.instrucciones.Apilar;
import interprete.instrucciones.ApilarDir;
import interprete.instrucciones.CambioSigno;
import interprete.instrucciones.CastInt;
import interprete.instrucciones.CastReal;
import interprete.instrucciones.Desapilar;
import interprete.instrucciones.DesapilarDir;
import interprete.instrucciones.Distinto;
import interprete.instrucciones.Dividir;
import interprete.instrucciones.Entrada;
import interprete.instrucciones.Igual;
import interprete.instrucciones.Mayor;
import interprete.instrucciones.MayorIg;
import interprete.instrucciones.Menor;
import interprete.instrucciones.MenorIg;
import interprete.instrucciones.Modulo;
import interprete.instrucciones.Multiplicar;
import interprete.instrucciones.Negacion;
import interprete.instrucciones.O_Logica;
import interprete.instrucciones.Parar;
import interprete.instrucciones.Restar;
import interprete.instrucciones.Salida;
import interprete.instrucciones.Sumar;
import interprete.instrucciones.ValorAbsoluto;
import interprete.instrucciones.Y_Logica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

public class MaquinaPTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MaquinaPTest.class);

	}

	public void test1() throws IOException {
		try {
			escribirPrograma("test/test1/codigoP");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void test2() throws IOException {
		try {
			escribirPrograma("test/test2/codigoP");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void test3() throws IOException {
		try {
			escribirPrograma("test/test3/codigoP");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void escribirPrograma(String filePath) throws Exception {

		ArrayList<InstruccionInterprete> ai = traductorBytecode(new File(
				filePath + ".txt"));

		EscritorPila ep = new EscritorPila();
		File f = new File(filePath + ".bc");
		ep.escribirPrograma(ai, f);

		Interprete interprete = new Interprete(true);

		interprete.leerPrograma(f);
		interprete.ejecutarPrograma();
		System.out.print(interprete.mostrarEstado());
	}

	public ArrayList<InstruccionInterprete> traductorBytecode(File f)
			throws Exception {

		ArrayList<InstruccionInterprete> ai = new ArrayList<InstruccionInterprete>();
		BufferedReader br = new BufferedReader(new FileReader(f));

		String line;

		while ((line = br.readLine()) != null) {
			ai.add(leerInstruccion(line));
		}

		return ai;

	}

	private InstruccionInterprete leerInstruccion(String ins) throws Exception {

		InstruccionInterprete ii = null;
		String[] words = ins.split(" ");

		if (words[0].equals("apila"))
			if (words.length == 1)
				ii = new Apilar();
			else
				ii = new Apilar(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("apiladir"))
			if (words.length == 1)
				ii = new ApilarDir();
			else
				ii = new ApilarDir(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("desapila"))
			if (words.length == 1)
				ii = new Desapilar();
			else
				ii = new Desapilar(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("desapiladir"))
			if (words.length == 1)
				ii = new DesapilarDir();
			else
				ii = new DesapilarDir(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("menor"))
			if (words.length == 1)
				ii = new Menor();
			else
				ii = new Menor(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("menorigual"))
			if (words.length == 1)
				ii = new MenorIg();
			else
				ii = new MenorIg(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("mayor"))
			if (words.length == 1)
				ii = new Mayor();
			else
				ii = new Mayor(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("mayorigual"))
			if (words.length == 1)
				ii = new MayorIg();
			else
				ii = new MayorIg(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("igual"))
			if (words.length == 1)
				ii = new Igual();
			else
				ii = new Igual(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("distinto"))
			if (words.length == 1)
				ii = new Distinto();
			else
				ii = new Distinto(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("suma"))
			if (words.length == 1)
				ii = new Sumar();
			else
				ii = new Sumar(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("resta"))
			if (words.length == 1)
				ii = new Restar();
			else
				ii = new Restar(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("multiplica"))
			if (words.length == 1)
				ii = new Multiplicar();
			else
				ii = new Multiplicar(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("divide"))
			if (words.length == 1)
				ii = new Dividir();
			else
				ii = new Dividir(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("modulo"))
			if (words.length == 1)
				ii = new Modulo();
			else
				ii = new Modulo(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("y"))
			if (words.length == 1)
				ii = new Y_Logica();
			else
				ii = new Y_Logica(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("o"))
			if (words.length == 1)
				ii = new O_Logica();
			else
				ii = new O_Logica(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("negacion"))
			if (words.length == 1)
				ii = new Negacion();
			else
				ii = new Negacion(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("cambio_signo"))
			if (words.length == 1)
				ii = new CambioSigno();
			else
				ii = new CambioSigno(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("castint"))
			if (words.length == 1)
				ii = new CastInt();
			else
				ii = new CastInt(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("castreal"))
			if (words.length == 1)
				ii = new CastReal();
			else
				ii = new CastReal(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("parar"))
			if (words.length == 1)
				ii = new Parar();
			else
				ii = new Parar(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("abs"))
			if (words.length == 1)
				ii = new ValorAbsoluto();
			else
				ii = new ValorAbsoluto(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("out"))
			if (words.length == 1)
				ii = new Salida();
			else
				ii = new Salida(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else if (words[0].equals("in"))
			if (words.length == 1)
				ii = new Entrada();
			else
				ii = new Entrada(new DatoPila(DatoPila.INT,
						Integer.valueOf(words[1])));
		else
			throw new InterpreteException("Instrucción leída no válida");

		return ii;
	}

}
