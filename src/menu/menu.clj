(ns menu.menu)
(require '[user.user :refer :all]
         '[blogic.blogic :refer :all])

(defn start-menu
  []
  (println (str "Hello film lover! Welcome to \"MOVIELAND\",
  your personal assistant in finding right movie,
  based on your preferences.

  Please, choose the way you would like me to find the movies.
  Type one of the following numbers...

  I can find movie(s) based on...
  1. genre, director, rating, ...
  2. your feelings, occasion, ...

  Type \"end\" if you want to exit the app.
  "))
  )

(defn user-choice
  []
  (let [choice (get-value "your choice: ")]
    (case choice
      "1" (try-with-x)
      "end")
    ))
(defn start
  []
  (start-menu)
  (when-not (= "end" (user-choice))
    (start)))