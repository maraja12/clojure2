(ns db.db
  (:require [next.jdbc :as jdbc]))

(def database
  {:dbtype "h2"
   :dbname "movieland"
   :user "mr"
   :password ""
   :subprotocol "h2:tcp"
   :subname "//localhost:9000/movieland;webAllowOthers=true;
            AUTO_SERVER=TRUE"
   })

(def datasource (jdbc/get-datasource database))

(defn get-connection
  []
  (jdbc/get-connection datasource))

(defn execute-script
  []
  (let [script (slurp "resources/script/db.sql")]
    (jdbc/with-transaction [tx (get-connection)]
                           (try
                             (jdbc/execute! tx [script])
                             (catch Exception ex
                               (println (.getMessage ex)))))))

;(execute-script)

(defn find-user
  [conn username]
  (let [result (jdbc/execute!
                    conn
                    ["SELECT * FROM users
                     WHERE USERNAME = (?)" username])]
    (when result
      (first result))))
;(find-user (get-connection) "marija")

(defn registration
  [conn username password]
  (let [user (find-user conn username)]
    (if (empty? user)
      (do
        (jdbc/execute!
          conn ["INSERT INTO users (username, password) VALUES (?, ?)"
                username password])
        (.close conn)
        true)
      false
      )))

(defn login-user
  [conn username password]
  (let [user (find-user conn username)]
    (when (= password (:USERS/PASSWORD user))
      user)))

(defn insert-history
  [conn username title genre rating]
  (let [user-id (:USERS/ID (find-user conn username))]
    (jdbc/execute!
      conn ["INSERT INTO history (user_id, title, genre, rating)
    VALUES (?, ?, ?, ?)" user-id title genre rating])
    (.close conn)))

(defn get-history
  [conn]
  (jdbc/execute!
    conn
    ["SELECT h.user_id, h.title, h.genre, h.rating
    FROM history h JOIN users s ON (h.user_id = s.id)"]))
;(get-history (get-connection))

(defn get-history-userId
  [conn user-id]
  (jdbc/execute!
    conn
    ["SELECT h.user_id, h.title, h.genre, h.rating
    FROM history h JOIN users u ON (h.user_id = u.id)
    WHERE h.user_id = (?)" user-id]
    ))

;(get-history-userId (get-connection) 1)
;(:HISTORY/TITLE (first (get-history-userId (get-connection) 1)))
