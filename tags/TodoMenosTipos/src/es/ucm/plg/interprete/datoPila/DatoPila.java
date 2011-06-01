package es.ucm.plg.interprete.datoPila;

import java.io.DataOutputStream;
import java.io.IOException;

import es.ucm.plg.interprete.InterpreteExcepcion;

public class DatoPila {

	public static final byte INT = 0;
	public static final byte REAL = 1;

	private byte tipo;
	private Object valor;

	public DatoPila(byte tipo, Object valor) {
		this.tipo = tipo;
		this.valor = valor;
	}

	public byte getTipo() {
		return tipo;
	}

	public void setTipo(byte tipo) {
		this.tipo = tipo;
	}

	public Object getValor() {
		return valor;
	}

	public Integer getEntero() {
		try {
			return Integer.valueOf(valor.toString());
		} catch (Exception ex) {
			return Float.valueOf(valor.toString()).intValue();
		}
	}

	public Float getReal() {
		return Float.valueOf(valor.toString());
	}

	public void setValor(Object valor) {
		this.valor = valor;
	}

	public void escribete(DataOutputStream dos) throws InterpreteExcepcion {
		try {
			dos.writeByte(tipo);
			if (tipo == DatoPila.INT)
				dos.writeInt(this.getEntero());
			else
				dos.writeFloat(this.getReal());
		} catch (IOException e) {
			throw new InterpreteExcepcion("DatoPila.escribete", InterpreteExcepcion.LECTURA_ESCRITURA);
		}
	}

	@Override
	public String toString() {
		return "Dato [tipo = " + tipo + ", valor = " + valor + "]";
	}

}
