package ru.yandex.practicum.filmorate.storage.dal.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FriendshipRowMapper implements RowMapper<Friendship> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friendship friendship = new Friendship(rs.getLong("user_id"), rs.getLong("friend_id"));
        String statusSql = "SELECT status FROM status WHERE id = ?";
        String statusName = jdbcTemplate.queryForObject(statusSql, String.class,
                rs.getLong("friend_status"));
        friendship.setFriendStatus(statusName);
        return friendship;
    }
}



