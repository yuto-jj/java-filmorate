package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public Film addFilm(@RequestBody NewFilmRequest r) {
        return filmService.addFilm(r);
    }

    @PutMapping
    public Film updateFilm(@RequestBody UpdateFilmRequest r) {
        return filmService.updateFilm(r);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Set<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTopTenFilms(count);
    }
}
