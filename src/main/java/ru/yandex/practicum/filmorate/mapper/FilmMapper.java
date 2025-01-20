package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {

    public static Film mapToFilm(NewFilmRequest r) {
        return Film.builder()
                .name(r.getName())
                .description(r.getDescription())
                .releaseDate(r.getReleaseDate())
                .duration(r.getDuration())
                .mpa(r.getMpa())
                .genre(r.getGenre())
                .build();
    }

    public static Film mapToFilm(UpdateFilmRequest r) {
        return Film.builder()
                .name(r.getName())
                .description(r.getDescription())
                .releaseDate(r.getReleaseDate())
                .duration(r.getDuration())
                .mpa(r.getMpa())
                .genre(r.getGenre())
                .build();
    }
}
