package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long durationOfMinutes;

    public void setDuration(long durationOfMinutes) {
        this.durationOfMinutes = durationOfMinutes;
    }

    public long getDuration() {
        return durationOfMinutes;
    }
}
