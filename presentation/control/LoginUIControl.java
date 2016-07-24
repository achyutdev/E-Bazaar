package presentation.control;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import presentation.data.LoginData;
import presentation.gui.LoginWindow;
import business.Login;
import business.SessionCache;
import business.exceptions.BackendException;
import business.exceptions.UserException;
import business.BusinessConstants;

public class LoginUIControl {


	private static final Logger LOG =
		Logger.getLogger(LoginUIControl.class.getName());

	//private SessionContext context;
    private LoginWindow loginWindow;
    private Callback controller;
    private LoginData loginData = new LoginData();


    Stage targetWindow;
    Stage departingFromWindow;

    //If parent and current windows are same, we don't make one vanish
    //while the other is displayed.
    private boolean targetDepartFromAreSame;

    public LoginUIControl(Stage target, Stage departingFrom){
        this.targetWindow = target;
        this.departingFromWindow = departingFrom;
        targetDepartFromAreSame = (target == departingFrom);
    }
    public LoginUIControl(Stage target, Stage departingFrom, Callback controller){
        this(target, departingFrom);
        this.controller=controller;
    }

    public void startLogin() {
        loginWindow = new LoginWindow(this);
        departingFromWindow.hide();
        loginWindow.show();
    }

    //////// event handling code

    public ShowLoginHandler getShowLoginHandler() {
    	return new ShowLoginHandler();
    }

    public SubmitHandler getSubmitHandler(LoginWindow w) {
        return new SubmitHandler();
    }
    public CancelHandler getCancelHandler(LoginWindow w) {
        return new CancelHandler();
    }

    class LogoutHandler implements EventHandler<ActionEvent> {
    	public void handle(ActionEvent evt) {
    		SessionCache cache = SessionCache.getInstance();
    		boolean loggedIn = (Boolean)cache.get(BusinessConstants.LOGGED_IN);

    		if(!loggedIn) {
    			controller.displayInfo("You are already logged out!");
    		} else {
    			cache.add(BusinessConstants.LOGGED_IN, Boolean.FALSE);
    			cache.remove(BusinessConstants.CUSTOMER);
    			controller.displayInfo("You have successfully logged out.");
    		}
    	}
    }

    public LogoutHandler getLogoutHandler() {
    	return new LogoutHandler();
    }

    class ShowLoginHandler implements EventHandler<ActionEvent> {
    	public void handle(ActionEvent evt) {
    		if((Boolean)(SessionCache.getInstance().get(BusinessConstants.LOGGED_IN))) {
    			controller.displayInfo("You are already logged in!");
    		} else {
	    		departingFromWindow.hide();
	    		startLogin();
    		}
    	}
    }

	class SubmitHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent evt) {
            loginWindow.hide();
            Login login = loginData.extractLogin(loginWindow);
            LOG.config("Login found: " + login.getCustId());
            boolean loginSuccessful = false;
            try {
            	int authorizationLevel = loginData.authenticate(login);
            	loginSuccessful = true;
            	System.out.println("loginuicontrol:now load customer...");
            	loginData.loadCustomer(login, authorizationLevel);
            } catch(UserException e) {
            	loginWindow.displayError(e.getMessage());
            	loginWindow.show();
            } catch(BackendException e) {
            	loginWindow.displayError(e.getMessage());
            	loginWindow.show();
            }
            if(loginSuccessful) {
	            loginWindow.hide();
	       	    if(controller != null) {
	       	    	if(targetDepartFromAreSame) {
	       	    		controller.displayInfo("Login Successful!");
	       	    		targetWindow.show();
	       	    	}
       	    		else {
       	    			controller.doUpdate();
       	    		}
	       	    }
	       	    else {
	       	    	targetWindow.show();
	       	    }
            }
        }
	}
	class CancelHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent evt) {

        	if(departingFromWindow != null) {
        		departingFromWindow.show();
        	}
        	loginWindow.hide();

        }
	}
}
