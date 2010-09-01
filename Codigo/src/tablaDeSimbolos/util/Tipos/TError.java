package tablaDeSimbolos.util.Tipos;

public class TError extends Tipo{

	public TError() {
		tipo = TipoDec.TipoError;
	}

	@Override
	public String toString() {
		return "TError [tipo=" + tipo + "]";
	}

}
