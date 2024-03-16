(ns user.user
  (:require [ds.ds :refer :all]
            [blogic.blogic :refer :all]
            [validation.validation :refer :all]
            [db.db :refer :all]))

(defn get-value
  "Reading value entered by user"
  [x]
  (println (str "Please enter " x " " ))
  (let [value (read-line)]
    value))

(defn get-value-required-field
  "Reading value entered by user"
  [x val-fun]
  (println (str "Please enter " x  ))
  (let [value (read-line)]
    ;(println  "Your input: " \" value \")
    (if-not (val-fun value)
      (do
        (println "\u001b[31m"
                 "This is REQUIRED field. Try again..."
                 "\u001b[0m")
        (println)
        (get-value-required-field x val-fun))
      value)))

(defn get-value-required-field-num
  "Reading value entered by user"
  [x val-fun num-fun]
  (println (str "Please enter " x  ))
  (let [value (read-line)]
    ;(println  "Your input: " \" value \")
    (if-not (val-fun value)
      (do
        (println "\u001b[31m"
                 "This is REQUIRED field. Try again..."
                 "\u001b[0m")
        (println)
        (get-value-required-field-num x val-fun num-fun))
      (if-not (num-fun value)
        (do
          (println "\u001b[31m"
                   "Invalid input! This field requires a number."
                   "\u001b[0m")
          (println)
          (get-value-required-field-num x val-fun num-fun))
        value))))
(defn movie-by-title
  []
  "Finding a film by title user entered"
  (let [title (get-value "title of film you are interested in: ")
        movie (find-title title)]
    (if (not (empty? movie))
            (do
              (print-movie movie)
              (println "Have you liked it?")
              (let [rate (get-value-required-field "your rate for the movie. [5 - 10]" is-empty)]
                (assoc (first movie) :userRate rate)))
            nil)))
;(movie-by-title)

(defn first-choice
  "Finding films based on genre, year, rating, duration, actor, director"
  []
  (println "REQUIRED fields are marked with *")
  (println "If you want to skip OPTIONAL field, just press ENTER")
  (println)
  (let [year (get-value-required-field-num
               "year * :
# will find entered age or younger" is-empty check-int)
        genre (get-value-required-field
                "genre * :
(Film-Noir, Thriller, Horror, Action, Adventure, War, Drama,
Mystery, Crime, Comedy, Romance, Fantasy, Family, Music, ...)" is-empty)
        rating (get-value-required-field-num
                 "rating * :
# will find entered rate or higher" is-empty check-double)
        duration (get-value-required-field-num
                   "duration (min) * :
# will find entered duration or shorter" is-empty check-int)
        actor (get-value "actor (optional field):")
        director (get-value "director (optional field):")]
    (let [result (year-genre-rating-duration-actor-director
                   year genre rating duration actor director)]
      (if (empty? result)
        (println "Sorry, I have not found any movie based on your criteria...")
        (do
          (print-format result)
          result)))))
;(first-choice)
(defn save-movie
  [username movies]
  (if (not (empty? movies))
    (do
      (let [res (movie-by-title)
            title (:Series_Title res)
            genre (:Genre res)
            rate (:userRate res)]
        (if (not= res nil)
          (insert-history
            (get-connection) username title genre (adapt-rating rate)))))))

(defn registration-input
  []
  (let [username (get-value-required-field "username: " not-empty)
        password (get-value-required-field "password" not-empty)]
    (if (registration (get-connection) username password)
      (do
        (println "\nYou are successfully registered."))
      (do
        (println "\u001B[31m\"
                  User with this username already exists. Try again...
                  \"\\u001b[0m\"")
        (registration-input)))))
(defn login-input
  []
  (let [username (get-value-required-field "username: " not-empty)
        password (get-value-required-field "password: " not-empty)
        user(login-user (get-connection)
                        username password)]
    (if-not user
      (do
        (println "\u001b[31m"
                 "Incorrect credentials. Try again..."
                 "\u001b[0m")
        (println)
        (login-input))
      (do
        ;(println user)
        user))))
;(login-input)



