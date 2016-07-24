package business.exceptions;

/**
 * Thrown when user attempts to access a screen with inadequate authorization
 *
 */
public class UnauthorizedException extends BusinessException {
	public UnauthorizedException(String msg) {
        super(msg);
 
    }
    public UnauthorizedException(Exception e) {
        super(e);
    }
	private static final long serialVersionUID = 3689355398893482807L;
	
}
