package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getUsers();

    boolean containsEmail(String email);

    User addUser(User user);

    User updateUser(User user);

    void removeUser(User user);

    User getUser(Long id);
}
