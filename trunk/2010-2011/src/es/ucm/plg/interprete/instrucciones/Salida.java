package es.ucm.plg.interprete.instrucciones;

import es.ucm.plg.interprete.InstruccionInterprete;
import es.ucm.plg.interprete.Interprete;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;

public class Salida extends InstruccionInterprete{
    public byte tipo;

    public Salida(){
        super(InstruccionInterprete.CODIGO_SALIDA);
    }

    public Salida(DatoPila d) throws InterpreteExcepcion{
        super(InstruccionInterprete.CODIGO_SALIDA, d);
		throw new InterpreteExcepcion(this.toString(), InterpreteExcepcion.SOBRA_PARAMETRO);
    }
    
    @Override
    public String toString() {
        return " out ";
    }

    @Override
    public boolean ejecutate(Interprete interprete){
        	
        	DatoPila d1 = interprete.getPila().pop();
        	
        	if (d1.getTipo() == DatoPila.INT)
        		interprete.getWriter().println(d1.getEntero());
        	else
        		interprete.getWriter().println(d1.getReal());

        	interprete.getPila().push(d1);
        	
            return true;

    }



}
