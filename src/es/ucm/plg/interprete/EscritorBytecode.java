package es.ucm.plg.interprete;

import java.io.File;
import java.util.ArrayList;

import es.ucm.plg.interprete.InstruccionInterprete;

/**
 * Esta interfaz deberia ser implementada por la clase que escriba instrucciones
 * de pila en un flujo de bytes
 */
public interface EscritorBytecode {
    public void escribirPrograma(ArrayList<InstruccionInterprete> programa, File f);
}
