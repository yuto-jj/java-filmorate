package ru.yandex.practicum.filmorate.storage.dal.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class GenreRowMapper implements RowMapper<Genre> {

    public Genre mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Genre genre = new Genre();
        genre.setId(rs.getLong("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}


