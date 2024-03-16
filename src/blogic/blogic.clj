(ns blogic.blogic
  (:require [clojure.string :as str]
            [ds.ds :refer :all]
            [validation.validation :refer :all]
            [db.db :refer :all]))

(defn find-genre
  "Filtering data by genre"
  [genre ds]
      (filter #(clojure.string/includes? (:Genre %) genre) ds))


(defn find-title
  "Filtering data by title"
  [title]
  (filter #(= (:Series_Title %) title) movies))
;(find-title "Casino")

(defn find-title-part
  "Filtering data by title"
  [title]
  (filter #(clojure.string/includes? (:Series_Title %) title) movies))
;(find-title-part "Shawshank Redemption")

(defn extract-duration
  "Extracting number from :Runtime"
  [s]
  (when s
    (when-let
      [num-str (re-find #"\d+" s)]
      num-str)))
;(extract-duration "67 min")

(defn find-year
  "Filtering movies which are younger or exact year"
  [year]
  (let [year (str-to-int year)]
    (if (not= year nil)
             (filter #(>= (or (str-to-int (:Released_Year %)) 0) year) movies)
             (println "Please enter the value again..."))))
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
          movies))
;(year-genre "2020" "Drama")

(defn year-genre-rating
  [year genre rating]
  (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) (str-to-int year))
                (clojure.string/includes? (:Genre %) genre)
                (>= (or (str-to-double (:IMDB_Rating %)) 0) (str-to-double rating)))
          movies))
;(year-genre-rating "2019" "Comedy" "8.5")

(defn year-genre-rating-duration
  [year genre rating duration]
  (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) (str-to-int year))
                (clojure.string/includes? (:Genre %) genre)
                (>= (or (str-to-double (:IMDB_Rating %)) 0) (str-to-double rating))
                (<= (or (str-to-int (extract-duration (:Runtime %))) 0)
                    (str-to-int duration)))
          movies))
;(year-genre-rating-duration "2019" "Comedy" "8.0" "100")

(defn year-genre-director
  [year genre director]
  (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) (str-to-int year))
                (clojure.string/includes? (:Genre %) genre)
                (clojure.string/includes? (:Director %) director))
          movies))
;(year-genre-director "2019" "Comedy" "Bong Joon Ho")

;now with optional fields: director and actor

(defn year-genre-rating-duration-director
  [year genre rating duration director]
  (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) (str-to-int year))
                (clojure.string/includes? (:Genre %) genre)
                (>= (or (str-to-double (:IMDB_Rating %)) 0) (str-to-double rating))
                (<= (or (str-to-int (extract-duration (:Runtime %))) 0)
                    (str-to-int duration))
                (clojure.string/includes? (:Director %) director))
          movies))
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
                (clojure.string/includes? (:Stars %) actor))
          (movies-with-stars movies)))
;(year-genre-rating-duration-actor "2019" "Comedy" "8.0" "100" "Rashida Jones")

(defn year-genre-rating-duration-actor-director
  "Finding movie(s) based on user input for
  year, genre, rating, duration, and optionally actor and director"
  [year genre rating duration actor director]
  (let [year (str-to-int year)
        rating (str-to-double rating)
        duration (str-to-int duration)]
    (filter #(and (>= (or (str-to-int (:Released_Year %)) 0) year)
                  (clojure.string/includes? (:Genre %) genre)
                  (>= (or (str-to-double (:IMDB_Rating %)) 0) rating)
                  (<= (or (str-to-int (extract-duration (:Runtime %))) 0)
                      duration)
                  (clojure.string/includes? (:Stars %) actor)
                  (clojure.string/includes? (:Director %) director))
            (movies-with-stars movies))))
(defn adapt-rating
  [rate]
  "If user enters rating 5, it is converted into 0... 10 -> 5
  **This is the way I found at Clojure CheatSheet, because format method
  returned decimal number with comma, instead of dot, and operation - had
  small rounding errors.
  "
  (let [res (conj [] (- (str-to-double rate) 5))
        formatted (java.lang.String/format
                    java.util.Locale/US "%.1f" (to-array res))]
    formatted)
  )
;(adapt-rating "6.1")

(defn clean-title [title]
  "Removes year from title in movies.csv"
  (if (re-matches #".*\((\d{4})\)$" title)
    (subs title 0 (- (count title) 7))
    title))
;(clean-title "Toy (2000)")
(defn compare-movies
  [t1 t2]
  (= t1 (clean-title t2)))
;(compare-movies "Toy" "Toy (2000)")

(defn find-same-movie
  "Finding movie in 'movies.csv' file"
  [title]
  (let [filtered (filter #(clojure.string/includes? (:title %) title) movies-id)]
    (filter #(compare-movies title (:title %)) filtered)))

;(find-same-movie "Toy Story")
(defn find-movieId
  "Finding id of a movie in 'movies.csv' file"
  [title]
  (mapv :movieId (find-same-movie title)))
;(find-movieId "Casino")
(defn find-users
  "Finding all users who rated this movie"
  [title]
  (let [id (get (find-movieId title) 0)]
    (filter #(= (:movieId %) id)ratings)))

;(find-users "Casino")

(defn same-rating
  "Finding user(s) who rated this movie with same mark"
  [title rate]
  (let [users (find-users title)]
    (filter #(= (:rating %) rate) users)))
;(same-rating "Forrest Gump" "2.0")
;(same-rating "Toy Story" "2.5")
;(same-rating "Casino" "4.0")
(defn user-movies-id
  "Finding movie(s) that user rated with mark 5"
  [userId]
  (let [movies (filter #(= (:userId %) userId) ratings)]
    (filter #(= (:rating %) "5.0") movies)))
;(user-movies-id "6")

(defn users-movies
  "Finding best rated movies (mark 5) of all users
  who rated this movie with same mark"
  [title rate]
  (let [users (same-rating title rate)]
    (map #(user-movies-id (:userId %)) users)))
;(users-movies "Casino" "4.0")
(defn find-movie-by-id
  [id]
  (first (filter #(= (:movieId %) id) movies-id)))
;(find-movie-by-id "353")

(defn split-genre
  [genre]
  (str/split genre #", ")
  )
;(split-genre "Crime, Drama")

(defn ratings-to-movies
  "Taking only films that are the same genre as this movie"
  [title rate]
  (let [movies (apply concat (users-movies title rate))
        ids (mapv :movieId movies)]
    (reduce
      (fn [acc e]
        (if (not (empty? e))
          (conj acc (find-movie-by-id e))
          acc))
      [] ids)))

;(ratings-to-movies "Casino" "4.0")
(defn take-same-genre
  [title rate genre]
  "Finding the matching movie genres."
  (let [genres (split-genre genre)
        movies (ratings-to-movies title rate)]
    (reduce
      (fn [acc e]
        (let [g (:genres e)
              res (map #(clojure.string/includes? g %) genres)]
          (if (some true? res)
            (conj acc e)
            acc)))
      [] movies)))
;(take-same-genre "Casino" "4.0" "Crime, Drama")
(defn titles-in-movies-imdb
  [title rate genre]
  "Finding matching titles in imdb_top_1000.csv"
  (let [movies (take-same-genre title rate genre)]
    (reduce
      (fn [acc e]
        (if (not (some #(= (clean-title (:title e)) %) acc))
          (conj acc (clean-title (:title e)))
          acc))
      [] movies)))
;(titles-in-movies-imdb "Casino" "3.5" "Crime, Drama")
;(titles-in-movies-imdb "Perfetti sconosciuti" "Comedy, Drama" "2.5")

(defn find-movie-in-movies-imdb
  "Replacing movies from movies.csv"
  [title rate genre]
  (let [titles (titles-in-movies-imdb title rate genre)]
    (reduce
      (fn [acc e]
        (if (not (empty? (find-title-part e)))
          (conj acc (first (find-title-part e)))
          acc))
      [] titles)))

;(find-movie-in-movies-imdb "Casino" "3.5" "Crime, Drama")
;(find-movie-in-movies-imdb "Green Book" "4.5" "Biography, Comedy, Drama")
;(find-movie-in-movies-imdb "Amelie" "4.5" "Biography, Comedy, Drama")

(defn reverse-array
  [a]
  (reduce
    (fn [acc e]
      (conj acc e))
    '() a))
(defn user-history
  "Reverses order of rows in user history"
  [user]
  (reverse-array (get-history-userId (get-connection) (:USERS/ID user))))

(defn take-from-history
  ([user]
   (take-from-history user (user-history user))
   )
  ([user h]
   "Recommendation based on latest user history, so it always
   has new recommendation list for watching."
   (let [history (first h)]
     (if (not= history nil)
       (do
         (let [title (:HISTORY/TITLE history)
               rate (:HISTORY/RATING history)
               genre (:HISTORY/GENRE history)
               res (find-movie-in-movies-imdb title rate genre)]
           (if (empty? res)
             (take-from-history user (next h))
             (do
               (print-format res)
               res))))
       (do
         (println "Your history is empty! You have not rated any movies yet...
or other users do not have same opinion as you.")
         nil)))))
;(take-from-history #:USERS{:ID 1, :USERNAME "marija", :PASSWORD "marija12"})