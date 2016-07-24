package business.exceptions;

/**
 * This exception is thrown in response to a backend
 * exception -- such as failure to access database.
 * 
 * Proper usage: When any kind of backend exception
 * is thrown and propagates to the business layer,
 * it should be caught and rethrown as a BackendException
 * @author Administrator
 *
 */
public class BackendException extends BusinessException {
	   public BackendException(String msg){
	        super(msg);
	    }
	    public BackendException(Exception e){
	        super(e);
	    }
		private static final long serialVersionUID = 3258144448058015026L;
}
