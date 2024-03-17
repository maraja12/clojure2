# MOVIELAND

MOVIELAND is Clojure app designed to help users and save ther time while looking for perfect movie they would like to watch.

This app is working both with csv files and h2 database. CSV files were downloaded from https://www.kaggle.com/, where
I have found one file with top 1000 movies (imdb_top_1000.csv) and two paired files with user ratings for specific movies 
(movies.csv, raitings.csv). Raitings.csv file contains ratings from 100 users. 

I had difficulties in finding dataset with same
movie titles as in imdb_top_1000.csv. I could have only used movies.csv file, instead of imdb_top_1000.csv (because not all movie titles match), 
but it provides user much more details, such as director, actors or average rating from IMDB... I assume that is relevant information for every filmophile. 
Furthermore, it is more fun this way.
So, I went trough the datasets and tried to rename titles, and somehow improve the soultion... Now, almost all the titles match.


Database is used for recommendation algorithm. I will talk about that in more detail soon... Just to mention, in order to use
the app it is necessary to load the script, via (execute-script) in db.db namespace.


As some people do not like to register and to have their data collected, MOVIELAND offers two ways of using the app.

  ![Snimak ekrana (861)](https://github.com/maraja12/clojure2/assets/115712461/6d7d4548-688c-4cae-8ec2-c920f6f29c36)


* One of the options is usage without signing in. In that case, user has limited possibilities. However, finding perfect
  movie is still possible. User needs to fulfill required fields, such as genre, release year, duration of movie, average 
  rating from IMDB, and optional fields for director and actor. MOVIELAND will list movies based on entered criteria. After that,
  user will be redirected to main menu again, in case of changing mind.
  Movie list looks like this:
  
  ![Snimak ekrana (862)](https://github.com/maraja12/clojure2/assets/115712461/7e79ddaa-7fc2-436d-aa09-577f6c09d4ee)


* If user chooses other option, new menu apperas with registration and login part, depending on wheteher user have its
  account or not. If the answer is no, after successful registration, login part follows and the user is further on treated
  under that username, until he leaves the app.

  ![Snimak ekrana (865)](https://github.com/maraja12/clojure2/assets/115712461/f2198434-1be8-4ea3-8591-a7cb846de645)

  
  Usage of MOVIELAND with signig in gives much more possibilities. User can still find movie(s) to watch by filling in mentioned parametars.
  However, now it is possible to enter a title of the film, if desired, and rate it. The movie and rate will be stored in
  database under logged-in username.

  ![Snimak ekrana (864)](https://github.com/maraja12/clojure2/assets/115712461/bda1a8e2-4452-42a5-b9e3-63a2e5244765)


  Thanks to that, it is possible to use this **recommendation algorithm**. The algorithm takes last rated movie of user's history
  and search for other users who rated identical movie with the same rate. After that, it looks for all movies that found
  users ever rated and filter ones marked with highest rate (in movies.csv, it is five). Of all these films, the ones with
  compatible genre with stored movie are taken and represented to user. Alghoritam always starts from the last inserted movie in
  user's history, in order not to always present the same list of recommended films. If it is not possible to find recommended movies (in
  case no user rated that film, or rated with same mark, or film with that id does not exist at imdb dataset)
  from the last user's input, alghoritam searches for the previous one.
  
  To sum up, idea is to bind users with similar opinion, and to recommend movies from the same category (genre) that one of the users
  consider great and rated with highest mark.

  I hope it will be more clear after going trough the code and seeing how it works.
  I am very sorry I couldn't find movie datasets with all the same movie titles,
  but I explained previous why I chose this way. Fortunately, the majority is the same.






## License

Copyright © 2024 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
