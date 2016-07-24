package middleware.externalinterfaces;

import middleware.exceptions.MiddlewareException;


public interface CreditVerification {
	public void checkCreditCard(CreditVerificationProfile profile) throws MiddlewareException;
}
