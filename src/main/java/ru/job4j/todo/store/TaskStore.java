package ru.job4j.todo.store;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class TaskStore {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param task задача.
     * @return задача с новым id.
     */
    public Optional<Task> create(Task task) {
        Function<Session, Optional<Task>> command = session -> {
            session.persist(task);
            return Optional.ofNullable(task);
        };
        return crudRepository.tx(command);
    }

    /**
     * Обновить в базе задач.
     * @param task задача.
     * @return boolean
     */
    public boolean update(Task task) {
        Function<Session, Boolean> command = session -> {
            session.merge(task);
            return true;
        };
        return crudRepository.tx(command);
    }

    /**
     * Установить в базе задач поле done == true.
     * @param task задача.
     * @return boolean
     */
    public boolean updateDone(Task task) {
        Function<Session, Boolean> command = session -> {
            task.setDone(true);
            session.merge(task);
            return true;
        };
        return crudRepository.tx(command);
    }

    /**
     * Удалить задачу по id.
     * @param id ID
     * @return boolean
     */
    public boolean delete(int id) {
        Function<Session, Integer> command = session -> {
            var sq = session.createQuery("delete from Task where id = :fId");
            sq.setParameter("fId", id);
            return sq.executeUpdate();
        };
        return crudRepository.tx(command) == 1;
    }

    /**
     * Список всех задач отсортированных по id.
     * @return список всех задач.
     */
    public List<Task> findAll() {
        return crudRepository.query("from Task order by id asc", Task.class);
    }

    /**
     * Найти задачу по ID
     * @return задача.
     */
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "from Task where id = :fId",
                Task.class,
                Map.of("fId", id)
        );
    }

    /**
     * Получить список задач где done == done
     * @return List<Task>.
     */
    public List<Task> findAllByDone(boolean done) {
        return crudRepository.query(
                "from Task as t where t.done= :fDone order by t.id asc ",
                Task.class,
                Map.of("fDone", done));
    }
}
