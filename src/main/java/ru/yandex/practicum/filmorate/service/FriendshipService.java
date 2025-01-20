package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.FriendshipDbStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FriendshipService {
    private final FriendshipDbStorage fsStorage;

    public void addFriend(Long userId, Long friendId) {
        fsStorage.updateFriendship(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        fsStorage.deleteFriendship(userId, friendId);
    }

    public Set<User> getFriends(Long userId) {
        return fsStorage.getFriends(userId);
    }

    public Set<User> getMutualFriends(Long userId, Long friendId) {
        Set<User> userFriends = fsStorage.getFriends(userId);
        Set<User> friendFriends = fsStorage.getFriends(friendId);
        return userFriends
                .stream()
                .filter(friendFriends::contains)
                .collect(Collectors.toSet());
    }
}
