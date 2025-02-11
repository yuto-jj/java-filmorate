package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }

    public void deleteFilm(Film film) {
        films.remove(film.getId());
    }

    public Film getFilm(Long id) {
        if (id == null || !films.containsKey(id) || films.get(id) == null) {
            log.error("Фильм с айди: {} - не найден.", id);
            throw new NotFoundException("Фильм с айди: " + id + " - не найден.");
        }
        return films.get(id);
    }
}
