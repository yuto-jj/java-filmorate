package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Jacksonized
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @Builder.Default
    private Set<Long> friends = new HashSet<>();

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }
}
