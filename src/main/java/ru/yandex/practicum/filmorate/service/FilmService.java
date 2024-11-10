package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private Long id = 0L;
    private static final LocalDate BAD_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms().values();
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        id++;
        film.setId(id);
        log.debug("Установлен айди фильма: {}", film.getId());
        filmStorage.addFilm(film);
        log.debug("Фильм добавлен в список");
        return film;
    }

    public Film updateFilm(@RequestBody Film film) {
        Map<Long, Film> films = filmStorage.getFilms();
        if (!films.containsKey(film.getId()) || films.get(film.getId()) == null) {
            log.error("Фильм с указанным айди не найден");
            throw new NotFoundException("Фильм с указанным айди не найден");
        }

        validateFilm(film);
        Film oldFilm = films.get(film.getId());
        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());

        return oldFilm;
    }


    public Film addLike(Long filmId, Long userId) {
        Film film = addOrRemoveLikeValidate(filmId, userId);
        film.addLike(userId);
        log.debug("В фильм с айди {} добавлен лайк пользователя с айди {}", film.getId(), userId);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = addOrRemoveLikeValidate(filmId, userId);
        film.removeLike(userId);
        log.debug("Из фильма с айди {} удален лайк пользователя с айди {}", film.getId(), userId);
        return film;
    }

    public Set<Film> getTopTenFilms(int count) {
        Comparator<Film> comparator = Comparator.comparing((Film film) -> film.getLikes().size()).reversed();
        return filmStorage.getFilms()
                .values()
                .stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }

        if (film.getReleaseDate().isBefore(BAD_DATE)) {
            log.error("Дата релиза должна быть не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }

        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    private Film addOrRemoveLikeValidate(Long filmId, Long userId) {
        Map<Long, Film> films = filmStorage.getFilms();
        Map<Long, User> users = userStorage.getUsers();

        if (!films.containsKey(filmId)) {
            log.error("Фильм с айди: {} - не найден.", filmId);
            throw new NotFoundException("Фильм с айди: " + filmId + " - не найден.");
        }

        if (!users.containsKey(userId)) {
            log.error("Пользователь с айди: {} - не найден.", userId);
            throw new NotFoundException("Пользователь с айди: " + userId + " - не найден.");
        }

        return films.get(filmId);
    }
}
