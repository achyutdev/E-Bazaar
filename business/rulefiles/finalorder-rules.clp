(defclass finalorder-template business.rulesbeans.FinalOrderBean)

(defmodule rules-finalorder)
(import java.util.*)
(import business.util.*)

(deffunction keysNoGreaterThanValues(?list)
    (foreach ?pair ?list
    	(if (> (?pair getFirst) (?pair getSecond))
    		then (return FALSE)
        )
    )
    (return TRUE)
)
(defrule req-not-more-than-avail
    (finalorder-template (requestedAvailableList ?z) )
    (test (= (keysNoGreaterThanValues ?z) FALSE))
     =>    
    (throw (new rulesengine.ValidationException "One of your Orders requests more items than are currently in stock.")))
 

(bind ?l (new ArrayList))
(bind ?p (new Pair 5 4))
(?l add ?p)
(printout t ((?l get 0) getFirst) crlf)
;(bind ?h (new HashMap))
;(?h put 10 8)  
;(?h put 9 9)
;(bind ?h2 (new HashMap))
;(?h2 put 10 8)  
;(?h2 put 9 11)
;(printout t "h passes test? " (valuesNoGreaterThanKeys ?h) crlf)
;(printout t "h2 passes test? " (valuesNoGreaterThanKeys ?h2) crlf)

