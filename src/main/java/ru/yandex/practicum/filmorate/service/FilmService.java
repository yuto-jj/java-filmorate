package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FilmService {

    private static final LocalDate BAD_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    public Film addFilm(NewFilmRequest r) {
        Film film = FilmMapper.mapToFilm(r);
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(@RequestBody UpdateFilmRequest r) {
        Film film = FilmMapper.mapToFilm(r);
        validateFilm(film);
        return filmStorage.updateFilm(film);
    }


    public Film addLike(Long filmId, Long userId) {
        Film film = addOrRemoveLikeValidate(filmId, userId);
        film.addLike(userId);
        filmStorage.addLike(filmId, userId);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = addOrRemoveLikeValidate(filmId, userId);
        film.removeLike(userId);
        filmStorage.removeLike(filmId, userId);
        return film;
    }

    public Set<Film> getTopTenFilms(int count) {
        Comparator<Film> comparator = Comparator.comparing((Film film) -> film.getLikes().size()).reversed();
        return filmStorage.getFilms()
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
        userStorage.getUser(userId);
        return filmStorage.getFilm(filmId);
    }
}
