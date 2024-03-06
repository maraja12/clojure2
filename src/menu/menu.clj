(ns menu.menu)
(require '[user.user :refer :all]
         '[blogic.blogic :refer :all])

(defn start-menu
  []
  (println (str "
  ####################################################################
  Hello film lover! Welcome to \"MOVIELAND\",
  your personal assistant in finding right movie,
  based on your preferences.

  Please, choose the way you would like me to find the movies.
  Type one of the following numbers...

  I can find movie(s) based on...
  1. genre, director, rating, duration, director, actor
  2. your feelings, occasion, ...
  3. title

  Type \"end\" if you want to exit the app.
  ####################################################################
  "))
  )

(defn user-choice
  []
  (let [choice (get-value "your choice")]
    (case choice
      "1" (first-choice)
      "end")
    ))
(defn start
  []
  (start-menu)
  (when-not (= "end" (user-choice))
    (start)))