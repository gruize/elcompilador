package analizadorLexico;

import java.util.Hashtable;

public class PalabrasReservadas {

	private Hashtable<String,String> palabrasReservadas = new Hashtable<String,String>();

	public PalabrasReservadas() {
		palabrasReservadas.put("boolean","tkboolean");
		palabrasReservadas.put("true","tktrue");
		palabrasReservadas.put("false","tkfalse");
		palabrasReservadas.put("character","tkcharacter");
		palabrasReservadas.put("natural","tknatural");
		palabrasReservadas.put("integer","tkinteger");
		palabrasReservadas.put("float","tkfloat");
		palabrasReservadas.put("and","tkand");
		palabrasReservadas.put("or","tkor");
		palabrasReservadas.put("not","tknot");
		palabrasReservadas.put("in","tkin");
		palabrasReservadas.put("out","tkout");
		palabrasReservadas.put("if","tkif");
		palabrasReservadas.put("then","tkthen");
		palabrasReservadas.put("else","tkelse");
		palabrasReservadas.put("while","tkwhile");
		palabrasReservadas.put("do","tkdo");
		palabrasReservadas.put("for","tkfor");
		palabrasReservadas.put("to","tkto");
		palabrasReservadas.put("tipo","tktipo");
		palabrasReservadas.put("array","tkarray");
		palabrasReservadas.put("of","tkof");
		palabrasReservadas.put("record","tkrecord");
		palabrasReservadas.put("pointer","tkpointer");
		palabrasReservadas.put("null","tknull");
		palabrasReservadas.put("new","tknew");
		palabrasReservadas.put("dispose","tkdispose");
		palabrasReservadas.put("procedure","tkprocedure");
		palabrasReservadas.put("var","tkvar");
		palabrasReservadas.put("forward","tkforward");
	}
	
	public String obtenerToken(String palabra){
		String token = "tkid";
		if(token(palabra))
			token = palabrasReservadas.get(palabra);
		return token;
	}

	public Hashtable<String, String> getPalabrasReservadas() {
		return palabrasReservadas;
	}

	public void setPalabrasReservadas(Hashtable<String, String> palabrasReservadas) {
		this.palabrasReservadas = palabrasReservadas;
	}
	
	public boolean token(String palabra){
		boolean existe = false;
		if(palabrasReservadas.containsKey(palabra))
			existe = true;
		return existe;
	}
}
