package ru.job4j.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.CategoryStore;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryStore categoryStore;

    /**
     * Список всех категорий отсортированных по id.
     * @return список всех категорий.
     */
    public List<Category> findAll() {
        return categoryStore.findAll();
    }

    /**
     * Найти категорию по ID
     * @param id категории.
     * @return категорию.
     */
    public Optional<Category> findById(int id) {
        return categoryStore.findById(id);
    }

    /**
     * Список всех категорий отсортированных по id.
     * @param ids массив выбранных идентификаторов категорий.
     * @return список выбранных категорий.
     */
    public List<Category> findByIds(int[] ids) {
        return categoryStore.findByIds(ids);
    }
}
