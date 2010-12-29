/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interprete.instrucciones;

import interprete.InstruccionInterprete;
import interprete.Interprete;
import interprete.InterpreteException;
import interprete.datoPila.DatoPila;

public class Salida extends InstruccionInterprete{
    public byte tipo;

    public Salida() throws InterpreteException {
        super(InstruccionInterprete.CODIGO_SALIDA);
    }

    public Salida(DatoPila d) throws InterpreteException {
        super(InstruccionInterprete.CODIGO_SALIDA, d);
        throw new InterpreteException("La instrucción no acepta argumentos");
    }
    
    @Override
    public String toString() {
        return " out ";
    }

    @Override
    public boolean ejecutate(Interprete interprete) throws InterpreteException {
        	
        	DatoPila d1 = interprete.getPila().pop();
        	
        	if (d1.getTipo() == DatoPila.INT)
        		interprete.getWriter().println(d1.getEntero());
        	else
        		interprete.getWriter().println(d1.getReal());

            return true;

    }



}
