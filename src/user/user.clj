(ns user.user
  (:require [ds.ds :refer :all]
            [clojure.string :as str]
            [blogic.blogic :refer :all]))

(defn get-value
  "Reading value entered by user"
  [x]
  (println (str "Please enter " x ": " ))
  (let [value (read-line)]
    (println x ": " \" value \")
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
(defn first-choice
  []
  (let [year (get-value "year")
        genre (get-value "genre")
        rating (get-value "rating")
        duration (get-value "duration")
        actor (get-value "actor")
        director (get-value "director")]
    (print-format
      (year-genre-rating-duration-actor-director
        year genre rating duration actor director))

    ))

