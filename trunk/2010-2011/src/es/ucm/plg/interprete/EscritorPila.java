package es.ucm.plg.interprete;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import es.ucm.plg.compilador.gestorErrores.GestorErrores;
import es.ucm.plg.interprete.InstruccionInterprete;

public class EscritorPila {

    public void escribirPrograma(ArrayList<InstruccionInterprete> programa, File f){
        DataOutputStream dos;
		try {
			dos = new DataOutputStream(new FileOutputStream(f));
			for(Iterator<InstruccionInterprete> it = programa.iterator(); it.hasNext();) {
	            (it.next()).escribete(dos);
	        }
		} catch (FileNotFoundException e) {
			GestorErrores.agregaError(20, 0, 0, "No se ha encontrado el archivo" + f.getName());
		}
    }

}
