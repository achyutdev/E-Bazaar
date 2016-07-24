
package business.exceptions;

/**
 * @author pcorazza
 * @since Nov 12, 2004
 * Class Description: This is the superclass
 * of all Exceptions in E-Bazaar.
 * 
 * 
 */
public class BusinessException extends Exception {
    public BusinessException(String msg){
        super(msg);
    }
    public BusinessException(Exception e){
        super(e);
    }
	private static final long serialVersionUID = 3258144448058015026L;
}
