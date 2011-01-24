package compilador.test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import compilador.analizador_lexico.AnalizadorLexico;

public class AnalizadorLexicoTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AnalizadorLexicoTest.class);

	}

	public void test1() throws IOException {
				
		BufferedReader br = new BufferedReader(new FileReader(new File("test/test1/code.txt")));
		char [] array = new char[1000];
		
		br.read(array);
		String programa = new String(array);
		
		AnalizadorLexico lexico = new AnalizadorLexico(programa);
		lexico.scanner();
		br = new BufferedReader(new FileReader(new File("test/test1/lexico.txt")));
		String prueba1 = br.readLine();
		assertEquals(prueba1, lexico.getTokens().toString());
	}
	
	public void test2() throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(new File("test/test2/code.txt")));
		char [] array = new char[1000];
		
		br.read(array);
		String programa = new String(array);
		
		AnalizadorLexico lexico = new AnalizadorLexico(programa);
		lexico.scanner();
		br = new BufferedReader(new FileReader(new File("test/test2/lexico.txt")));
		String prueba1 = br.readLine();
		assertEquals(prueba1, lexico.getTokens().toString());
	}
	
	public void test3() throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(new File("test/test3/code.txt")));
		char [] array = new char[1000];
		
		br.read(array);
		String programa = new String(array);
		
		AnalizadorLexico lexico = new AnalizadorLexico(programa);
		lexico.scanner();
		br = new BufferedReader(new FileReader(new File("test/test3/lexico.txt")));
		String prueba1 = br.readLine();
		assertEquals(prueba1, lexico.getTokens().toString());
	}

}
