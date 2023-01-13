package ru.job4j.todo.store;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskStore {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param task задача.
     * @return задача с новым id.
     */
    public Optional<Task> create(Task task) {
        Optional<Task> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.save(task);
                session.getTransaction().commit();
                result = Optional.of(task);
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Обновить в базе задач.
     * @param task задача.
     */
    public boolean update(Task task) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.createQuery(
                                "UPDATE Task SET description = :fDescription, "
                                        + "created = :fCreated, done = :fDone WHERE id = :fId")
                        .setParameter("fDescription", task.getDescription())
                        .setParameter("fCreated", task.getCreated())
                        .setParameter("fDone", task.isDone())
                        .setParameter("fId", task.getId())
                        .executeUpdate();
                session.getTransaction().commit();
                result = true;
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Установить в базе задач поле done == true.
     * @param task задача.
     */
    public boolean done(Task task) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.createQuery(
                                "UPDATE Task SET done = :fDone WHERE id = :fId")
                        .setParameter("fDone", true)
                        .setParameter("fId", task.getId())
                        .executeUpdate();
                session.getTransaction().commit();
                result = true;
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Удалить задачу по id.
     * @param taskId ID
     */
    public boolean delete(int taskId) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.createQuery(
                                "DELETE Task WHERE id = :fId")
                        .setParameter("fId", taskId)
                        .executeUpdate();
                session.getTransaction().commit();
                result = true;
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Список всех задач отсортированных по id.
     * @return список всех задач.
     */
    public List<Task> findAll() {
        List<Task> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<Task> query = session.createQuery("from Task order by id", Task.class);
                result = query.list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
            session.close();
        }
        return result;
    }

    /**
     * Найти задачу по ID
     * @return задача.
     */
    public Optional<Task> findById(int id) {
        Optional<Task> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<Task> query = session.createQuery(
                        "from Task as i where i.id = :fId", Task.class)
                        .setParameter("fId", id);
                session.getTransaction().commit();
                result = query.uniqueResultOptional();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
            session.close();
        }
        return result;
    }

    public List<Task> findAllDone(boolean done) {
        List<Task> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<Task> query = session.createQuery(
                        "from Task as t where t.done= :fDone order by t.id", Task.class)
                        .setParameter("fDone", done);
                result = query.list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
            session.close();
        }
        return result;
    }
}
