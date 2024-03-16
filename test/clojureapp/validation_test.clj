(ns clojureapp.validation-test
  (:require [midje.sweet :refer :all]
            [validation.validation :refer :all]))

(fact
  "Invalid argument"
  (str-to-int "") => nil)
(fact
  "Invalid argument"
  (str-to-int "a") => nil)
(fact
  "Invalid argument"
  (str-to-int "1.1") => nil)
(fact
  "Valid argument"
  (str-to-int "1") => 1)

(fact
  "Valid argument"
  (str-to-double "1") => 1.0)

