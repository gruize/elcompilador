package interprete.datoPila;

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

	public void setValor(Object valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "Dato [tipo=" + tipo + ", valor=" + valor + "]";
	}

}
