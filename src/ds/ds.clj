(ns ds.ds)
(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[clojure.data.json :as json])


(def movies-ds (with-open [in-file (io/reader "C:/Users/Marija/clojureapp/resources/movies/imdb_top_1000.csv")]
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

(defn format-output
  "Enables better display for user"
  [movie]
  (str
    "TITLE: " (:Series_Title movie) ", "
    "GENRE: " (:Genre movie) ", "
    "YEAR: " (:Released_Year movie) ", "
    "DURATION: " (:Runtime movie)", "
    "RATING: " (:IMDB_Rating movie))
  )

(defn print-format
  "Printing movies in new format, with added row numbers"
  [m]
  (println)
  (println "*** MOVIE(S) FOR YOU ***")
  (doseq [index (range (count m))]
    (let [m (nth m index)]
      (println (str (inc index) ". ") (format-output m)))))
;(print-format movies)

;(mapv
;  #(clojure.string/includes? (:Genre %) "Comedy") test-example
;  )