package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FilmController.class);

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза должна быть не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }

        if (film.getDuration().isNegative()) {
            log.error("Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

        film.setId(nextFilmId());
        log.debug("Установлен айди фильма: {}", film.getId());
        films.put(film.getId(), film);
        log.debug("Фильм добавлен в список");
        return film;
    }

    @PutMapping Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Фильм с указанным айди не найден");
            throw new ValidationException("Фильм с указанным айди не найден");
        }

        Film oldFilm = films.get(film.getId());
        Film buildFilm = Film.builder()
                .id(oldFilm.getId())
                .name(oldFilm.getName())
                .description(oldFilm.getDescription())
                .releaseDate(oldFilm.getReleaseDate())
                .durationOfMinutes(oldFilm.getDuration().toMinutes())
                .build();

        if (film.getName() != null && !film.getName().isEmpty()) {
            buildFilm.setName(film.getName());
            log.debug("Установлено новое имя: {}", oldFilm.getName());
        }

        if (film.getDescription() != null && !film.getDescription().isEmpty()) {
            if (film.getDescription().length() > 200) {
                log.error("Максимальная длина описания - 200 символов");
                throw new ValidationException("Максимальная длина описания - 200 символов");
            }
            buildFilm.setDescription(film.getDescription());
            log.debug("Установлено новое описание: {}", oldFilm.getDescription());
        }

        if (film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 27))) {
                log.error("Дата релиза должна быть не раньше 28 декабря 1895 года");
                throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
            }
            buildFilm.setReleaseDate(film.getReleaseDate());
            log.debug("Установлена новая дата релиза: {}", oldFilm.getReleaseDate());
        }

        if (film.getDuration().toMinutes() != 0) {
            if (film.getDuration().isNegative()) {
                log.error("Продолжительность фильма должна быть положительным числом");
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }
            buildFilm.setDuration(film.getDuration());
            log.debug("Установлена новая длительность фильма: {}", oldFilm.getDuration());
        }

        films.put(buildFilm.getId(), buildFilm);
        return buildFilm;
    }

    private Long nextFilmId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
