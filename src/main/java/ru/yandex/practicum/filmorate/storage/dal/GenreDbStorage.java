package ru.yandex.practicum.filmorate.storage.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String INSERT_GENRE_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_GENRE_QUERY = "DELETE FROM genres WHERE film_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    public Genre getGenre(Long genreId) {
        Optional<Genre> genre = findOne(FIND_BY_ID_QUERY, genreId);
        if (genre.isPresent()) {
            return genre.get();
        } else {
            log.error("Жанр с айди: {} - не найден.", genreId);
            throw new NotFoundException("Жанр с айди: " + genreId + " - не найден.");
        }
    }

    public void addFilmGenre(Set<Genre> genres, Long filmId) {
        genres.forEach(g -> insert(INSERT_GENRE_QUERY, filmId, g.getId()));
    }

    public void deleteFilmGenre(Long filmId) {
        delete(DELETE_GENRE_QUERY, filmId);
    }
}
