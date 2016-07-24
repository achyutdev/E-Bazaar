(defclass payment-template business.rulesbeans.PaymentBean)

(defmodule rules-payment)
(import java.util.*)
(import rulesupport.*)
(import rulesengine.*)
(deffunction isNumeric(?str)
    (if (= (call ?str length) 0) then (return FALSE)
     else 
        (try (new java.math.BigInteger ?str)
	        (return TRUE)
        catch     
    		(return FALSE)
        )
     )
) 

(defrule mastercard-illinois
    (payment-template (cardType "MasterCard") (state "IL"))  
    =>
    (throw (new rulesengine.ValidationException "MasterCard is not allowed in Illinois."))
)
(defrule payment-required
    (declare (salience 10))
    ?a <- (payment-template (cardNum ?z1) (expirationDate ?z2) (nameOnCard ?z3) (cardType ?z4))
    (test (or (?z1 equals "") (?z2 equals "") (?z3 equals "") (?z4 equals "")))
    =>
    (retract ?a)
    (throw (new rulesengine.ValidationException "All payment fields are required")))

(defrule card-num-length
     ?q <-(payment-template (cardNum ?z)) 
     (test (not (= (?z length) 16)))
     =>
     (printout t "length = " (?z length) crlf)
     (retract ?q)
     (throw (new rulesengine.ValidationException "Card number must have exactly 16 characters")))
 
(defrule card-num-numeric
    (declare (salience 5))
	?q <-(payment-template (cardNum ?z))
     (test (not (isNumeric ?z)))
    =>
	(throw (new rulesengine.ValidationException "Card number must be numeric.")))

(defrule card-expiration
    (declare (salience 10))
	?q <-(payment-template (expirationDate ?z))
     (test (= (call CalendarUtil isAfterToday ?z) FALSE))
    =>   
    (retract ?q)
	(throw (new rulesengine.ValidationException "Credit card expiration date is invalid.")))
  
;(printout t (isNumeric "01/02/2008") crlf)

;(printout t (call CalendarUtil isAfterToday "01/01/2008") crlf)
 