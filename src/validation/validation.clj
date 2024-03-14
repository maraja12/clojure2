(ns validation.validation)

(defn str-to-int
  "Converting string into integer"
  [s]
  (if (not (empty? s))
    (try
      (Integer/parseInt s)
      (catch NumberFormatException e
        ;(println "Invalid input! This filed requires a number.")
        nil))))

(defn str-to-double
  "Converting string into double"
  [s]
  (try
    (Double/parseDouble s)
    (catch NumberFormatException e
      ;(println "Invalid input! This filed requires a number.")
      nil)))

(defn is-empty
  [x]
 (not (empty? x)))
(is-empty "")


(defn check-int
  "Checks if string that user entered is integer type"
  [n]
  (number? (str-to-int n)))

(defn check-double
  "Checks if string that user entered is double type"
  [n]
  (number? (str-to-double n)))
;(number? nil)




