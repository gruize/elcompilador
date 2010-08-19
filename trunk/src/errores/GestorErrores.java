package errores;

import java.util.Vector;

public class GestorErrores {

	private static Errores errores = null;
	private static Vector<Error> errores_propios = null;
	
	public static void creaGestor() {
		errores = new Errores();
	}
	
	public static Vector<Error> getInstancia(){
		if(errores_propios == null){
			errores_propios = new Vector<Error>();
			creaGestor();
		}
		return errores_propios;
	}
	
	public static void agregaError(int id, int fila, int columna){
		errores_propios.add(new Error(fila,columna,id));
	}
	
	public static void agregaError(int id, int fila, int columna, String descripcion){
		errores_propios.add(new Error(fila,columna,descripcion,id));
	}
	
	public static String escribe(){
		String error = "No hay errores";
		int i = 0;
		int num_error = 1;
		if(errores_propios.size() != 0){
			while(i < errores_propios.size()){
				Error temp = errores_propios.elementAt(i);
				error = error + "[nº " + num_error + "] [ErrorID: " + temp.getId() + " - Descripción: " + temp.getDescripcion() + " - [Fila: " + temp.getFila() + " - Columna: " + temp.getCol() + "]]"+ "\n";
				i++;
				num_error++;
			}
		}
		return error;
	}
}
