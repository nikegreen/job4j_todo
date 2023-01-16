package ru.job4j.todo.store;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class UserStore {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе пользователя.
     * @param user пользователь.
     * @return пользователь с новым id.
     */
    public Optional<User> create(User user) {
        Function<Session, Optional<User>> command = session -> {
            session.persist(user);
            return Optional.ofNullable(user);
        };
        return crudRepository.tx(command);
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     * @return boolean
     * true - обновлён, false - не обновлён.
     */
    public boolean update(User user) {
        Function<Session, Boolean> command = session -> {
            session.merge(user);
            return true;
        };
        return crudRepository.tx(command);
    }

    /**
     * Удалить пользователя по id из БД.
     * @param id пользователя
     * @return true - удалён, false - не удалён.
     */
    public boolean delete(int id) {
         Function<Session, Integer> command = session -> {
            var sq = session.createQuery("delete from User where id = :fId");
            sq.setParameter("fId", id);
            return sq.executeUpdate();
        };
        return crudRepository.tx(command) == 1;
    }

    /**
     * Список всех пользователей отсортированных по id.
     * @return список всех пользователей.
     */
    public List<User> findAll() {
         return crudRepository.query("from User order by id asc", User.class);
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        return crudRepository.optional(
                "from User where id = :fId",
                User.class,
                Map.of("fId", id)
        );
    }

    /**
     * Найти пользователя по login и password
     * @return Optional\<User\> пользователь.
     */
    public Optional<User> findByLoginAndPassword(String login, String password) {
         return crudRepository.optional(
                "from User where login = :fLogin and password = :fPassword",
                User.class,
                Map.of("fLogin", login, "fPassword", password)
         );
    }
}
