DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS mpas;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     email VARCHAR,
                                     login VARCHAR,
                                     name VARCHAR,
                                     birthday DATE
);

CREATE TABLE IF NOT EXISTS status (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      status VARCHAR
);

CREATE TABLE IF NOT EXISTS friends (
                                       user_id INT,
                                       friend_id INT,
                                       friend_status INT,
                                       PRIMARY KEY (user_id, friend_id),
                                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ,
                                       FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE,
                                       FOREIGN KEY (friend_status) REFERENCES status(id)
);

CREATE TABLE IF NOT EXISTS mpas (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR
);

CREATE TABLE IF NOT EXISTS films (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR,
                                     description VARCHAR,
                                     release_date DATE,
                                     duration INT,
                                     mpa INT,
                                     FOREIGN KEY (mpa) REFERENCES mpas(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR
);

CREATE TABLE IF NOT EXISTS film_genre (
                                          film_id INT,
                                          genre_id INT,
                                          PRIMARY KEY (film_id, genre_id),
                                          FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
                                          FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
                                     film_id INT,
                                     user_id INT,
                                     PRIMARY KEY (film_id, user_id),
                                     FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
                                     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);