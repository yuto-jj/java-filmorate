package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;
import java.util.Set;

public interface FriendshipStorage {
    void updateFriendship(Long userId, Long friendId);

    Optional<Friendship> getFriendship(Long userId, Long friendId);

    Set<User> getFriends(Long userId);

    void deleteFriendship(Long userId, Long friendId);
}
