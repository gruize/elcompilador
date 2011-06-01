package es.ucm.plg.interprete;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class EscritorPila {

    public void escribirPrograma(ArrayList<InstruccionInterprete> programa, File f) throws InterpreteExcepcion{
        DataOutputStream dos;
		try {
			dos = new DataOutputStream(new FileOutputStream(f));
			for(Iterator<InstruccionInterprete> it = programa.iterator(); it.hasNext();) {
	            (it.next()).escribete(dos);
	        }
		} catch (FileNotFoundException e) {
			throw new InterpreteExcepcion("EscritorPila.escribirPrograma", InterpreteExcepcion.LECTURA_ESCRITURA);
		}
    }

}
