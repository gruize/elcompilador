package util;

public class InstruccionesMaquinaP {

	private OperacionesMaquinaP op;
	private Object arg;
	
	public InstruccionesMaquinaP(OperacionesMaquinaP op) {
		this.op = op;
	}
	public InstruccionesMaquinaP(OperacionesMaquinaP op, Object arg) {
		this.op = op;
		this.arg = arg;
	}
	public OperacionesMaquinaP getOp() {
		return op;
	}
	public void setOp(OperacionesMaquinaP op) {
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
