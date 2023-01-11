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
    public Task create(Task task) {
        return taskStore.create(task);
    }

    /**
     * Обновить в базе задач.
     * @param task задача.
     */
    public void update(Task task) {
        taskStore.update(task);
    }

    /**
     * Удалить задачу по ID.
     * @param taskId ID
     */
    public void delete(int taskId) {
        taskStore.delete(taskId);
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

    public List<Task> findAllDone() {
        return taskStore.findAllDone();
    }

    public List<Task> findAllNew() {
        return taskStore.findAllNew();
    }
}
