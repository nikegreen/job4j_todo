package ru.job4j.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskStore;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskStore taskStore;

    /**
     * Сохранить в базе.
     * @param task задача.
     * @return задача с новым id.
     */
    public Optional<Task> create(Task task) {
        return taskStore.create(task);
    }

    /**
     * Обновить в базе задач.
     * @param task задача.
     */
    public boolean update(Task task) {
        return taskStore.update(task);
    }

    /**
     * Удалить задачу по ID.
     * @param taskId ID
     */
    public boolean delete(int taskId) {
        return taskStore.delete(taskId);
    }

    /**
     * Список всех задач отсортированных по id.
     * @return список всех задач.
     */
    public List<Task> findAll() {
        return taskStore.findAll();
    }

    /**
     * Найти задачу по ID
     * @return задача.
     */
    public Optional<Task> findById(int taskId) {
        return taskStore.findById(taskId);
    }

    /**
     * Список всех задач отсортированных по id. Где DONE == done
     * @return список всех задач.
     */
    public List<Task> findAllDone(boolean done) {
        return taskStore.findAllDone(done);
    }

    /**
     * Установить в базе задач поле done == true.
     * @param task задача.
     */
    public boolean done(Task task) {
        return taskStore.done(task);
    }
}
