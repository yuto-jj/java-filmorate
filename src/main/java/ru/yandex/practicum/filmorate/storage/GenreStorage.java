package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    List<Genre> getGenres();

    Genre getGenre(Long genreId);

    void addFilmGenre(Set<Genre> genres, Long filmId);

    void deleteFilmGenre(Long filmId);
}
