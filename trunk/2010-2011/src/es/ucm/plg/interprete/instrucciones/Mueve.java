package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Mueve extends InstruccionInterprete {

	public Mueve() throws InterpreteExcepcion {
		super(InstruccionInterprete.CODIGO_MUEVE);
		throw new InterpreteExcepcion(this.toString(),
				InterpreteExcepcion.FALTA_PARAMETRO);
	}

	public Mueve(DatoPila d) throws InterpreteExcepcion {
		super(InstruccionInterprete.CODIGO_MUEVE, d);
		if (d.getTipo() != DatoPila.INT)
			throw new InterpreteExcepcion(this.toString(),
					InterpreteExcepcion.TIPO_INCORRECTO);
	}

	@Override
	public String toString() {
		return "mueve " + this.getDato().getValor();
	}

	@Override
	public boolean ejecutate(Interprete interprete) throws InterpreteExcepcion {

		boolean ok = false;
		DatoPila origen = interprete.getPila().pop();
		DatoPila destino = interprete.getPila().pop();

		if (origen.getTipo() == DatoPila.INT
				&& destino.getTipo() == DatoPila.INT) {
			if (origen.getEntero() >= 0
					&& origen.getEntero() < interprete.getMemoria()
							.getMemoria().length) {
				if (destino.getEntero() >= 0
						&& destino.getEntero() < interprete.getMemoria()
								.getMemoria().length) {
					if (this.getDato().getTipo() == DatoPila.INT
							&& this.getDato().getEntero() > 0) {
						if ((origen.getEntero() + this.getDato().getEntero()) < interprete
								.getMemoria().getMemoria().length
								|| (destino.getEntero() + this.getDato()
										.getEntero()) < interprete.getMemoria()
										.getMemoria().length) {
							for (int i = 0; i < this.getDato().getEntero(); i++)
								interprete.getMemoria().getMemoria()[destino
										.getEntero() + i] = interprete
										.getMemoria().getMemoria()[origen
										.getEntero() + i];
							ok = true;
						} else

							throw new InterpreteExcepcion(this.toString(),
									"Movimiento imposible. Valor de memoria inexistente.");
					} else

						throw new InterpreteExcepcion(this.toString(),
								"El tamaño identificado no es valido. Valor de memoria inexistente.");
				} else

					throw new InterpreteExcepcion(this.toString(),
							"Las direccion de destino no es valida. Valor de memoria inexistente.");
			} else

				throw new InterpreteExcepcion(this.toString(),
						"Las direccion de origen no es valida. Valor de memoria inexistente.");
		} else if (origen.getTipo() != DatoPila.INT)

			throw new InterpreteExcepcion(this.toString(),
					"Las direccion de origen debe ser un valor entero.");
		else
			throw new InterpreteExcepcion(this.toString(),
					"Las direccion de destino debe ser un valor entero.");
		return ok;
	}

}
