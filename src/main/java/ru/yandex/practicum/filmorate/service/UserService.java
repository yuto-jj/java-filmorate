package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private Long id = 0L;
    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers().values();
    }

    public User createUser(User user) {
        validate(user);
        id++;
        user.setId(id);
        log.debug("Установлен айди пользователя: {}", user.getId());
        userStorage.addUser(user);
        log.debug("Пользователь добавлен в список пользователей");
        return user;
    }

    public User updateUser(User user) {
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(user.getId()) || users.get(user.getId()) == null) {
            log.error("Пользователь с указанным айди не найден");
            throw new NotFoundException("Пользователь с указанным айди не найден");
        }

        validate(user);
        User oldUser = users.get(user.getId());
        if (!user.getEmail().equals(oldUser.getEmail())) {
            userStorage.removeEmail(oldUser.getEmail());
            userStorage.addEmail(user.getEmail());
        }

        oldUser.setEmail(user.getEmail());
        oldUser.setName(user.getName());
        oldUser.setLogin(user.getLogin());
        oldUser.setBirthday(user.getBirthday());
        return oldUser;
    }

    public User addFriend(Long userId, Long friendId) {
        List<User> users = addOrRemoveFriendValidate(userId, friendId);
        User user1 = users.get(0);
        User user2 = users.get(1);
        user1.addFriend(friendId);
        user2.addFriend(userId);
        return user1;
    }

    public User removeFriend(Long userId, Long friendId) {
        List<User> users = addOrRemoveFriendValidate(userId, friendId);
        User user1 = users.get(0);
        User user2 = users.get(1);
        user1.removeFriend(friendId);
        user2.removeFriend(userId);
        return user1;
    }

    public Set<User> getFriends(Long userId) {
        Map<Long, User> users = userStorage.getUsers();
        if (userId == null || !users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с айди: " + userId + " - не найден.");
        }

        return users.get(userId).getFriends()
                .stream()
                .map(users::get)
                .collect(Collectors.toSet());
    }

    public Set<User> getMutualFriends(Long userId, Long friendId) {
        List<User> validUsers = addOrRemoveFriendValidate(userId, friendId);
        User user1 = validUsers.get(0);
        User user2 = validUsers.get(1);
        Map<Long, User> users = userStorage.getUsers();
        return user1.getFriends()
                .stream()
                .filter(u -> user2.getFriends().contains(u))
                .map(users::get)
                .collect(Collectors.toSet());
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        if (!userStorage.addEmail(user.getEmail())) {
            log.error("Пользователь с таким емеил уже существует");
            throw new ValidationException("Пользователь с таким емеил уже существует");
        }
        log.debug("Емеил пользователя добавлен в список");

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя изменено на логин: {}", user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private List<User> addOrRemoveFriendValidate(Long userId, Long friendId) {
        Map<Long, User> users = userStorage.getUsers();
        List<User> validUsers = new ArrayList<>();

        if (userId == null || !users.containsKey(userId)) {
            log.error("Пользователь с айди: {} - не найден.", userId);
            throw new NotFoundException("Пользователь с айди: " + userId + " - не найден.");
        }
        validUsers.add(users.get(userId));

        if (friendId == null || !users.containsKey(friendId)) {
            log.error("Пользователь с айди: {} - не найден.", friendId);
            throw new NotFoundException("Пользователь с айди: " + friendId + " - не найден.");
        }
        validUsers.add(users.get(friendId));

        return validUsers;
    }
}
