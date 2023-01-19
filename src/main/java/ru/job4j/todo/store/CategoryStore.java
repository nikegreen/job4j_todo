package ru.job4j.todo.store;

import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import ru.job4j.todo.model.Category;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryStore {
    private final CrudRepository crudRepository;

    /**
     * Список всех категорий отсортированных по id.
     * @return список всех категорий.
     */
    public List<Category> findAll() {
        return crudRepository.query("FROM Category t ORDER BY t.id ASC", Category.class);
    }

    /**
     * Найти категорию по ID
     * @param id категории.
     * @return категорию.
     */
    public Optional<Category> findById(int id) {
        return crudRepository.optional(
                "FROM Category WHERE id = :fId",
                Category.class,
                Map.of("fId", id)
        );
    }
}
