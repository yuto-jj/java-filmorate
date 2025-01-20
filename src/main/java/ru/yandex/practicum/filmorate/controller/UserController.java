package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private final UserService userService;
    private final FriendshipService friendshipService;

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@RequestBody NewUserRequest r) {
        return userService.createUser(r);
    }

    @PutMapping
    public User updateUser(@RequestBody UpdateUserRequest r) {
        return userService.updateUser(r);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(value = "id") Long id,
                          @PathVariable(value = "friendId") Long friendId) {
        friendshipService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        friendshipService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable Long id) {
        return friendshipService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getMutualFriends(@PathVariable Long id,
                                      @PathVariable Long otherId) {
        return friendshipService.getMutualFriends(id, otherId);
    }
}
