package analizador_lexico;


import junit.framework.TestCase;

import compilador.analizador_lexico.AnalizadorLexico;

public class AnalizadorLexicoTest extends TestCase {

	String programa = 	"real cantidad; \n" + 
						"int euros; \n" + 
						"real centimos; \n" + 
						"out euros = (int)(in cantidad); \n" + 
						"out centimos = cantidad - euros;";

	String prueba1 = "[DatosToken [token=tkreal, fila=0, columna=4, indice=4], "
			+ "DatosToken [token=tkid, fila=0, columna=13, indice=13], "
			+ "DatosToken [token=tk;, fila=0, columna=14, indice=14], "
			+ "DatosToken [token=tkint, fila=1, columna=3, indice=19], "
			+ "DatosToken [token=tkid, fila=1, columna=9, indice=25], "
			+ "DatosToken [token=tk;, fila=1, columna=10, indice=26], "
			+ "DatosToken [token=tkreal, fila=2, columna=4, indice=32], "
			+ "DatosToken [token=tkid, fila=2, columna=13, indice=41], "
			+ "DatosToken [token=tk;, fila=2, columna=14, indice=42], "
			+ "DatosToken [token=tkout, fila=3, columna=3, indice=47], "
			+ "DatosToken [token=tkid, fila=3, columna=9, indice=53], "
			+ "DatosToken [token=tk=, fila=3, columna=11, indice=55], "
			+ "DatosToken [token=tkcastint, fila=3, columna=17, indice=61], "
			+ "DatosToken [token=tk(, fila=3, columna=18, indice=62], "
			+ "DatosToken [token=tkin, fila=3, columna=20, indice=64], "
			+ "DatosToken [token=tkid, fila=3, columna=29, indice=73], "
			+ "DatosToken [token=tk), fila=3, columna=30, indice=74], "
			+ "DatosToken [token=tk;, fila=3, columna=31, indice=75], "
			+ "DatosToken [token=tkout, fila=4, columna=3, indice=80], "
			+ "DatosToken [token=tkid, fila=4, columna=12, indice=89], "
			+ "DatosToken [token=tk=, fila=4, columna=14, indice=91], "
			+ "DatosToken [token=tkid, fila=4, columna=23, indice=100], "
			+ "DatosToken [token=tk-, fila=4, columna=25, indice=102], "
			+ "DatosToken [token=tkid, fila=4, columna=31, indice=108]]";

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AnalizadorLexicoTest.class);

	}

	public void testPrograma() {

		AnalizadorLexico lexico = new AnalizadorLexico(programa);
		lexico.scanner();

		assertEquals(prueba1, lexico.getTokens().toString());
	}

}
