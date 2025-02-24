package ru.yandex.practicum.filmorate.exception;

public class BadRequestExeption extends RuntimeException {
    public BadRequestExeption(final String message) {
        super(message);
    }
}
