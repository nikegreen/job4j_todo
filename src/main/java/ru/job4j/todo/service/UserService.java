package ru.job4j.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.UserStore;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStore userStore;

    /**
     * Сохранить в базе пользователя.
     * @param user пользователь.
     * @return пользователь с новым id.
     */
    public Optional<User> create(User user) {
        return userStore.create(user);
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     * @return true - обновлён, false - не обновлён.
     */
    public boolean update(User user) {
        return userStore.update(user);
    }

    /**
     * Удалить пользователя по id из БД.
     * @param id пользователя
     * @return true - удалён, false - не удалён.
     */
    public boolean delete(int id) {
        return userStore.delete(id);
    }

    /**
     * Список всех пользователей отсортированных по id.
     * @return список всех пользователей.
     */
    public List<User> findAll() {
        return userStore.findAll();
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        return userStore.findById(id);
    }

    /**
     * Найти пользователя по login и password
     * @return Optional\<User\> пользователь.
     */
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userStore.findByLoginAndPassword(login, password);
    }
}
