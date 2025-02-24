package ru.yandex.practicum.filmorate.storage.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FriendshipDbStorage extends BaseDbStorage<Friendship> implements FriendshipStorage {
    private final UserStorage userStorage;

    private static final String FIND_ONE_QUERY = "SELECT * FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM friends WHERE user_id = ?";
    /*
    private static final String FIND_ALL_QUERY = "SELECT * FROM friends f1 " +
            "JOIN friends f2 ON f1.user_id = f2.friend_id AND f1.friend_id = f2.user_id " +
            "WHERE f1.user_id = ?";


    private static final String FIND_MUTUAL_QUERY = "SELECT f.user_id, f.friend_id FROM users u, friends f, " +
            "friends o WHERE u.user_id = f.friend_id AND u.user_id = o.friend_id AND f.user_id = ? AND o.user_id = ?";

     */

    private static final String FIND_MUTUAL_QUERY = "SELECT f.user_id, f.friend_id AS mutual_friend FROM friends f " +
            "INNER JOIN friends o ON f.friend_id = o.friend_id WHERE f.user_id = ? AND o.user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO friends(user_id, friend_id)" +
            "VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    public FriendshipDbStorage(JdbcTemplate jdbc, RowMapper<Friendship> mapper, UserStorage userStorage) {
        super(jdbc, mapper);
        this.userStorage = userStorage;
    }

    public void updateFriendship(Long userId, Long friendId) {
        validate(userId, friendId);
        update(INSERT_QUERY, userId, friendId);
    }

    public Optional<Friendship> getFriendship(Long userId, Long friendId) {
        return findOne(FIND_ONE_QUERY, userId, friendId);
    }

    public Set<User> getFriends(Long userId) {
        userStorage.getUser(userId);
        return findMany(FIND_ALL_QUERY, userId).stream().map(f -> userStorage.getUser(f.getFriendId()))
                .collect(Collectors.toSet());
    }

    public Set<User> getMutualFriends(Long userId, Long friendId) {
        return findMany(FIND_MUTUAL_QUERY, userId, friendId).stream().map(f -> userStorage.getUser(f.getFriendId()))
                .collect(Collectors.toSet());
    }

    public void deleteFriendship(Long userId, Long friendId) {
        validate(userId, friendId);
        delete(DELETE_QUERY, userId, friendId);
    }

    private void validate(Long userId, Long friendId) {
        userStorage.getUser(userId);
        userStorage.getUser(friendId);
    }
}
