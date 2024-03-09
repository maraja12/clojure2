(ns blogic.blogic
  (:require [clojure.string :as str]
            [ds.ds :refer :all]))

(defn find-genre
  "Filtering data by genre"
  [genre ds]
  (filter #(clojure.string/includes? (:Genre %) genre) ds))


(defn find-title
  "Filtering data by title"
  [title]
  (filter #(clojure.string/includes? (:Series_Title %) title) movies))
;(find-title "Casino")

(defn str-to-int
  "Converting string into integer"
  [s]
  (if (not (empty? s))
    (try
      (Integer/parseInt s)
      (catch NumberFormatException e
        ;(println "Invalid input! This filed requires a number.")
        nil))
    )
  )

(defn str-to-double
  "Converting string into double"
  [s]
  (try
    (Double/parseDouble s)
    (catch NumberFormatException e
      ;(println "Invalid input! This filed requires a number.")
      nil))
  )

(defn extract-duration
  "Extracting number from :Runtime"
  [s]
  (when s
    (when-let
      [num-str (re-find #"\d+" s)]
      num-str
      ))
  )
;(extract-duration "67 min")

(defn find-year
  "Filtering movies which are younger or exact year"
  [year]
  (let [year (str-to-int year)]
    (if (not= year nil)
             (filter #(>= (or (str-to-int (:Released_Year %)) 0) year) movies)
             (println "Please enter the value again...")
             )

    ))
;(find-year "2019")
;(find-year "")

(defn find-duration
  "Filtering data by duration"
  [duration]
  (filter #(<= (or (str-to-int (extract-duration (:Runtime %))) 0)
                (str-to-int duration)) movies))
;(find-duration "100")

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

(defn year-genre-rating-duration
  [year genre rating duration]
  (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) (str-to-int year))
                (clojure.string/includes? (:Genre %) genre)
                (>= (or (str-to-double (:IMDB_Rating %)) 0) (str-to-double rating))
                (<= (or (str-to-int (extract-duration (:Runtime %))) 0)
                    (str-to-int duration))
                )
          movies)
  )
;(year-genre-rating-duration "2019" "Comedy" "8.0" "100")

(defn year-genre-director
  [year genre director]
  (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) (str-to-int year))
                (clojure.string/includes? (:Genre %) genre)
                (clojure.string/includes? (:Director %) director))
          movies)
  )
;(year-genre-director "2019" "Comedy" "Bong Joon Ho")

;now with optional fields: director and actor

(defn year-genre-rating-duration-director
  [year genre rating duration director]
  (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) (str-to-int year))
                (clojure.string/includes? (:Genre %) genre)
                (>= (or (str-to-double (:IMDB_Rating %)) 0) (str-to-double rating))
                (<= (or (str-to-int (extract-duration (:Runtime %))) 0)
                    (str-to-int duration))
                (clojure.string/includes? (:Director %) director)
                )
          movies)
  )
;(year-genre-rating-duration-director "2019" "Comedy" "8.0" "100" "Pete Docter")

(defn concat-stars [movie]
  (str (:Star1 movie) ", " (:Star2 movie) ", " (:Star3 movie) ", " (:Star4 movie)))

(defn add-stars [movie]
  (assoc movie :Stars (concat-stars movie)))

(defn movies-with-stars [ds]
  (map add-stars ds))
;(movies-with-stars movies)

(defn year-genre-rating-duration-actor
  [year genre rating duration actor]
  (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) (str-to-int year))
                (clojure.string/includes? (:Genre %) genre)
                (>= (or (str-to-double (:IMDB_Rating %)) 0) (str-to-double rating))
                (<= (or (str-to-int (extract-duration (:Runtime %))) 0)
                    (str-to-int duration))
                (clojure.string/includes? (:Stars %) actor)
                )
          (movies-with-stars movies))
  )
;(year-genre-rating-duration-actor "2019" "Comedy" "8.0" "100" "Rashida Jones")

(defn year-genre-rating-duration-actor-director
  [year genre rating duration actor director]
  (let [year (str-to-int year)
        rating (str-to-double rating)
        duration (str-to-int duration)]
    (if (some nil? [year rating duration])
      (println "
      Invalid input! This filed requires a number.
      Please enter the value again...")
      (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) year)
                    (clojure.string/includes? (:Genre %) genre)
                    (>= (or (str-to-double (:IMDB_Rating %)) 0) rating)
                    (<= (or (str-to-int (extract-duration (:Runtime %))) 0)
                        duration)
                    (clojure.string/includes? (:Stars %) actor)
                    (clojure.string/includes? (:Director %) director)
                    )
              (movies-with-stars movies))
      ))
    )

;(year-genre-rating-duration-actor "2019" "Comedy" "8.0" "100" "")
;(year-genre-rating-duration-actor-director "2019" "Comedy" "8.0" "100" "J.K. Simmons" "Pete Docter")
;(year-genre-rating-duration-actor-director
  ;"2020" "Comedy" "8.0" "100" "" "")
;(year-genre-rating-duration-actor-director "2019" "Comedy" "8.0" "" "" "Pete Docter")
;(year-genre-rating-duration-actor-director "" "Comedy" "" "" "" "")
