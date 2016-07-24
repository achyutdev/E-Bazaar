package middleware.creditverifcation;

import java.util.logging.Logger;

import middleware.exceptions.MiddlewareException;
import middleware.externalinterfaces.CreditVerificationProfile;
import publicview.IVerificationSystem;
import publicview.TransactionFailedException;


class CreditVerifMediator {
	//for testing only; should disable in production
	private static int numFailures = 0;
	private static final Logger LOG = Logger.getLogger(CreditVerifMediator.class.getName());
	void processCreditRequest(IVerificationSystem v, CreditVerificationProfile profile)
				throws MiddlewareException {
		v.setBillingAddress(profile.getStreet(), 
							profile.getCity(),
							profile.getState(),
							profile.getZip());
		v.setAmountToCharge(profile.getAmount());
		v.setCreditCardExpirationDate(profile.getExpirationDate());
		v.setCreditCardNumber(profile.getCardNum());
		v.setCustomerFirstName(profile.getFirstName());
		v.setCustomerLastName(profile.getLastName());
		try {
			LOG.info("Processing credit verification request...");
			v.processRequest();
			LOG.info("...passed");
			
		}
		catch (TransactionFailedException tfe) {
			++numFailures;
			if(numFailures <= 1) {
				throw new MiddlewareException(tfe.getMessage());
			} else {
				LOG.warning("Getting many Credit Verification failures");
			}
		}
	}
}
