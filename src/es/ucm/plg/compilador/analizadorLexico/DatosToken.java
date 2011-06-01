package es.ucm.plg.compilador.analizadorLexico;

public class DatosToken {
	
	private String token = "";
	private Integer fila = 0;
	private Integer columna = 0;
	private Integer indice = 0;
	
	/**
	 * Crea un token
	 * @param token Identificador del token
	 * @param fila Fila en la que se encuentra el token dentro del programa
	 * @param columna Columna en la que se encuentra el token dentro del programa
	 * @param indice Posición global en la que se encuentra el token dentro del programa
	 */
	public DatosToken(String token, Integer fila, Integer columna, Integer indice) 
	{
		this.token = token;
		this.fila = fila;
		this.columna = columna;
		this.indice = indice;
	}
	
	
	/**
	 * Crea un token
	 * @param token Identificador del token
	 * @param fila Fila en la que se encuentra el token dentro del programa
	 * @param columna Columna en la que se encuentra el token dentro del programa
	 */
	public DatosToken(String token, Integer fila, Integer columna) 
	{
		new DatosToken(token, fila, columna, 0);
	}
	
	/**
	 * Crea un token
	 * @param token Identificador del token
	 * @param indice Posicion global en la que se encuentra el token dentro del programa
	 */
	public DatosToken(String token, Integer indice) 
	{
		new DatosToken(token, 0, 0, indice);
	}

	/**
	 * @return El identificador del token
	 */
	public String getToken() {
		return token;
	}


	/**
	 * @return La fila del token
	 */
	public Integer getFila() {
		return fila;
	}


	/**
	 * @return La columna del token
	 */
	public Integer getColumna() {
		return columna;
	}


	/**
	 * @return El indice global en el programa
	 */
	public Integer getIndice() {
		return indice;
	}

	@Override
	public String toString() 
	{
		return "DatosToken [token=" + token + ", fila=" + fila + ", columna="
				+ columna + ", indice=" + indice + "]";
	}
	
}
