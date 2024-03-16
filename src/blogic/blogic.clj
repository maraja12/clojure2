(ns blogic.blogic
  (:require [clojure.string :as str]
            [ds.ds :refer :all]
            [validation.validation :refer :all]
            [db.db :refer :all]))

(defn find-title
  "Filtering data by title"
  [title]
  (filter #(= (:Series_Title %) title) movies))

(defn find-title-part
  "Filtering data by part of title"
  [title]
  (filter #(clojure.string/includes? (:Series_Title %) title) movies))

(defn extract-duration
  "Extracting number part of string"
  [s]
  (when s
    (when-let
      [num-str (re-find #"\d+" s)]
      num-str)))

(defn find-year
  "Filtering movies which are younger or exact year"
  [year]
  (let [year (str-to-int year)]
    (if (not= year nil)
             (filter #(>= (or (str-to-int (:Released_Year %)) 0) year) movies)
             (println "Please enter the value again..."))))

(defn concat-stars [movie]
  (str (:Star1 movie) ", " (:Star2 movie) ", " (:Star3 movie) ", " (:Star4 movie)))

(defn add-stars [movie]
  "Merging all stars into value od :Stars"
  (if (not (empty? movie))
    (assoc movie :Stars (concat-stars movie))))

(defn movies-with-stars [ds]
  (map add-stars ds))

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

;Here starts code for the recommendation algorithm.
;Almost always the following method relies on the previous one.
;Sometimes, I just describe what is new in that method compared to
;the previous one,in order to avoid enormous comments.
(defn adapt-rating
  [rate]
  "If user enters rating 5, it is converted into 0... 10 -> 5
  **This is the way I found at Clojure CheatSheet, because format method
  returned decimal number with comma, instead of dot, and operation - had
  small rounding errors."
  (if (double? (str-to-double rate))
    (let [res (conj [] (- (str-to-double rate) 5))
          formatted (java.lang.String/format
                      java.util.Locale/US "%.1f" (to-array res))]
      formatted)))

(defn clean-title [title]
  "Removes year from title in movies.csv"
  (if (re-matches #".*\((\d{4})\)$" title)
    (subs title 0 (- (count title) 7))
    title))

(defn compare-movies
  [t1 t2]
  (= t1 (clean-title t2)))

(defn find-same-movie
  "Finding movie in 'movies.csv' file"
  [title]
  (let [filtered (filter #(clojure.string/includes? (:title %) title) movies-id)]
    (filter #(compare-movies title (:title %)) filtered)))

(defn find-movieId
  "Finding id of a movie in 'movies.csv' file"
  [title]
  (mapv :movieId (find-same-movie title)))

(defn find-users
  "Finding all users who rated this movie"
  [title]
  (let [id (get (find-movieId title) 0)]
    (filter #(= (:movieId %) id)ratings)))


(defn same-rating
  "Finding user(s) who rated this movie with same mark"
  [title rate]
  (let [users (find-users title)]
    (filter #(= (:rating %) rate) users)))

(defn user-movies-id
  "Finding movie(s) that user rated with mark 5"
  [userId]
  (let [movies (filter #(= (:userId %) userId) ratings)]
    (filter #(= (:rating %) "5.0") movies)))

(defn users-movies
  "Finding best rated movies (mark 5) of all users
  who rated this movie same as this"
  [title rate]
  (let [users (same-rating title rate)]
    (map #(user-movies-id (:userId %)) users)))

(defn find-movie-by-id
  [id]
  (first (filter #(= (:movieId %) id) movies-id)))

(defn split-genre
  [genre]
  (str/split genre #", "))

(defn ratings-to-movies
  "Taking only films that are same genre as this movie."
  [title rate]
  (let [movies (apply concat (users-movies title rate))
        ids (mapv :movieId movies)]
    (reduce
      (fn [acc e]
        (if (not (empty? e))
          (conj acc (find-movie-by-id e))
          acc))
      [] ids)))

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

(defn find-movie-in-movies-imdb
  "Replacing movies from movies.csv with imdb movies"
  [title rate genre]
  (let [titles (titles-in-movies-imdb title rate genre)]
    (reduce
      (fn [acc e]
        (if (not (empty? (find-title-part e)))
          (conj acc (first (find-title-part e)))
          acc))
      [] titles)))
;(find-movie-in-movies-imdb "Casino" "3.5" "Crime, Drama")

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
  "Recommendation based on latest user history, so it always
   has new recommendation list for watching."
  ([user]
   (take-from-history user (user-history user))
   )
  ([user h]
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