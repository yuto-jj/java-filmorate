DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS mpas;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     email VARCHAR not null,
                                     login VARCHAR not null,
                                     name VARCHAR,
                                     birthday DATE,
                                     UNIQUE (email),
                                     UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS friends (
                                       user_id BIGINT,
                                       friend_id BIGINT,
                                       PRIMARY KEY (user_id, friend_id),
                                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                       FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS mpas (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR not null
);

CREATE TABLE IF NOT EXISTS films (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR not null,
                                     description VARCHAR,
                                     release_date DATE not null,
                                     duration INT not null,
                                     mpa INT not null,
                                     FOREIGN KEY (mpa) REFERENCES mpas(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR not null
);

CREATE TABLE IF NOT EXISTS film_genre (
                                          film_id BIGINT,
                                          genre_id INT,
                                          PRIMARY KEY (film_id, genre_id),
                                          FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
                                          FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
                                     film_id BIGINT,
                                     user_id BIGINT,
                                     PRIMARY KEY (film_id, user_id),
                                     FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
                                     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);