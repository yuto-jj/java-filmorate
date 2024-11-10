package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Getter
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    public void addEmail(String email) {
        emails.add(email);
    }

    public void removeEmail(String email) {
        emails.remove(email);
    }

    public boolean containsEmail(String email) {
        return emails.contains(email);
    }

    public void addUser(User user) {
        emails.add(user.getEmail());
        users.put(user.getId(), user);
    }

    public void updateUser(User user) {
        emails.remove(users.get(user.getId()).getEmail());
        emails.add(user.getEmail());
        users.put(user.getId(), user);
    }

    public void removeUser(User user) {
        emails.remove(user.getEmail());
        users.remove(user.getId());
    }

    public User getUser(Long id) {
        if (id == null || !users.containsKey(id) || users.get(id) == null) {
            log.error("Пользователь с айди: {} - не найден.", id);
            throw new NotFoundException("Пользователь с айди: " + id + " - не найден.");
        }
        return users.get(id);
    }
}
