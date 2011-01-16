package compilador.gestorErrores;

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
		Error temp = new Error(fila,columna,id);
		getInstancia().add(temp);
	}
	
	public static Errores getErrores() {
		return errores;
	}

	public static void setErrores(Errores errores) {
		GestorErrores.errores = errores;
	}

	public static Vector<Error> getErrores_propios() {
		return errores_propios;
	}

	public static void setErrores_propios(Vector<Error> errores_propios) {
		GestorErrores.errores_propios = errores_propios;
	}

	public static String getDespcripcionError(int error){
		String temp = "";
		if(getErrores().getErrores().containsKey(new Integer(error)));
			temp = getErrores().getErrores().get(new Integer(error));
		return temp;
	}
	
	public static void agregaError(int id, int fila, int columna, String descripcion){
		Error temp = new Error(fila,columna,descripcion,id);
		getInstancia().add(temp);
	}
	
	public static String escribe(){
		String error = "No hay errores";
		int i = 0;
		int num_error = 1;
		if(getErrores_propios() != null && !getErrores_propios().isEmpty()){
			error = "";
			while(i < errores_propios.size()){
				Error temp = errores_propios.elementAt(i);
				error = error + "[nº " + num_error + "] [ErrorID: " + temp.getId() + " - Descripción: " + getDespcripcionError(temp.getId()) + " - Motivo: " + temp.getDescripcion() + " - [Fila: " + temp.getFila() + " - Columna: " + temp.getColumna() + "]]"+ "\n";
				i++;
				num_error++;
			}
		}
		return error;
	}
}
