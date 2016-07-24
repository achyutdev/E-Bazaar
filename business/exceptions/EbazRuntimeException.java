package business.exceptions;

public class EbazRuntimeException extends RuntimeException {
	public EbazRuntimeException(String s) {
		super(s);
	}
	public EbazRuntimeException(Exception e) {
		super(e);
	}
	private static final long serialVersionUID = 2566282094218081403L;
}
