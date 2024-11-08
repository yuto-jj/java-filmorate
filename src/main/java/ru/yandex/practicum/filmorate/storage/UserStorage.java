package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.Set;

public interface UserStorage {

    Map<Long, User> getUsers();

    Set<String> getEmails();

    boolean addEmail(String email);

    void removeEmail(String email);

    void addUser(User user);

    void updateUser(User user);

    void removeUser(User user);
}
