(ns ds.ds)
(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[clojure.data.json :as json])


(def movies-ds (with-open [in-file (io/reader "C:/Users/Marija/clojureapp/resources/movies/imdb_top_1000.csv")]
                 (doall
                   (csv/read-csv in-file))))
(def movies-id-ds (with-open [in-file (io/reader "C:/Users/Marija/clojureapp/resources/movies/movies.csv")]
                    (doall
                      (csv/read-csv in-file))))

(def ratings-ds (with-open [in-file (io/reader "C:/Users/Marija/clojureapp/resources/movies/ratings.csv")]
                    (doall
                      (csv/read-csv in-file))))
(defn to-keys
  [s]
  (map #(keyword %) s))

(defn vectors-to-maps [ds]
  "Converting vector into map, keys are taken from the first row"
  (let [keys (to-keys (first ds))
        values (rest ds)]
    (map #(zipmap keys %) values)))

(def movies (vectors-to-maps movies-ds))
(def movies-id (vectors-to-maps movies-id-ds))
(def ratings (vectors-to-maps ratings-ds))

(defn format-output
  "Enables better display for user"
  [movie]
  (str
    "TITLE: " (:Series_Title movie) ", "
    "GENRE: " (:Genre movie) ", "
    "YEAR: " (:Released_Year movie) ", "
    "DURATION: " (:Runtime movie)", "
    "RATING: " (:IMDB_Rating movie)))

(defn format-movie
  "Movie format with more details"
  [movie]
  (str
    "TITLE: " (:Series_Title movie) ", "
    "GENRE: " (:Genre movie) ", "
    "YEAR: " (:Released_Year movie) ", "
    "DURATION: " (:Runtime movie)", "
    "RATING: " (:IMDB_Rating movie)", "
    "DIRECTOR: " (:Director movie)", "
    "STARS: " (:Star1 movie)", " (:Star2 movie)", "
    (:Star3 movie)", " (:Star4 movie)))

(defn print-format
  "Printing movies in new format, with added row numbers"
  [m]
  (println)
  (println "*** MOVIE(S) FOR YOU ***")
  (doseq [index (range (count m))]
    (let [m (nth m index)]
      (println (str (inc index) ". ") (format-output m)))))

;(print-format movies)
(defn print-movie
  "Printing movie in new format"
  [m]
  (println)
  (println "*** MOVIE FOR YOU ***")
  (doseq [movie m]
    (println (format-movie movie))
    ))