
(defclass address-template business.rulesbeans.AddressBean)
 
(defmodule rules-address) 

(defrule ziplength
     ?q <-(address-template (zip ?z))
     (test (not (= (?z length) 5)))
     =>
     (retract ?q)
     (throw (new rulesengine.ValidationException "Zipcode must have exactly 5 characters")))

(defrule zipnumeric
    (declare (salience 5))
	?q <-(address-template (zip ?z))
     (test (not (isNumeric ?z)))
    =>
	(throw (new rulesengine.ValidationException "Zipcode must be numeric.")))
  
 (defrule address-required
    (declare (salience 10))
    ?a <- (address-template (street ?z1) (city ?z2) (state ?z3) (zip ?z4))
    (test (or (?z1 equals "") (?z2 equals "") (?z3 equals "") (?z4 equals "")))
    =>
    (retract ?a)
    (throw (new rulesengine.ValidationException "All address fields are required")))

 (defrule address-cleanse
 	(declare (salience -100))
 	?m <-(address-template (street ?z1) (city ?z2) (state ?z3) (zip ?z4))
 	=>
    (retract ?m)
 	(bind ?u1 (capitalize ?z1))
 	(bind ?u2 (capitalize ?z2))
 	(bind ?u3 (capitalize ?z3))
 	(bind ?u4 (capitalize ?z4))
 	(bind ?addr (create$ ?u1 ?u2 ?u3 ?u4))
 	(store update ?addr))

 (deffunction capitalize (?X)
    (bind ?s (call ?X toUpperCase))
    (return ?s))

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
  		