package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    public boolean addEmail(String email) {
        return emails.add(email);
    }

    public void removeEmail(String email) {
        emails.remove(email);
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
}
