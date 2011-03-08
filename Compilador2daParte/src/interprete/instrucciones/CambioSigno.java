package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.datoPila.DatoPila;
import compilador.gestorErrores.GestorErrores;

public class CambioSigno extends InstruccionInterprete {

	public CambioSigno(){
		super(InstruccionInterprete.CODIGO_CAMBIO_SIGNO);
	}

	public CambioSigno(DatoPila d){
		super(InstruccionInterprete.CODIGO_CAMBIO_SIGNO);
		GestorErrores.agregaError("La instruccion no acepta argumentos");
	}

	@Override
	public String toString() {
		return "cambio_signo";
	}
	
	@Override
	public boolean ejecutate(Interprete interprete) {
		DatoPila d1 = interprete.getPila().pop();
		DatoPila d2 = new DatoPila(DatoPila.INT, 0);
		Byte tipoRes = getTipoResult(d1.getTipo(), d2.getTipo());
		DatoPila resultado;
		if (tipoRes == DatoPila.INT)
			resultado = new DatoPila(DatoPila.INT,
					(Integer) (d2.getEntero() - d1.getEntero()));
		else
			resultado = new DatoPila(DatoPila.REAL,
					(Float) (d2.getReal() - d1.getReal()));
		interprete.getPila().push(resultado);
		return true;
	}

}
