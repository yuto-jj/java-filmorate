package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre getGenre(Integer genreId) {
        return genreStorage.getGenre(genreId);
    }

    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }
}
