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
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreRowMapper;

    @Override
    public Film mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .build();

        Long mpaId = rs.getLong("mpa");
        String mpaQuery = "SELECT name FROM mpa WHERE id = ?";
        String mpaName = jdbcTemplate.queryForObject(mpaQuery, String.class, mpaId);
        film.setMpa(new Mpa(mpaId, mpaName));


        Long filmId = film.getId();
        String genreQuery = "SELECT g.name " +
                            "FROM genre g " +
                            "JOIN film_genre fg ON g.id = fg.genre_id " +
                            "WHERE fg.film_id = ?;";
        List<Genre> genres = jdbcTemplate.query(genreQuery, genreRowMapper, filmId);
        film.setGenre(new HashSet<>(genres));

        String likesQuery = "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id = ?;";
        List<Long> likes = jdbcTemplate.queryForList(likesQuery, Long.class, filmId);
        film.setLikes(new HashSet<>(likes));

        return film;
    }
}
