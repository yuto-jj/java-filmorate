package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Map<Long, Film> getFilms();

    void addFilm(Film film);

    void updateFilm(Film film);

    void deleteFilm(Film film);

    Film getFilm(Long id);
}
