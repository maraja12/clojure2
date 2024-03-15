DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id bigint NOT NULL AUTO_INCREMENT,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE history (
  id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  title varchar(255) NOT NULL,
  genre varchar(255) NOT NULL,
  rating varchar(255) NOT NULL,
  PRIMARY KEY (id, user_id),
  CONSTRAINT history_fk FOREIGN KEY (user_id) REFERENCES users (id)
);




