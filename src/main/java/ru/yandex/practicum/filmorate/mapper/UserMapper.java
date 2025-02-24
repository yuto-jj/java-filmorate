package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {

    public static User mapToUser(NewUserRequest r) {
        return User.builder()
                .email(r.getEmail())
                .login(r.getLogin())
                .name(r.getName())
                .birthday(r.getBirthday())
                .build();
    }

    public static User mapToUser(UpdateUserRequest r) {
        return User.builder()
                .id(r.getId())
                .email(r.getEmail())
                .login(r.getLogin())
                .name(r.getName())
                .birthday(r.getBirthday())
                .build();
    }
}
