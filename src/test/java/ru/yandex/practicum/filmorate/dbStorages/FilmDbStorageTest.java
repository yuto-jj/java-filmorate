package ru.yandex.practicum.filmorate.dbStorages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.storage.dal.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class, MpaDbStorage.class, MpaRowMapper.class,
        GenreDbStorage.class, GenreRowMapper.class, UserDbStorage.class, UserRowMapper.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private Film film1;
    private Film film2;

    @BeforeEach
    void setUp() {
        film1 = Film.builder()
                .id(2L)
                .name("Движение вверх")
                .description("Фильм о баскетболе")
                .releaseDate(LocalDate.of(2015, 10, 12))
                .duration(120)
                .mpa(new Mpa(1))
                .genres(Set.of(new Genre(1)))
                .build();
        Set<Genre> genres = new LinkedHashSet<>();
        genres.add(new Genre(3));
        genres.add(new Genre(2));
        film2 = Film.builder()
                .id(11L)
                .name("Интерстеллар")
                .description("Фильм про космос")
                .releaseDate(LocalDate.of(2012, 3, 19))
                .duration(180)
                .mpa(new Mpa(5))
                .genres(genres)
                .build();
    }

    @Test
    @Order(1)
    public void addAndGetFilmTest() {
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        Film testFilm = filmStorage.getFilm(1L);
        assertThat(testFilm.getId()).isEqualTo(1L);
        assertThat(testFilm.getName()).isEqualTo(film1.getName());
        assertThat(testFilm.getDescription()).isEqualTo(film1.getDescription());
        assertThat(testFilm.getReleaseDate()).isEqualTo(film1.getReleaseDate());
        assertThat(testFilm.getDuration()).isEqualTo(film1.getDuration());
        assertThat(testFilm.getMpa()).isEqualTo(film1.getMpa());
        assertThat(testFilm.getGenres()).isEqualTo(film1.getGenres());
        Film testFilm2 = filmStorage.getFilm(2L);
        assertThat(testFilm2.getId()).isEqualTo(2L);
        assertThat(testFilm2.getName()).isEqualTo(film2.getName());
        assertThat(testFilm2.getDescription()).isEqualTo(film2.getDescription());
        assertThat(testFilm2.getReleaseDate()).isEqualTo(film2.getReleaseDate());
        assertThat(testFilm2.getDuration()).isEqualTo(film2.getDuration());
        assertThat(testFilm2.getMpa()).isEqualTo(film2.getMpa());
        assertThat(testFilm2.getGenres()).isEqualTo(film2.getGenres());
    }

    @Test
    @Order(2)
    public void updateFilmTest() {
        filmStorage.addFilm(film1);
        film1.setId(3L);
        film1.setName("Новое");
        film1.setDescription("Новое");
        film1.setReleaseDate(LocalDate.of(1990, 4, 1));
        film1.setDuration(37);
        film1.setMpa(new Mpa(3));
        film1.setGenres(Set.of(new Genre(5)));
        filmStorage.updateFilm(film1);
        Film updatedFilm = filmStorage.getFilm(3L);
        assertThat(updatedFilm.getId()).isEqualTo(3L);
        assertThat(updatedFilm.getName()).isEqualTo(film1.getName());
        assertThat(updatedFilm.getDescription()).isEqualTo(film1.getDescription());
        assertThat(updatedFilm.getReleaseDate()).isEqualTo(film1.getReleaseDate());
        assertThat(updatedFilm.getDuration()).isEqualTo(film1.getDuration());
        assertThat(updatedFilm.getMpa()).isEqualTo(film1.getMpa());
        assertThat(updatedFilm.getGenres()).isEqualTo(film1.getGenres());
    }

    @Test
    @Order(3)
    public void getFilmTest() {
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        Film testFilm = filmStorage.getFilm(4L);
        assertThat(testFilm.getId()).isEqualTo(4L);
        assertThat(testFilm.getName()).isEqualTo(film1.getName());
        assertThat(testFilm.getDescription()).isEqualTo(film1.getDescription());
        assertThat(testFilm.getReleaseDate()).isEqualTo(film1.getReleaseDate());
        assertThat(testFilm.getDuration()).isEqualTo(film1.getDuration());
        assertThat(testFilm.getMpa()).isEqualTo(film1.getMpa());
        assertThat(testFilm.getGenres()).isEqualTo(film1.getGenres());
        Film testFilm2 = filmStorage.getFilm(5L);
        assertThat(testFilm2.getId()).isEqualTo(5L);
        assertThat(testFilm2.getName()).isEqualTo(film2.getName());
        assertThat(testFilm2.getDescription()).isEqualTo(film2.getDescription());
        assertThat(testFilm2.getReleaseDate()).isEqualTo(film2.getReleaseDate());
        assertThat(testFilm2.getDuration()).isEqualTo(film2.getDuration());
        assertThat(testFilm2.getMpa()).isEqualTo(film2.getMpa());
        assertThat(testFilm2.getGenres()).isEqualTo(film2.getGenres());
    }

    @Test
    @Order(4)
    public void getFilmsTest() {
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        Film film3 = Film.builder()
                .name("3 фильм")
                .description("3 описание")
                .releaseDate(LocalDate.of(1970, 2, 6))
                .duration(98)
                .mpa(new Mpa(5))
                .genres(Set.of(new Genre(3)))
                .build();
        filmStorage.addFilm(film3);
        List<Film> films = filmStorage.getFilms();
        assertThat(films).hasSize(3);
        assertThat(films).usingRecursiveFieldByFieldElementComparator().contains(film1);
        assertThat(films).usingRecursiveFieldByFieldElementComparator().contains(film2);
        assertThat(films).usingRecursiveFieldByFieldElementComparator().contains(film3);
        filmStorage.deleteFilm(film3);
    }

    @Test
    @Order(5)
    public void addAndRemoveLikeTest() {
        filmStorage.addFilm(film1);
        User user = User.builder()
                .name("ff")
                .email("ff@mail.ru")
                .birthday(LocalDate.of(2001, 12, 1))
                .login("ff223")
                .build();
        userStorage.addUser(user);
        filmStorage.addLike(film1.getId(), user.getId());
        Film testFilm = filmStorage.getFilm(film1.getId());
        assertThat(testFilm.getLikes().contains(user.getId())).isTrue();

        filmStorage.removeLike(film1.getId(), user.getId());
        Film testFilm2 = filmStorage.getFilm(film1.getId());
        assertThat(testFilm2.getLikes().contains(user.getId())).isFalse();
    }
}
