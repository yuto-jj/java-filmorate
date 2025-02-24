package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.LinkedHashSet;

public class FilmMapper {

    public static Film mapToFilm(NewFilmRequest r) {
        return Film.builder()
                .name(r.getName())
                .description(r.getDescription())
                .releaseDate(r.getReleaseDate())
                .duration(r.getDuration())
                .mpa(r.getMpa())
                .genres(new LinkedHashSet<>(r.getGenres()))
                .build();
    }

    public static Film mapToFilm(UpdateFilmRequest r) {
        return Film.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .releaseDate(r.getReleaseDate())
                .duration(r.getDuration())
                .mpa(r.getMpa())
                .genres(new LinkedHashSet<>(r.getGenres()))
                .build();
    }
}
