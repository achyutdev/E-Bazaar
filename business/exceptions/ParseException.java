
package business.exceptions;

/**
 * Thrown when a text file is not read properly (when
 * a message to the user is read dynamically from a text file)
 *
 */
public class ParseException extends BusinessException {
	public ParseException(String msg) {
        super(msg);
 
    }
    public ParseException(Exception e) {
        super(e);
    }
	private static final long serialVersionUID = 3689355398893482807L;
	
}
