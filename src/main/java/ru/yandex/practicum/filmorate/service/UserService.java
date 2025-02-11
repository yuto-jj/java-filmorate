package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(Long userId) {
        return userStorage.getUser(userId);
    }

    public User createUser(NewUserRequest r) {
        User user = UserMapper.mapToUser(r);
        validate(user);
        userStorage.addUser(user);
        log.debug("Пользователь добавлен в список пользователей");
        return user;
    }

    public User updateUser(UpdateUserRequest r) {
        User user = UserMapper.mapToUser(r);
        validate(user);
        userStorage.updateUser(user);
        return user;
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

        User oldUser = null;
        if (user.getId() != null) {
            oldUser = userStorage.getUser(user.getId());
        }
        if (userStorage.containsEmail(user.getEmail()) &&
                (oldUser == null || !oldUser.getEmail().equals(user.getEmail()))) {
            log.error("Пользователь с таким емеил уже существует");
            throw new ValidationException("Пользователь с таким емеил уже существует");
        }

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
