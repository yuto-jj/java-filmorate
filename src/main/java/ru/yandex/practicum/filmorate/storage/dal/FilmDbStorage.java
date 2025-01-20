package ru.yandex.practicum.filmorate.storage.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Primary
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private final GenreStorage genreStorage;

    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String INSERT_FILM_QUERY = "INSERT INTO films (name, description, release_date, " +
            "duration, rating) VALUES (?, ?, ?, ?, ?) returning id";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
            "duration = ?, mpa = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, GenreStorage genreStorage) {
        super(jdbc, mapper);
        this.genreStorage = genreStorage;
    }

    public List<Film> getFilms() {
        return findMany(FIND_ALL_QUERY);
    }

    public Film getFilm(Long filmId) {
        Optional<Film> film = findOne(FIND_BY_ID_QUERY, filmId);
        if (film.isPresent()) {
            return film.get();
        } else {
            log.error("Фильм с айди: {} - не найден.", filmId);
            throw new NotFoundException("Фильм с айди: " + filmId + " - не найден.");
        }
    }

    public Film addFilm(Film film) {
        long id = insert(
                INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        log.debug("Фильм добавлен в список");
        film.setId(id);
        log.debug("Установлен айди фильма: {}", film.getId());
        genreStorage.addFilmGenre(film.getGenre(), film.getId());
        return film;
    }

    public Film updateFilm(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        genreStorage.deleteFilmGenre(film.getId());
        genreStorage.addFilmGenre(film.getGenre(), film.getId());
        return film;
    }

    public void deleteFilm(Film film) {
        delete(DELETE_QUERY, film.getId());
    }

    public void addLike(Long filmId, Long userId) {
        insert(INSERT_LIKE_QUERY, filmId, userId);
        log.debug("В фильм с айди {} добавлен лайк пользователя с айди {}", filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        delete(DELETE_LIKE_QUERY, filmId, userId);
        log.debug("Из фильма с айди {} удален лайк пользователя с айди {}", filmId, userId);
    }
}
