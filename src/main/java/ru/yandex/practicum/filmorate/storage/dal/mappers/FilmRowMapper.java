package ru.yandex.practicum.filmorate.storage.dal.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreRowMapper;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
    Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .build();

        Integer mpaId = rs.getInt("mpa");
        String mpaQuery = "SELECT name FROM mpas WHERE id = ?";
        String mpaName = jdbcTemplate.queryForObject(mpaQuery, String.class, mpaId);
        film.setMpa(new Mpa(mpaId, mpaName));

        String sql = "SELECT fg.id as filmGenreId, g.id as id, g.name as name " +
                "FROM film_genre fg " +
                "JOIN genres g ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ?";

        Long filmId = film.getId();
        Map<Long, Genre> genres = jdbcTemplate.query(sql, new Object[]{filmId},  rs2 -> {
            HashMap<Long, Genre> filmGenres = new HashMap<>();
            while (rs2.next()) {
                long filmGenreId = rs2.getLong("filmGenreId");
                Genre genre = genreRowMapper.mapRow(rs2, rs2.getRow());
                filmGenres.put(filmGenreId, genre);
            }
            return filmGenres;
        });

        Set<Genre> sortedGenres = genres.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        film.setGenres(sortedGenres);

        String likesQuery = "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id = ?;";
        List<Long> likes = jdbcTemplate.queryForList(likesQuery, Long.class, filmId);
        film.setLikes(new HashSet<>(likes));

        return film;
    }
}
