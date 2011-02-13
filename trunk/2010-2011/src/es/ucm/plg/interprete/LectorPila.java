package es.ucm.plg.interprete;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.datoPila.DatoPila;
import es.ucm.plg.interprete.instrucciones.Apilar;
import es.ucm.plg.interprete.instrucciones.ApilarDir;
import es.ucm.plg.interprete.instrucciones.CambioSigno;
import es.ucm.plg.interprete.instrucciones.CastInt;
import es.ucm.plg.interprete.instrucciones.CastReal;
import es.ucm.plg.interprete.instrucciones.Desapilar;
import es.ucm.plg.interprete.instrucciones.DesapilarDir;
import es.ucm.plg.interprete.instrucciones.Distinto;
import es.ucm.plg.interprete.instrucciones.Dividir;
import es.ucm.plg.interprete.instrucciones.Entrada;
import es.ucm.plg.interprete.instrucciones.Igual;
import es.ucm.plg.interprete.instrucciones.Mayor;
import es.ucm.plg.interprete.instrucciones.MayorIg;
import es.ucm.plg.interprete.instrucciones.Menor;
import es.ucm.plg.interprete.instrucciones.MenorIg;
import es.ucm.plg.interprete.instrucciones.Modulo;
import es.ucm.plg.interprete.instrucciones.Multiplicar;
import es.ucm.plg.interprete.instrucciones.Negacion;
import es.ucm.plg.interprete.instrucciones.O_Logica;
import es.ucm.plg.interprete.instrucciones.Parar;
import es.ucm.plg.interprete.instrucciones.Restar;
import es.ucm.plg.interprete.instrucciones.Salida;
import es.ucm.plg.interprete.instrucciones.Sumar;
import es.ucm.plg.interprete.instrucciones.ValorAbsoluto;
import es.ucm.plg.interprete.instrucciones.Y_Logica;

public class LectorPila {

	/**
	 * Este método lee un dato de un DataInputStream
	 * 
	 * @param dis
	 * @return el dato leido
	 * @throws InterpreteException
	 * @throws LectorExc
	 *             en el caso de que ocurra algún error al leer el dato
	 */
	private DatoPila leerDato(DataInputStream dis) {
		try {
			byte tipo = dis.readByte();

			switch (tipo) {
			case DatoPila.INT:
				return new DatoPila(DatoPila.INT, dis.readInt());
			case DatoPila.REAL:
				return new DatoPila(DatoPila.REAL, dis.readFloat());
			default:
				GestorErrores.agregaError("Tipo de dato inválido: "
						+ Byte.toString(tipo));
			}
		} catch (IOException e) {
			GestorErrores.agregaError(e.getMessage());
			return null;
		}
		return null;

	}

	/**
	 * Este método lee una instrucción de un DataInputStream
	 * 
	 * @param dis
	 * @return la InstruccionInterprete leida
	 * @throws IOException
	 *             En el caso de que ocurra un error del stream
	 * @throws InterpreteException
	 * @throws LectorExc
	 *             En el caso de que ocurra un error de formato del programa
	 *             fuente (por ejemplo, apilar sin argumento)
	 */
	private InstruccionInterprete leerInstruccion(DataInputStream dis)
			throws IOException {
		byte tipoIns = dis.readByte();
		InstruccionInterprete inst;
		switch (tipoIns) {
		default:
			throw new IOException("Instrucción inválida");
		case InstruccionInterprete.CODIGO_APILAR:
			inst = new Apilar(leerDato(dis));
			break;
		case InstruccionInterprete.CODIGO_APILARDIR:
			inst = new ApilarDir(leerDato(dis));
			break;
		case InstruccionInterprete.CODIGO_DESAPILAR:
			inst = new Desapilar();
			break;
		case InstruccionInterprete.CODIGO_DESAPILARDIR:
			inst = new DesapilarDir(leerDato(dis));
			break;
		case InstruccionInterprete.CODIGO_MENOR:
			inst = new Menor();
			break;
		case InstruccionInterprete.CODIGO_MAYOR:
			inst = new Mayor();
			break;
		case InstruccionInterprete.CODIGO_MENORIG:
			inst = new MenorIg();
			break;
		case InstruccionInterprete.CODIGO_MAYORIG:
			inst = new MayorIg();
			break;
		case InstruccionInterprete.CODIGO_IGUAL:
			inst = new Igual();
			break;
		case InstruccionInterprete.CODIGO_DISTINTO:
			inst = new Distinto();
			break;
		case InstruccionInterprete.CODIGO_SUMA:
			inst = new Sumar();
			break;
		case InstruccionInterprete.CODIGO_RESTA:
			inst = new Restar();
			break;
		case InstruccionInterprete.CODIGO_MULTIPLICA:
			inst = new Multiplicar();
			break;
		case InstruccionInterprete.CODIGO_DIVIDE:
			inst = new Dividir();
			break;
		case InstruccionInterprete.CODIGO_MODULO:
			inst = new Modulo();
			break;
		case InstruccionInterprete.CODIGO_Y:
			inst = new Y_Logica();
			break;
		case InstruccionInterprete.CODIGO_O:
			inst = new O_Logica();
			break;
		case InstruccionInterprete.CODIGO_NEGACION:
			inst = new Negacion();
			break;
		case InstruccionInterprete.CODIGO_CAMBIO_SIGNO:
			inst = new CambioSigno();
			break;
		case InstruccionInterprete.CODIGO_CASTINT:
			inst = new CastInt();
			break;
		case InstruccionInterprete.CODIGO_CASTREAL:
			inst = new CastReal();
			break;
		case InstruccionInterprete.CODIGO_PARAR:
			inst = new Parar();
			break;
		case InstruccionInterprete.CODIGO_ABS:
			inst = new ValorAbsoluto();
			break;
		case InstruccionInterprete.CODIGO_SALIDA:
			inst = new Salida();
			break;
		case InstruccionInterprete.CODIGO_ENTRADA:
			inst = new Entrada();
			break;
		}
		return inst;
	}

	public ArrayList<InstruccionInterprete> leerPrograma(File f)
			throws FileNotFoundException, IOException {

		ArrayList<InstruccionInterprete> ad = new ArrayList<InstruccionInterprete>();
		DataInputStream dis = new DataInputStream(new FileInputStream(f));

		while (dis.available() > 0) {
			ad.add(leerInstruccion(dis));
		}

		return ad;
	}

}
