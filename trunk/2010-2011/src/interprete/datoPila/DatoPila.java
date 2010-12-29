package interprete.datoPila;

import java.io.DataOutputStream;
import java.io.IOException;

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
		return (Integer) valor;
	}

	public Float getReal() {
		return (Float) valor;
	}

	public void setValor(Object valor) {
		this.valor = valor;
	}

    public void escribete(DataOutputStream dos) throws IOException {
        dos.writeByte(tipo);
        if (tipo == DatoPila.INT)
        	dos.writeInt(this.getEntero());
        else
        	dos.writeFloat(this.getReal());
    }
    
	@Override
	public String toString() {
		return "Dato [tipo = " + tipo + ", valor = " + valor + "]";
	}

}
