(ns user.user
  (:require [ds.ds :refer :all]
            [clojure.string :as str]
            [blogic.blogic :refer :all]))

(defn get-value
  "Reading value entered by user"
  [x]
  (println (str "Please enter " x " " ))
  (let [value (read-line)]
    value
    )
  )

;(defn first-try
;  []
;  (let [year (get-value "year")
;        genre (get-value "genre")]
;    (print-format (year-genre year genre)))
;  )

;(defn try-with-x
;  []
;  (let [year (get-value "year")
;        genre (get-value "genre")]
;    (if (= year "x")
;      (print-format (find-genre genre movies))
;      (print-format (year-genre year genre)))
;    ))
(defn first-choice
  []
  (println "If you want to skip field, just press ENTER")
  (println)
  (let [year (get-value "year: (will find entered age or younger)")
        genre (get-value "genre:
(Film-Noir, Thriller, Horror, Action, Adventure, War, Drama,
Mystery, Crime, Comedy, Romance, Fantasy, Family, Music, ...)")
        rating (get-value "rating: (will find entered rate or higher)")
        duration (get-value "duration: (will find entered duration or shorter)")
        actor (get-value "actor:")
        director (get-value "director:")]
    (print-format
      (year-genre-rating-duration-actor-director
        year genre rating duration actor director))

    ))

