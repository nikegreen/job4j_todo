package ru.job4j.todo.store;

import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import ru.job4j.todo.model.Priority;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PriorityStore {
    private final CrudRepository crudRepository;

    /**
     * Список всех приоритетов отсортированных по id.
     * @return список всех задач.
     */
    public List<Priority> findAll() {
        return crudRepository.query("FROM Priority t ORDER BY t.id ASC", Priority.class);
    }

    /**
     * Найти приоритет по ID
     * @return приоритет.
     */
    public Optional<Priority> findById(int id) {
        return crudRepository.optional(
                "FROM Priority WHERE id = :fId",
                Priority.class,
                Map.of("fId", id)
        );
    }
}
