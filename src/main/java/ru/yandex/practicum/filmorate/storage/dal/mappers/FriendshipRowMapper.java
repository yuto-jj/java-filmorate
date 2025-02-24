package ru.yandex.practicum.filmorate.storage.dal.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FriendshipRowMapper implements RowMapper<Friendship> {

    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Friendship(rs.getLong("user_id"), rs.getLong("friend_id"));
    }
}



