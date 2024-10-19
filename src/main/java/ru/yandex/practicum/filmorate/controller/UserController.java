package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = (Logger) LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        if (!emails.add(user.getEmail())) {
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

        user.setId(nextUserId());
        log.debug("Установлен айди пользователя: {}", user.getId());
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен в список пользователей");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с указанным айди не найден");
            throw new ValidationException("Пользователь с указанным айди не найден");
        }

        User oldUser = users.get(user.getId());
        User buildUser = User.builder()
                .id(oldUser.getId())
                .name(oldUser.getName())
                .email(oldUser.getEmail())
                .login(oldUser.getLogin())
                .birthday(oldUser.getBirthday())
                .build();

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (!user.getEmail().contains("@") || emails.contains(user.getEmail())) {
                log.error("Электронная почта должна содержать символ @, " +
                        "либо пользователь с таким емейл уже существует");
                throw new ValidationException("Электронная почта должна содержать символ @, " +
                        "либо пользователь с таким емейл уже существует");
            }
            buildUser.setEmail(user.getEmail());
            log.debug("Установлен новый емейл: {}", buildUser.getEmail());
        }

        if (user.getLogin() != null && !user.getLogin().isEmpty()) {
            if (user.getLogin().contains(" ")) {
                log.error("Логин не может содержать пробелы");
                throw new ValidationException("Логин не может содержать пробелы");
            }
            buildUser.setLogin(user.getLogin());
            log.debug("Установлен новый логин: {}", buildUser.getLogin());
        }

        if (user.getName() != null && !user.getName().isEmpty()) {
            buildUser.setName(user.getName());
            log.debug("Установлено новое имя: {}", buildUser.getName());
        }

        if (user.getBirthday() != null) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Дата рождения не может быть в будущем");
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
            buildUser.setBirthday(user.getBirthday());
            log.debug("Установлена новая дата рождения: {}", buildUser.getBirthday());
        }

        if (!buildUser.getEmail().equals(oldUser.getEmail())) {
            emails.remove(oldUser.getEmail());
            emails.add(buildUser.getEmail());
        }

        users.put(buildUser.getId(), buildUser);
        return buildUser;
    }



    private Long nextUserId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
