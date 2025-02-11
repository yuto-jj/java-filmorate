package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import ru.yandex.practicum.filmorate.FilmorateApplicationTests;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
public class FilmControllerTest extends FilmorateApplicationTests {
    private Film film1;
    private Film film2;
    private String filmJson1;
    private String filmJson2;

    @BeforeEach
    void beforeEach() throws JsonProcessingException {
        film1 = Film.builder()
                .id(2L)
                .name("Движение вверх")
                .description("Фильм о баскетболе")
                .releaseDate(LocalDate.of(2015, 10, 12))
                .duration(120)
                .mpa(new Mpa(1))
                .genres(Set.of(new Genre(1)))
                .build();
        filmJson1 = objectMapper.writeValueAsString(film1);

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
        filmJson2 = objectMapper.writeValueAsString(film2);
    }

    @Test
    void postFilm() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(film1.getName()))
                .andExpect(jsonPath("$.description").value(film1.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film1.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film1.getDuration()))
                .andExpect(jsonPath("$.mpa.id").value(1))
                .andExpect(jsonPath("$.mpa.name").value("G"))
                .andExpect(jsonPath("$.genres[0].id").value(1))
                .andExpect(jsonPath("$.genres[0].name").value("Комедия"));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value(film2.getName()))
                .andExpect(jsonPath("$.description").value(film2.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film2.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film2.getDuration()))
                .andExpect(jsonPath("$.mpa.id").value(5))
                .andExpect(jsonPath("$.mpa.name").value("NC-17"))
                .andExpect(jsonPath("$.genres[0].id").value(3))
                .andExpect(jsonPath("$.genres[0].name").value("Мультфильм"))
                .andExpect(jsonPath("$.genres[1].id").value(2))
                .andExpect(jsonPath("$.genres[1].name").value("Драма"));

        film1.setName("");
        String badJson1 = objectMapper.writeValueAsString(film1);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson1))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        film1.setName("Движение вверх");
        film1.setDescription("Есть победы, которые меняют ход истории." +
                " Победы духа, победы страны, победы всего мира." +
                " Таким триумфом стали легендарные «три секунды» -" +
                " выигрыш сборной СССР по баскетболу на роковой мюнхенской Олимпиаде 1972 г. " +
                "Впервые за 36 лет была повержена «непобедимая» команда США." +
                " Никто даже помыслить не мог о том, что это возможно – " +
                "обыграть великолепных непогрешимых американцев на Олимпийских играх! " +
                "Никто, кроме советских баскетболистов (русских и грузин, украинцев и казахов, белорусов и литовцев)");
        String badJson2 = objectMapper.writeValueAsString(film1);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson2))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        film1.setDescription("Фильм о баскетболе");
        film1.setReleaseDate(LocalDate.of(1700, 1, 1));
        String badJson3 = objectMapper.writeValueAsString(film1);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson3))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        film1.setReleaseDate(LocalDate.of(1900, 1, 1));
        film1.setDuration(-10);
        String badJson4 = objectMapper.writeValueAsString(film1);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson4))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        film1.setDuration(30);
        film1.setMpa(new Mpa(6));
        String badJson5 = objectMapper.writeValueAsString(film1);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson5))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isNotFound());

        film1.setMpa(new Mpa(1));
        film1.setGenres(Set.of(new Genre(7)));
        String badJson6 = objectMapper.writeValueAsString(film1);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson6))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isNotFound());
    }

    @Test
    void getFilms() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filmJson1));

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filmJson2));

        mockMvc.perform(get("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(film1.getName()))
                .andExpect(jsonPath("$[0].description").value(film1.getDescription()))
                .andExpect(jsonPath("$[0].releaseDate").value(film1.getReleaseDate().toString()))
                .andExpect(jsonPath("$[0].duration").value(film1.getDuration()))
                .andExpect(jsonPath("$[0].mpa.id").value(1))
                .andExpect(jsonPath("$[0].mpa.name").value("G"))
                .andExpect(jsonPath("$[0].genres[0].id").value(1))
                .andExpect(jsonPath("$[0].genres[0].name").value("Комедия"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value(film2.getName()))
                .andExpect(jsonPath("$[1].description").value(film2.getDescription()))
                .andExpect(jsonPath("$[1].releaseDate").value(film2.getReleaseDate().toString()))
                .andExpect(jsonPath("$[1].duration").value(film2.getDuration()))
                .andExpect(jsonPath("$[1].mpa.id").value(5))
                .andExpect(jsonPath("$[1].mpa.name").value("NC-17"))
                .andExpect(jsonPath("$[1].genres[*].id").value(containsInAnyOrder(3, 2)))
                .andExpect(jsonPath("$[1].genres[*].name"
                ).value(containsInAnyOrder("Мультфильм", "Драма")));

    }

    @Test
    void putFilm() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filmJson1));

        Film film3 = Film.builder()
                .id(1L)
                .name("Новое движение вверх")
                .description("Новый фильм о баскетболе")
                .releaseDate(LocalDate.of(2024, 1, 2))
                .duration(60)
                .mpa(new Mpa(4))
                .genres(Set.of(new Genre(2), new Genre(5), (new Genre(6))))
                .build();
        String filmJson3 = objectMapper.writeValueAsString(film3);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(film3.getName()))
                .andExpect(jsonPath("$.description").value(film3.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film3.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film3.getDuration()))
                .andExpect(jsonPath("$.mpa.id").value(4))
                .andExpect(jsonPath("$.mpa.name").value("R"))
                .andExpect(jsonPath("$.genres[*].id").value(containsInAnyOrder(2, 5, 6)))
                .andExpect(jsonPath("$.genres[*].name")
                        .value(containsInAnyOrder("Документальный", "Драма", "Боевик")));

        film3.setId(7L);
        String badJson1 = objectMapper.writeValueAsString(film3);
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson1))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isInternalServerError());

        film3.setId(1L);
        film3.setDescription("Есть победы, которые меняют ход истории." +
                " Победы духа, победы страны, победы всего мира." +
                " Таким триумфом стали легендарные «три секунды» -" +
                " выигрыш сборной СССР по баскетболу на роковой мюнхенской Олимпиаде 1972 г. " +
                "Впервые за 36 лет была повержена «непобедимая» команда США." +
                " Никто даже помыслить не мог о том, что это возможно – " +
                "обыграть великолепных непогрешимых американцев на Олимпийских играх! " +
                "Никто, кроме советских баскетболистов (русских и грузин, украинцев и казахов, белорусов и литовцев)");
        String badJson2 = objectMapper.writeValueAsString(film3);
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson2))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        film3.setDescription("Фильм");
        film3.setReleaseDate(LocalDate.of(1700, 1, 2));
        String badJson3 = objectMapper.writeValueAsString(film3);
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson3))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());

        film3.setReleaseDate(LocalDate.of(1900, 1, 2));
        film3.setDuration(-60);
        String badJson4 = objectMapper.writeValueAsString(film3);
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson4))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(status().isBadRequest());
    }
}
