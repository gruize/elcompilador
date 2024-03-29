package es.ucm.plg.interprete;

import java.io.DataOutputStream;
import java.io.IOException;

import es.ucm.plg.interprete.datoPila.DatoPila;

public abstract class InstruccionInterprete {

	public static final byte CODIGO_PARAR = (byte) 0;
	public static final byte CODIGO_APILAR = (byte) 1;
	public static final byte CODIGO_APILARDIR = (byte) 2;
	public static final byte CODIGO_DESAPILAR = (byte) 3;
	public static final byte CODIGO_DESAPILARDIR = (byte) 4;
	public static final byte CODIGO_MENOR = (byte) 5;
	public static final byte CODIGO_MAYOR = (byte) 6;
	public static final byte CODIGO_MENORIG = (byte) 7;
	public static final byte CODIGO_MAYORIG = (byte) 8;
	public static final byte CODIGO_IGUAL = (byte) 9;
	public static final byte CODIGO_DISTINTO = (byte) 10;
	public static final byte CODIGO_SUMA = (byte) 11;
	public static final byte CODIGO_RESTA = (byte) 12;
	public static final byte CODIGO_MULTIPLICA = (byte) 13;
	public static final byte CODIGO_DIVIDE = (byte) 14;
	public static final byte CODIGO_MODULO = (byte) 15;
	public static final byte CODIGO_Y = (byte) 16;
	public static final byte CODIGO_O = (byte) 17;
	public static final byte CODIGO_NEGACION = (byte) 18;
	public static final byte CODIGO_CAMBIO_SIGNO = (byte) 19;
	public static final byte CODIGO_CASTINT = (byte) 22;
	public static final byte CODIGO_CASTREAL = (byte) 25;
	public static final byte CODIGO_ABS = (byte) 26;
	public static final byte CODIGO_SALIDA = (byte) 27;
	public static final byte CODIGO_ENTRADA = (byte) 30;
	public static final byte CODIGO_LIMPIAR = (byte) 31;
	public static final byte CODIGO_IR_F = (byte) 32;
	public static final byte CODIGO_IR_A = (byte) 33;
	public static final byte CODIGO_IR_V = (byte) 34;
	public static final byte CODIGO_IR_IND = (byte) 35;
	public static final byte CODIGO_MUEVE = (byte) 36;
	public static final byte CODIGO_COPIA = (byte) 37;
	public static final byte CODIGO_NEW = (byte) 38;
	public static final byte CODIGO_DELETE = (byte) 39;
	public static final byte CODIGO_APILARIND = (byte) 40;
	public static final byte CODIGO_DESAPILARIND = (byte) 41;

	private DatoPila dato;
	private byte tipoIns;

	public InstruccionInterprete(byte tipoIns) {
		this.tipoIns = tipoIns;
		this.dato = null;
	}

	public InstruccionInterprete(byte tipoIns, DatoPila dato) {
		this.tipoIns = tipoIns;
		this.dato = dato;
	}

	public abstract boolean ejecutate(Interprete interprete) throws InterpreteExcepcion;

	public DatoPila getDato() {
		return dato;
	}

	public byte getTipoIns() {
		return tipoIns;
	}

	public byte getTipoResult(byte tipo1, byte tipo2) {
		if ((tipo1 == DatoPila.INT) && (tipo2 == DatoPila.INT))
			return DatoPila.INT;
		else
			return DatoPila.REAL;

	}
	
    public void escribete(DataOutputStream dos) throws InterpreteExcepcion{
        try {
			dos.writeByte(getTipoIns());
			 if (getDato() != null) {
		            getDato().escribete(dos);
		     }
		} catch (IOException e) {
			throw new InterpreteExcepcion("InstruccionInterprete.escribete", "Error al escribir la instruccion");
		}
       
    }

}
