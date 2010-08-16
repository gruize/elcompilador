package util;

public class instruccionesMaquinaP {

	private operacionesMaquinaP op;
	private Object arg;
	
	public instruccionesMaquinaP(operacionesMaquinaP op) {
		this.op = op;
	}
	public instruccionesMaquinaP(operacionesMaquinaP op, Object arg) {
		this.op = op;
		this.arg = arg;
	}
	public operacionesMaquinaP getOp() {
		return op;
	}
	public void setOp(operacionesMaquinaP op) {
		this.op = op;
	}
	public Object getArg() {
		return arg;
	}
	public void setArg(Object arg) {
		this.arg = arg;
	}
	
	@Override
	public String toString() {
		return "instruccionesMaquinaP [op=" + op + ", arg=" + arg + "]";
	}
	
}
