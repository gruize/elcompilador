package es.ucm.plg.compilador.analizadorSintactico;

import es.ucm.plg.compilador.analizadorLexico.PalabrasReservadas;
import es.ucm.plg.compilador.tablaSimbolos.tipos.Tipo;
import es.ucm.plg.compilador.tablaSimbolos.tipos.TipoEntero;
import es.ucm.plg.interprete.InterpreteExcepcion;
import es.ucm.plg.interprete.datoPila.DatoPila;
import es.ucm.plg.interprete.instrucciones.IrA;
import es.ucm.plg.interprete.instrucciones.IrF;
import es.ucm.plg.interprete.instrucciones.LimpiarPila;

public class Acciones {

	private AnalizadorSintactico sintactico;

	public Acciones(AnalizadorSintactico sintactico) {
		this.sintactico = sintactico;
	}

	public void acciones() throws Exception {

		try {
			accion();
			accionesRE();

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	private void accionesRE() throws Exception {
		
		try {
			sintactico.getCodigo().add(new LimpiarPila());
			if (!sintactico.getLexico().isFin_programa()) {
				accion();
				accionesRE();
			}
			
		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	private boolean accion() throws Exception {

		try {
			if (!accionAlternativa() && !accionIteracion() && !accionReserva()
					&& !accionLibera()
					&& !(sintactico.getExpresiones().expresion() != null)) {
				throw new MiExcepcion("Se esperaba una accion");
			}

			if (!sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA))
				throw new MiExcepcion(SintacticoException.FALTA_PUNTO_COMA);

			return true;

		} catch (MiExcepcion ex) {
			throw new SintacticoException(ex.getMessage(), sintactico
					.getLexico().getLexema(), sintactico.getLexico().getFila(),
					sintactico.getLexico().getColumna());
		}
	}

	private boolean accionLibera() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean accionReserva() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean accionAlternativa() throws Exception {
		boolean ok = false;
		if (sintactico.reconoce(PalabrasReservadas.TOKEN_IF)) {
			Tipo tipo = sintactico.getExpresiones().expresion2();
			if (tipo instanceof TipoEntero && sintactico.reconoce(PalabrasReservadas.TOKEN_THEN)) {
				sintactico.getCodigo().add(new IrF(null));
				int irfalseAux = sintactico.getCodigo().size() - 1;
                sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
                ok = bloque(PalabrasReservadas.TOKEN_IF);
                sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
                sintactico.getCodigo().set(irfalseAux, new IrF(new DatoPila(DatoPila.INT, sintactico.getEtiqueta())));
                sintactico.getCodigo().add(new IrA(null));
                int irAAux = sintactico.getCodigo().size() - 1;
                ok = accionelse();                                      
                sintactico.getCodigo().set(irAAux, new IrA(new DatoPila(DatoPila.INT, sintactico.getEtiqueta())));
                if(!sintactico.reconoce(PalabrasReservadas.TOKEN_END_IF))
                	throw new MiExcepcion("Alternativa sin finalizar. Se esperaba la palabra EndIf");                
                if(sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA))
                        ok = true;
			}else
				throw new MiExcepcion("Se esperaba un entero con valor 1 o 0 y la palabra Then");
        }							
		return ok;
	}

	private boolean accionelse() throws Exception {
		boolean ok = false;
	    if(sintactico.getLexico().getToken_actual().equals(PalabrasReservadas.TOKEN_ELSIF)){
	    	sintactico.reconoce(PalabrasReservadas.TOKEN_ELSIF);
            Tipo tipo = sintactico.getExpresiones().expresion2();
            if(tipo instanceof TipoEntero && sintactico.reconoce(PalabrasReservadas.TOKEN_THEN)){
                sintactico.getCodigo().add(new IrF(null));
                int irfalseAux = sintactico.getCodigo().size() - 1;
                sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
                ok = bloque(PalabrasReservadas.TOKEN_ELSIF);
                sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
                sintactico.getCodigo().set(irfalseAux, new IrF(new DatoPila(DatoPila.INT, sintactico.getEtiqueta())));
                sintactico.getCodigo().add(new IrA(null));
                int irAAux = sintactico.getCodigo().size() - 1;
                ok = accionelse();                                      
                sintactico.getCodigo().set(irAAux, new IrA(new DatoPila(DatoPila.INT, sintactico.getEtiqueta())));
                if(sintactico.getLexico().getToken_actual().equals(PalabrasReservadas.TOKEN_END_IF))
                	ok = true;
            }else
            	throw new MiExcepcion("Se esperaba un entero con valor 1 o 0 y la palabra Then");            
	    }
        return ok;
	}

	private boolean bloque(String tokenFinaliza) throws Exception {
        boolean ok = false;     
        if(tokenFinaliza.equals(PalabrasReservadas.TOKEN_IF) || tokenFinaliza.equals(PalabrasReservadas.TOKEN_ELSIF)){
            while(!sintactico.getLexico().getToken_actual().equals(PalabrasReservadas.TOKEN_ELSIF)
            		&& !sintactico.getLexico().getToken_actual().equals(PalabrasReservadas.TOKEN_END_IF) 
            		&& !sintactico.getLexico().isFin_programa())
                    ok = accion();         
        }else{
            if(tokenFinaliza.equals(PalabrasReservadas.TOKEN_WHILE)){
                while(!sintactico.getLexico().isFin_programa() && 
                        !sintactico.getLexico().getToken_actual().equals(PalabrasReservadas.TOKEN_END_WHILE))
                	ok = accion();
            }//else{ Resto de token para los que se requieren bloques
        }   
        return ok;
	}

	private boolean accionIteracion() throws Exception {
		boolean ok = false;        
	    if(sintactico.reconoce(PalabrasReservadas.TOKEN_WHILE)){
            int whileAux = sintactico.getEtiqueta();
            Tipo tipo = sintactico.getExpresiones().expresion2();
            if(tipo instanceof TipoEntero && sintactico.reconoce(PalabrasReservadas.TOKEN_DO)){                                
                sintactico.getCodigo().add(new IrF(null));
                int irFalseAux = sintactico.getCodigo().size() - 1;
                sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
                ok = bloque(PalabrasReservadas.TOKEN_WHILE);
                sintactico.setEtiqueta(sintactico.getEtiqueta() + 1);
                sintactico.getCodigo().set(irFalseAux, new IrF(new DatoPila(DatoPila.INT, sintactico.getEtiqueta())));
                sintactico.getCodigo().add(new IrA(new DatoPila(DatoPila.INT, whileAux)));
                if(!sintactico.reconoce(PalabrasReservadas.TOKEN_END_WHILE))
                	throw new MiExcepcion("Bucle infinito. Se esperaba la palabra EndWhile");                                                      
                if(sintactico.reconoce(PalabrasReservadas.TOKEN_PUNTO_COMA))
                    ok = true;
            }else
            	throw new MiExcepcion("Se esperaba una expresion entera y la palabra DO");
	    }
        return ok;
	}

	@SuppressWarnings("serial")
	private class MiExcepcion extends Exception {

		public MiExcepcion(String mensaje) {
			super(mensaje);
		}

	}

}
