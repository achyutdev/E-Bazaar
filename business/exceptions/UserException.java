
package business.exceptions;


/**
 * 
 * Thrown when login fails
 */
public class UserException extends BusinessException {
   

	public UserException(String msg){
        super(msg);
    }
	private static final long serialVersionUID = 3690196564010546740L;

}
