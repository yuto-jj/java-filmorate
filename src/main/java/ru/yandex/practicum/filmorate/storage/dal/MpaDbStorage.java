package ru.yandex.practicum.filmorate.storage.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class MpaDbStorage extends BaseDbStorage<Mpa> implements MpaStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpas";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpas WHERE id = ?";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> getMpas() {
        return findMany(FIND_ALL_QUERY);
    }

    public Mpa getMpa(Long mpaId) {
        Optional<Mpa> mpa = findOne(FIND_BY_ID_QUERY, mpaId);
        if (mpa.isPresent()) {
            return mpa.get();
        } else {
            log.error("Рейтинг с айди: {} - не найден.", mpaId);
            throw new NotFoundException("Рейтинг с айди: " + mpaId + " - не найден.");
        }
    }
}
