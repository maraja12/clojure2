(ns blogic.blogic
  (:require [clojure.string :as str]
            [ds.ds :refer :all]))

(defn find-genre
  "Filtering data by genre"
  [genre]
  (filter #(clojure.string/includes? (:Genre %) genre) movies))
;(find-genre "Comedy")

(defn find-title
  "Filtering data by title"
  [title]
  (filter #(clojure.string/includes? (:Series_Title %) title) movies))
;(find-title "Casino")

(defn str-to-int
  "Converting string into integer"
  [s]
  (try
    (Integer/parseInt s)
    (catch NumberFormatException e
      nil))
  )

(defn str-to-double
  "Converting string into double"
  [s]
  (try
    (Double/parseDouble s)
    (catch NumberFormatException e
      nil))
  )

(defn find-year
  "Filtering movies which are younger or exact year"
  [year]
  (filter #(>= (or (str-to-int (:Released_Year %)) 0) (str-to-int year)) movies))
;(find-year "2019")

(defn year-genre
  "Filtering movies with entered genre and year"
  [year genre]
  (filter #(and (= (or (:Released_Year %) 0) year)
                (clojure.string/includes? (:Genre %) genre))
          movies)
  )
;(year-genre "2020" "Drama")

(defn year-genre-rating
  [year genre rating]
  (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) (str-to-int year))
                (clojure.string/includes? (:Genre %) genre)
                (>= (or (str-to-double (:IMDB_Rating %)) 0) (str-to-double rating)))
          movies)
  )
;(year-genre-rating "2019" "Comedy" "8.5")

(defn year-genre-director
  [year genre director]
  (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) (str-to-int year))
                (clojure.string/includes? (:Genre %) genre)
                (clojure.string/includes? (:Director %) director))
          movies)
  )
;(year-genre-director "2019" "Comedy" "Bong Joon Ho")




