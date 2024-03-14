(ns clojureapp.blogic-test
  (:require [blogic.blogic :refer :all]
            [ds.ds :refer :all]
            [midje.sweet :refer :all]
            [validation.validation :refer :all]
            ))

(fact "Checking if obtained movie(s) match all criteria"
      (year-genre-rating-duration-actor-director
        "1961" "Romance" "7.0" "120" "Buddy Ebsen" "Blake Edwards" ) =>
      '({:Genre "Comedy, Drama, Romance",
       :Gross "",
       :No_of_Votes "166544",
       :Released_Year "1961",
       :Overview "A young New York socialite becomes interested in a young man who has moved into her apartment building, but her past threatens to get in the way.",
       :Star2 "George Peppard",
       :Star3 "Patricia Neal",
       :Star1 "Audrey Hepburn",
       :Meta_score "76",
       :Poster_Link "https://m.media-amazon.com/images/M/MV5BNGEwMTRmZTQtMDY4Ni00MTliLTk5ZmMtOWMxYWMyMTllMDg0L2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyNjc1NTYyMjg@._V1_UX67_CR0,0,67,98_AL_.jpg",
       :Star4 "Buddy Ebsen",
       :Stars "Audrey Hepburn, George Peppard, Patricia Neal, Buddy Ebsen"
       :Certificate "A",
       :Series_Title "Breakfast at Tiffany's",
       :Director "Blake Edwards",
       :Runtime "115 min",
       :IMDB_Rating "7.6"})
      )

(fact "Testing when there aro no movies matching criteria"
      (year-genre-rating-duration-actor-director
        "1961" "Romance" "7.0" "120" "Al Pacino" "Blake Edwards" ) =>
      ()
      )
(fact "Testing with empty genre, actor and duration,
       with obviously satisfied 'number' fields,
       so it should return whole dataset"
      (year-genre-rating-duration-actor-director
        "1900" "" "7.0" "300" "" "" ) =>
      movies-with-stars)

(defn check-include
  "Returns vector of true/false values depending on whether
  the key contains forwarded value"
  [key value ds]
  (mapv
    #(clojure.string/includes? (key %) value) ds))

(fact "Checking whether each movie of the result contains
       forwarded value of genre"
      (let [result (year-genre-rating-duration-actor-director
                     "2019" "Comedy" "8" "150" "" "")]
        (every? true? (check-include :Genre "Comedy" result)) => true
        ;(some false? (check-include :Genre "Comedy" result)) => falsey
        ))

(defn map-years
  "Making an array of all years from dataset"
  [ds]
  (mapv #(str-to-int (:Released_Year %)) ds))

(def years
  (reduce
    (fn [acc e]
      (if (nil? e)
        acc
        (conj acc e)
        ))
    [] (map-years movies)))
;(count years)

(defn check-year
  "Making a vector of years which satisfy condition"
  [year]
  (reduce
    (fn [acc e]
      (if (> year e)
        acc
        (conj acc (str e))))
    [] years))
;(check-year 2019)
;(count (check-year 2019))
(fact "Checking whether Released years from the result
       are equal to expected years"
      (let [result (find-year "2019")]
        (= (check-year 2019) (mapv :Released_Year result)) => true))


;(mapv :Released_Year (find-year "2019"))
;(count (map :Released_Year (find-year "2019")))

;(year-genre-rating-duration-actor-director
;  "2019" "Drama" "8" "150" "" "")
;(year-genre-rating-duration-actor-director
  ;"2019" "Comedy" "8" "150" "" "")
;(check-include :Released_Year "2019" (year-genre-rating-duration-actor-director
                              ;"2019" "Comedy" "8" "150" "" ""))

