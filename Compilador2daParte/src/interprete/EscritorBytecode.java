package interprete;

import java.io.File;
import java.util.ArrayList;

public interface EscritorBytecode {
    public void escribirPrograma(ArrayList<InstruccionInterprete> programa, File f);
}
