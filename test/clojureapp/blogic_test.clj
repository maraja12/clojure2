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
        "1961" "Romance" "7.0" "120" "Al Pacino" "Blake Edwards" ) => ())

(fact "Testing with empty genre, actor and duration,
       with obviously satisfied 'number' fields,
       so it should return whole dataset"
      (year-genre-rating-duration-actor-director
        "1900" "" "7.0" "300" "" "" ) =>
      movies-with-stars)

(fact "Checking whether each movie of the result contains
       forwarded value of genre"
      (let [result (year-genre-rating-duration-actor-director
                     "2019" "Comedy" "8" "150" "" "")]
        (every? true?
                (mapv #(clojure.string/includes?
                         (:Genre %) "Comedy") result)) => true))

(fact "Returns a movie matching the title"
      (find-title "Inception") => '({:Certificate "UA"
                                    :Director "Christopher Nolan"
                                    :Genre "Action, Adventure, Sci-Fi"
                                    :Gross "292,576,195"
                                    :IMDB_Rating "8.8"
                                    :Meta_score "74"
                                    :No_of_Votes "2067042"
                                    :Overview "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O."
                                    :Poster_Link "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_UX67_CR0,0,67,98_AL_.jpg"
                                    :Released_Year "2010"
                                    :Runtime "148 min"
                                    :Series_Title "Inception"
                                    :Star1 "Leonardo DiCaprio"
                                    :Star2 "Joseph Gordon-Levitt"
                                    :Star3 "Elliot Page"
                                    :Star4 "Ken Watanabe"}))

(fact "Returns an empty list if no movie matches the title"
      (find-title "x") => '())

(fact "Returns a movie if user does not enter whole title"
      (find-title-part "Shawshank") => '({:Certificate "A"
                                          :Director "Frank Darabont"
                                          :Genre "Drama"
                                          :Gross "28,341,469"
                                          :IMDB_Rating "9.3"
                                          :Meta_score "80"
                                          :No_of_Votes "2343110"
                                          :Overview "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."
                                          :Poster_Link "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX67_CR0,0,67,98_AL_.jpg"
                                          :Released_Year "1994"
                                          :Runtime "142 min"
                                          :Series_Title "The Shawshank Redemption"
                                          :Star1 "Tim Robbins"
                                          :Star2 "Morgan Freeman"
                                          :Star3 "Bob Gunton"
                                          :Star4 "William Sadler"}))

(fact "Returns an empty list if no movie matches the title"
      (find-title-part "\n") => '())

(fact "Returns only number part of the string"
  (extract-duration "67 xxx") => "67")

(fact "Number is not in the string"
      (extract-duration "aa") => nil)

(fact "Returns :Stars key, containing Star1, Star2, Star3 and Star4"
      (add-stars {:Certificate "A"
                  :Director "Frank Darabont"
                  :Genre "Drama"
                  :Gross "28,341,469"
                  :IMDB_Rating "9.3"
                  :Meta_score "80"
                  :No_of_Votes "2343110"
                  :Overview "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."
                  :Poster_Link "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX67_CR0,0,67,98_AL_.jpg"
                  :Released_Year "1994"
                  :Runtime "142 min"
                  :Series_Title "The Shawshank Redemption"
                  :Star1 "Tim Robbins"
                  :Star2 "Morgan Freeman"
                  :Star3 "Bob Gunton"
                  :Star4 "William Sadler"}) => {:Certificate "A"
                                                      :Director "Frank Darabont"
                                                      :Genre "Drama"
                                                      :Gross "28,341,469"
                                                      :IMDB_Rating "9.3"
                                                      :Meta_score "80"
                                                      :No_of_Votes "2343110"
                                                      :Overview "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."
                                                      :Poster_Link "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX67_CR0,0,67,98_AL_.jpg"
                                                      :Released_Year "1994"
                                                      :Runtime "142 min"
                                                      :Series_Title "The Shawshank Redemption"
                                                      :Star1 "Tim Robbins"
                                                      :Star2 "Morgan Freeman"
                                                      :Star3 "Bob Gunton"
                                                      :Star4 "William Sadler"
                                                      :Stars "Tim Robbins, Morgan Freeman, Bob Gunton, William Sadler"})

(fact "Returns nil if forwarded movie is empty"
  (add-stars "") => nil)

(fact "Returns string in double format decreased by 5"
  (adapt-rating "9") => "4.0")

(fact "Returns nil if input is not a number"
      (adapt-rating "aa") => nil)

(fact "Returns text without year section"
      (clean-title "Avatar (2000)") => "Avatar")

(fact "Returns same is string if there are no brackets with year"
  (clean-title "Avatar 1") => "Avatar 1")

(fact "Returns true if one string misses year section"
  (compare-movies "Toy" "Toy (2000)") => true)

(fact "Returns false in other cases"
      (compare-movies nil "Toy (2000)") => false)

(fact "Returns desired film for existing title"
  (find-same-movie "Pulp Fiction") => '({:genres "Comedy|Crime|Drama|Thriller"
                                        :movieId "296"
                                        :title "Pulp Fiction (1994)"}))
(fact "Returns empty list if movie does not exist"
      (find-same-movie "") => '())

(fact "Returns wanted id"
  (find-movieId "Pulp Fiction") => ["296"])

(fact "Returns empty array for nonexistent movie"
      (find-movieId "") => [])
(fact "Returns empty list for nonexistent movie"
  (find-users "") => '())

(fact "Returns expecting list for this input"
  (same-rating "Pulp Fiction" "4.0") =>
  '({:userId "8", :movieId "296", :rating "4.0", :timestamp "839463422"}
   {:userId "15", :movieId "296", :rating "4.0", :timestamp "1510571877"}
   {:userId "18", :movieId "296", :rating "4.0", :timestamp "1455050745"}
   {:userId "24", :movieId "296", :rating "4.0", :timestamp "1458942020"}
   {:userId "26", :movieId "296", :rating "4.0", :timestamp "836950245"}
   {:userId "50", :movieId "296", :rating "4.0", :timestamp "1500573696"}
   {:userId "56", :movieId "296", :rating "4.0", :timestamp "835799034"}
   {:userId "78", :movieId "296", :rating "4.0", :timestamp "1252574962"}
   {:userId "84", :movieId "296", :rating "4.0", :timestamp "857653593"}
   {:userId "94", :movieId "296", :rating "4.0", :timestamp "843406616"}))

(fact "Returns empty list for missing field"
      (same-rating "Pulp Fiction" "") => '())

(fact "Returns expected value for entered id"
  (user-movies-id "2") =>
  '({:movieId "60756" :rating "5.0" :timestamp "1445714980" :userId "2"}
    {:movieId "80906" :rating "5.0" :timestamp "1445715172" :userId "2"}
    {:movieId "89774" :rating "5.0" :timestamp "1445715189" :userId "2"}
    {:movieId "106782" :rating "5.0" :timestamp "1445714966" :userId "2"}
    {:movieId "122882" :rating "5.0" :timestamp "1445715272" :userId "2"}
    {:movieId "131724" :rating "5.0" :timestamp "1445714851" :userId "2"}))

(fact "Returns empty list if user does not exist"
      (user-movies-id "xx") => '())

(fact "Returns empty list for missing field"
  (users-movies "" "4.0") => ())

(fact "Returns expected value for entered id"
  (find-movie-by-id "1") => {:genres "Adventure|Animation|Children|Comedy|Fantasy"
                                 :movieId "1"
                                 :title "Toy Story (1995)"})

(fact "Returns nil for nonexistent movie"
      (find-movie-by-id "") => nil)

(fact "Returns expected result for the input"
  (split-genre "Drama, Comedy") => ["Drama" "Comedy"])

(fact "Returns array of string same as input, if it is not correct
        (no comma)"
      (split-genre "Drama. Comedy") => ["Drama. Comedy"])

(fact "Returns empty list for wrong input"
  (ratings-to-movies "xxx" "vvv") => '()
      (take-same-genre "" "" "") => '()
      (titles-in-movies-imdb "" "" "") => '()
      (find-movie-in-movies-imdb "" "" "") => '())

(fact "Returns reversed array"
  (reverse-array ["a" "b" "c"]) => ["c" "b" "a"])

(fact "Returns same array"
      (reverse-array ["a"]) => ["a"]
      (reverse-array []) => [])

(fact "Returns empty array if nil is forwarded"
      (reverse-array nil) => [])

