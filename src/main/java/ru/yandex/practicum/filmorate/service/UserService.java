package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {

    private Long id = 0L;
    private final UserStorage userStorage;

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
        User oldUser = userStorage.getUser(user.getId());
        validate(user);

        if (!user.getEmail().equals(oldUser.getEmail())) {
            userStorage.removeEmail(oldUser.getEmail());
            userStorage.addEmail(user.getEmail());
        }

        userStorage.updateUser(user);
        return user;
    }

    public User addFriend(Long userId, Long friendId) {
        User user1 = userStorage.getUser(userId);
        User user2 = userStorage.getUser(friendId);
        user1.addFriend(friendId);
        user2.addFriend(userId);
        return user1;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user1 = userStorage.getUser(userId);
        User user2 = userStorage.getUser(friendId);
        user1.removeFriend(friendId);
        user2.removeFriend(userId);
        return user1;
    }

    public Set<User> getFriends(Long userId) {
        return userStorage.getUser(userId).getFriends()
                .stream()
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }

    public Set<User> getMutualFriends(Long userId, Long friendId) {
        User user1 = userStorage.getUser(userId);
        User user2 = userStorage.getUser(friendId);
        return user1.getFriends()
                .stream()
                .filter(u -> user2.getFriends().contains(u))
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }

    private void validate(User user) {
        String regex = "\\s";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher1 = pattern.matcher(user.getEmail());
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@") ||
        matcher1.find()) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        Map<Long, User> users = userStorage.getUsers();
        User oldUser = users.get(user.getId());
        if (userStorage.containsEmail(user.getEmail()) &&
                (oldUser == null || !oldUser.getEmail().equals(user.getEmail()))) {
            log.error("Пользователь с таким емеил уже существует");
            throw new ValidationException("Пользователь с таким емеил уже существует");
        }
        log.debug("Емеил пользователя добавлен в список");

        Matcher matcher2 = pattern.matcher(user.getLogin());
        if (user.getLogin() == null || user.getLogin().isEmpty() || matcher2.find()) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя изменено на логин: {}", user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
