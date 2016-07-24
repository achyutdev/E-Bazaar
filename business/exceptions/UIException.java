package business.exceptions;

/**
 * Thrown in response to a user UI action that prevents the UI from
 * working properly -- such as trying to use a selected value when
 * no value has been selected.
 *
 */
public class UIException extends BusinessException {
	public UIException(String msg) {
        super(msg);
 
    }
    public UIException(Exception e) {
        super(e);
    }
	private static final long serialVersionUID = 3689355398893482807L;
	
}
