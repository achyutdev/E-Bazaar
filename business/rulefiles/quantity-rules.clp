(defclass quantity-template business.rulesbeans.QuantityBean)

(defmodule rules-quantity)

(defrule non-blank
    (declare (salience 10))
	(quantity-template (quantityRequested ?r&:(= (call ?r length) 0)))
    =>
	(throw (new rulesengine.ValidationException "This is a required field.")))

(defrule numeric
    (declare (salience 5))
	(quantity-template (quantityRequested ?r&:(not (isNumeric ?r))))
    =>
	(throw (new rulesengine.ValidationException "The value must be numeric.")))

(defrule bigger-than-zero
    (declare (salience 3))
    (quantity-template (quantityRequested ?r))
	(test (<= (call ?r compareTo "0") 0))
    =>
    (throw (new rulesengine.ValidationException "The value  must be a positive number.")))
 
(defrule sufficient-quantity
    (declare (salience 0))
    (quantity-template (quantityAvailable ?a) (quantityRequested ?r))
	(test (and (isNumeric ?r) (> (call Integer parseInt ?r)  ?a)))
     =>
  	(throw (new rulesengine.ValidationException "Quantity requested exceeds quantity available")))

(deffunction isNumeric(?str)
    (if (= (call ?str length) 0) then (return FALSE)
     else 
        (try (call Integer parseInt ?str)
	        (return TRUE)
        catch     
    		(return FALSE)
        )
     )
)
