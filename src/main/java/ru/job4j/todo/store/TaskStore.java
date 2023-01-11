package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@AllArgsConstructor
@Repository
@RequiredArgsConstructor
public class TaskStore {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param task задача.
     * @return задача с новым id.
     */
    public Task create(Task task) {
        Task result = null;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.save(task);
                session.getTransaction().commit();
                result = task;
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
    public void update(Task task) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.createQuery(
                        "UPDATE Task SET description = :fDescription, "
                                + "created = :fCreated, done = :fDone WHERE id = :fId")
                        .setParameter("fDescription", task.getDescription())
                        .setParameter("fCreated", task.getCreated())
                        .setParameter("fDone", task.getDone())
                        .setParameter("fId", task.getId())
                        .executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
    }

    /**
     * Удалить задачу по id.
     * @param taskId ID
     */
    public void delete(int taskId) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.createQuery(
                                "DELETE Task WHERE id = :fId")
                        .setParameter("fId", taskId)
                        .executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
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
                result = query.uniqueResultOptional());
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
            session.close();
        }
        return result;
    }

    public List<Task> findAllDone() {
        List<Task> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<Task> query = session.createQuery(
                        "from Task as t where t.done=true order by t.id", Task.class);
                result.addAll(query.list());
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
            session.close();
        }
        return result;
    }

    public List<Task> findAllNew() {
        List<Task> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<Task> query = session.createQuery(
                        "from Task as t where t.done=false order by t.id", Task.class);
                result.addAll(query.list());
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
            session.close();
        }
        return result;
    }
}
