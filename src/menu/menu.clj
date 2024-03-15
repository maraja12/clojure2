(ns menu.menu)
(require '[user.user :refer :all]
         '[blogic.blogic :refer :all]
         '[db.db :refer :all])

(defn start-menu
  []
  (println (str "
####################################################################
Hello film lover "
                ;(:USERS/USERNAME signed-user)
                "! Welcome to \"MOVIELAND\",
your personal assistant in finding right movie,
based on your preferences.

If you want me to recommend you movie(s), relying on your previous choices,
you need to sign up. Otherwise, I can still find movie(s) based on
genre, release year, rating, duration, director or actor you entered.

Please, choose the way you would like to use the app.
Type one of the following numbers...

1. with signing up
2. without signing up

Press any other key if you want to exit the app.
####################################################################
  ")))

(defn menu-no-login
  []
  (println (str "

********************************************************************
I can find movie(s) based on:
genre, director, rating, duration, director and actor.\n"))
  (first-choice)
  )

(defn menu-login
  []
  (println (str "

********************************************************************

Please, choose between two options:

1. I do not have an account.
2. I have an account.

")))

(defn menu-logged
  ([]
   (let [user (login-input)]
     (menu-logged user)
     ;user
     )
   )
  ([logged-user]
   (println (str "
Welcome " (:USERS/USERNAME logged-user) ", I can suggest movie(s) based on...

1. genre, year, rating, duration, director and actor
2. your history
"
              ))
   logged-user
   ))

;(menu-logged)

(defn handle-menu-login
  []
  (menu-login)
  (let [account (get-value "your choice:")]
    (case account
      "1" (do
            (registration-input)
            (println "Now you are able to sign in!\n")
            (let [logged-user (menu-logged)
                  choice (get-value "your choice:")]
              (case choice
                "1" (do
                      (println (str "hello " (:USERS/USERNAME logged-user)))
                      (first-choice))
                "2"
                )))
      "2" (do
            (let [logged-user (menu-logged)
                  choice (get-value "your choice:")]
              (case choice
                "1" (do
                      (println (str "hello " (:USERS/USERNAME logged-user)))
                      (first-choice))
                "2"
                ))))))


(defn user-choice-main-menu
  []
  (let [choice (get-value "your choice:")]
    (case choice
      "1" (do
            (handle-menu-login)
            )
      "2" (menu-no-login)
      "END")))
(defn start
  []
  (start-menu)
  (when-not (= "END" (user-choice-main-menu))
    ;(if (= (user-choice-main-menu) "1")
    ;
    ;  )
    (start)))
