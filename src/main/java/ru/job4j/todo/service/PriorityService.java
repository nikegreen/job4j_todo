package ru.job4j.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.store.PriorityStore;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriorityService {
    private final PriorityStore priorityStore;

    /**
     * Список всех приоритетов отсортированных по id.
     * @return список всех задач.
     */
    public List<Priority> findAll() {
        return priorityStore.findAll();
    }

    /**
     * Найти приоритет по ID
     * @return приоритет.
     */
    public Optional<Priority> findById(int id) {
        return priorityStore.findById(id);
    }
}
