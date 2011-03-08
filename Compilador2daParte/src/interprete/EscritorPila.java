package interprete;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import compilador.gestorErrores.GestorErrores;

public class EscritorPila {

    public void escribirPrograma(ArrayList<InstruccionInterprete> programa, File f) throws IOException{
        DataOutputStream dos;
		try {
			dos = new DataOutputStream(new FileOutputStream(f));		
	        for(Iterator<InstruccionInterprete> it = programa.iterator(); it.hasNext();)
				(it.next()).escribete(dos);
		} catch (FileNotFoundException e) {
			GestorErrores.agregaError("No se ha podido encontrar el archivo " + f.getPath() + f.getName());
		}
    }

}
