package ru.yandex.practicum.filmorate.storage.dal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FriendshipDbStorage extends BaseDbStorage<Friendship> implements FriendshipStorage {
    private final UserStorage userStorage;

    private static final String FIND_ONE_QUERY = "SELECT * FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM friends WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO friends(user_id, friend_id, friend_status)" +
            "VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE friends SET friend_status = ? + WHERE user_id = ? AND " +
            "friend_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    public FriendshipDbStorage(JdbcTemplate jdbc, RowMapper<Friendship> mapper, UserStorage userStorage) {
        super(jdbc, mapper);
        this.userStorage = userStorage;
    }

    public void updateFriendship(Long userId, Long friendId) {
        update(UPDATE_QUERY, userId, friendId, 2);
        boolean friendshipRequest = getFriendship(friendId, userId);
        if (friendshipRequest) {
            insert(INSERT_QUERY, userId, friendId, 2);
            update(UPDATE_QUERY, friendId, userId, 2);
        } else {
            insert(INSERT_QUERY, userId, friendId, 1);
        }
    }

    public boolean getFriendship(Long userId, Long friendId) {
        return findOne(FIND_ONE_QUERY, userId, friendId).isPresent();

    }

    public Set<Friendship> getFriendships(Long userId) {
        return new HashSet<>(findMany(FIND_ALL_QUERY, userId));
    }

    public Set<User> getFriends(Long userId) {
        return findMany(FIND_ALL_QUERY, userId).stream().map(f -> userStorage.getUser(f.getFriendId()))
                .collect(Collectors.toSet());
    }

    public void deleteFriendship(Long userId, Long friendId) {
        delete(DELETE_QUERY, userId, friendId);
    }
}
