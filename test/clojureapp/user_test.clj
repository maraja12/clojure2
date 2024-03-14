(ns clojureapp.user-test
  (:require [midje.sweet :refer :all]
            [user.user :refer :all]
            [validation.validation :refer :all]))

(fact "Valid input for REQUIRED field"
      (with-in-str "Drama" (get-value-required-field "valid test" is-empty))
      => "Drama")

(fact "Invalid input -> valid input"
      (with-in-str "\nDrama"
                   (get-value-required-field "invalid-valid test" is-empty))
      => "Drama")

(fact "Invalid input -> ... -> valid input"
      (with-in-str "\n\n\nDrama"
                   (get-value-required-field "multiple-invalid-valid test" is-empty))
      => "Drama")

(fact "Valid input for required NUMBER field"
      (with-in-str "2000"
                   (get-value-required-field-num "number test" is-empty check-int))
      => "2000")

(fact "Invalid input -> valid input (NUMBER field)"
      (with-in-str "aa\n2000"
                   (get-value-required-field-num "number test" is-empty check-int))
      => "2000")

(fact "Invalid input -> ... -> valid input (NUMBER field)"
      (with-in-str "\n\nAAA\n2019"
                   (get-value-required-field-num " number test" is-empty check-int))
      => "2019")




