package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Friendship {
    @JsonIgnore
    private Long userId;
    private Long friendId;
    private Long friendStatusId = 1L;

    public Friendship(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public Friendship(Long userId, Long friendId, Long friendStatusId) {
        this.userId = userId;
        this.friendId = friendId;
        this.friendStatusId = friendStatusId;
    }
}
