package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MpaService {

    private final MpaStorage mpaStorage;

    public Mpa getMpa(Integer mpaId) {
        return mpaStorage.getMpa(mpaId);
    }

    public List<Mpa> getMpas() {
        return mpaStorage.getMpas();
    }
}
