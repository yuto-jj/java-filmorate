package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long durationOfMinutes;

    public void setDuration(Duration duration) {
        durationOfMinutes = duration.toMinutes();
    }

    public Duration getDuration() {
        return Duration.ofMinutes(durationOfMinutes);
    }
}
