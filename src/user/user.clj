(ns user.user
  (:require [ds.ds :refer :all]
            [clojure.string :as str]
            [blogic.blogic :refer :all]))

(defn get-value
  "Reading value entered by user"
  [x]
  (println (str "Please enter the " x ": " ))
  (let [value (read-line)]
    value)
  )

(defn first-try
  []
  (let [year (get-value "year")
        genre (get-value "genre")]
    (print-format (year-genre year genre)))
  )

(defn try-with-x
  []
  (let [year (get-value "year")
        genre (get-value "genre")]
    (if (= year "x")
      (print-format (find-genre genre))
      (print-format (year-genre year genre)))
    ))

