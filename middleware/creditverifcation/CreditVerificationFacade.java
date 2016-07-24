package middleware.creditverifcation;

import middleware.exceptions.MiddlewareException;
import middleware.externalinterfaces.CreditVerification;
import middleware.externalinterfaces.CreditVerificationProfile;
import publicview.IVerificationSystem;
import publicview.VerificationManager;


public class CreditVerificationFacade implements CreditVerification {

	/**
	 * Use of "amount" here is a violation of encapsulation. Should use a
	 * command object to encapsulate all the data.
	 * @param custProfile
	 * @param billingAddress
	 * @param creditCard
	 * @param amount
	 */
	@Override
	public void checkCreditCard(CreditVerificationProfile profile) throws MiddlewareException {
		
		IVerificationSystem verifSystem = VerificationManager.clientInterface();
		CreditVerifMediator mediator = new CreditVerifMediator();
		mediator.processCreditRequest(verifSystem, profile);

	}
	
	public static CreditVerificationProfile getCreditProfileShell() {
		return new CreditVerificationProfileImpl();
	}
	

}
