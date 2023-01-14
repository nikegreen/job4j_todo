package ru.job4j.todo.store;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserStore {
    private final SessionFactory sf;

    /**
     * Сохранить в базе пользователя.
     * @param user пользователь.
     * @return пользователь с новым id.
     */
    public Optional<User> create(User user) {
        Optional<User> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
                result = Optional.of(user);
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     * @return true - обновлён, false - не обновлён.
     */
    public boolean update(User user) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.merge(user);
                session.getTransaction().commit();
                result = true;
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Удалить пользователя по id из БД.
     * @param id пользователя
     * @return true - удалён, false - не удалён.
     */
    public boolean delete(int id) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                int i = session.createQuery(
                        "DELETE User WHERE id = :fId", User.class)
                        .setParameter("fId", id)
                        .executeUpdate();
                session.getTransaction().commit();
                result = i == 1;
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Список всех пользователей отсортированных по id.
     * @return список всех пользователей.
     */
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<User> query = session.createQuery("from User order by id", User.class);
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
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        Optional<User> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<User> query = session.createQuery(
                        "from User as i where i.id = :fId", User.class)
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

    /**
     * Найти пользователя по login и password
     * @return Optional\<User\> пользователь.
     */
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<User> query = session.createQuery(
                         "from User as i where i.login = :fLogin and i.password = :fPassword",
                            User.class)
                        .setParameter("fLogin", login)
                        .setParameter("fPassword", password);
                session.getTransaction().commit();
                result = query.uniqueResultOptional();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
            session.close();
        }
        return result;
    }
}
