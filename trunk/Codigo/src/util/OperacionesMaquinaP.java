package util;

public enum OperacionesMaquinaP {
	MENOR,
	MAYOR,
	MENOR_IGUAL,
	MAYOR_IGUAL,
	IGUAL,
	DISTINTO,
	SUMA,
	RESTA,
	MULT,
	DIVISION,
	MODULO,
	Y_LOGICA,
	O_LOGICA,
	NEG_LOGICA,
	SIGNO_MENOS,
	DESP_IZQ,
	DESP_DER,
	CAST_NAT,
	CAST_INT,
	CAST_CHAR,
	CAST_FLOAT,
	//Apilar el valor
	APILA,
	//Desapilar la cima de la pila
	DESAPILA,
	//Apila el contenido de la posicion de memoria dada por el parametro
	APILA_DIR,
	//Desapila un valor y almacenarlo en la posicion de memoria dada por el parametro
	DESAPILA_DIR,
	//Se desapila la cima, y se apila el contenido de la posicion de memoria dada
	//por el valor de la cima desapilada
	APILA_IND,
	//Se desapila la cima y la subcima, y se guarda en la posicion de memoria dada
	//por la subcima, el valor de la cima 
	DESAPILA_IND,
	LEER,
	ESCRIBIR,
	// Salto incondicional a la etiqueta pasada por parametro
	IR_A,
	//Desapila la cima de la pila. Si es 0, ir a la etiqueta dada por el parametro, si no
	//ejecucion de la siguiente instruccion
	IR_F,
	//Desapila la cima de la pila. Si NO es 0, ir a la etiqueta dada por el parametro, si no
	//ejecucion de la siguiente instruccion
	IR_V,
	IR_IND,
	MUEVE,
	//Duplica la cima de la pila
	COPIA,
	NEW,
	DEL,
	STOP
}
