package ru.job4j.todo.controller;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>TaskController class. Spring boot controller</p>
 * @author nikez
 * @version $Id: $Id
 */
@ThreadSafe
@RequiredArgsConstructor
@Controller
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    /**
     * <p>index.</p>
     * Main page web service
     * @return a {@link java.lang.String} object.
     */

    @GetMapping("/index")
    public String index(HttpSession session) {
        String filter = (String) session.getAttribute("filter");
        if ("done".equals(filter)) {
            return "redirect:/pages/done";
        } else if ("new".equals(filter)) {
            return "redirect:/pages/new";
        }
        return "redirect:/pages/all";
    }

    /**
     * <p>formCreate.</p>
     * formCreate page web service. Handler button 'Довавить задание' from index page.
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/formCreate")
    public String formCreate(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Priority> priorities = priorityService.findAll();
        model.addAttribute("priorities", priorities);
        Priority priority = priorities.get(0);
        model.addAttribute("priority_id", priority.getId());

        Task task = new Task();
        task.setDescription("description");
        task.setCreated(LocalDateTime.now());
        task.setDone(false);
        task.setUser(user);
        task.setPriority(priority);
        task.setCategories(new ArrayList<>());
        model.addAttribute("task", task);

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "create";
    }

    /**
     * <p>add.</p>
     * Web service. Handler button 'Сохранить' from formCreate page.
     * @return a {@link java.lang.String} object.
     */
    @PostMapping("/add")
    public String add(
            Model model,
            HttpSession session,
            @ModelAttribute Task task,
            @RequestParam("priority_id") int priorityId,
            @RequestParam(value = "checks", required = false) int[] checks
    ) {
        User user = (User) session.getAttribute("user");
        task.setUser(userService.findById(user.getId()).orElse(null));
        Optional<Priority> priority = priorityService.findById(priorityId);
        priority.ifPresent(x -> task.setPriority(priority.get()));
        setCategoriesFromChecks(task, checks);
        Optional<Task> task1 = taskService.create(task);
        if (task1.isPresent()) {
            return "redirect:/index";
        }
        model.addAttribute("task", task);
        model.addAttribute("link", "/index");
        model.addAttribute("error", "Ошибка! Не удалось создать задачу.");
        return "error";
    }

    private void setCategoriesFromChecks(Task task, int[] checks) {
        if (checks != null) {
            List<Category> categories = new ArrayList<>();
            for (int check: checks) {
                Optional<Category> category = categoryService.findById(check);
                category.ifPresent(categories::add);
            }
            task.setCategories(categories);
        }
    }

    /**
     * <p>add.</p>
     * Web service. Set filter to all records.
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/pages/all")
    public String all(Model model,
                        HttpSession session) {
        model.addAttribute("tasks", taskService.findAll());
        session.setAttribute("filter", "all");
        return "index";
    }

    /**
     * <p>add.</p>
     * Web service. Set filter to done records.
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/pages/done")
    public String done(Model model, HttpSession session) {
        model.addAttribute("tasks", taskService.findAllByDone(true));
        session.setAttribute("filter", "done");
        return "index";
    }

    /**
     * <p>add.</p>
     * Web service. Set filter to new records.
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/pages/new")
    public String new1(Model model, HttpSession session) {
        model.addAttribute("tasks", taskService.findAllByDone(false));
        session.setAttribute("filter", "new");
        return "index";
    }

    /**
     * <p>formView.</p>
     * Task view page web service.
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/formView/{Id}")
    public String formView(Model model, @PathVariable("Id") int id) {
        Optional<Task> task = taskService.findById(id);
        if (task.isPresent()) {
            model.addAttribute("task", task.get());
            return "view";
        }
        model.addAttribute("link", "/index");
        model.addAttribute("error", "Ошибка! Задача c id="
                + id + " не существует.");
        return "error";
    }

    /**
     * <p>formView.</p>
     * Web service. Handler button 'Удалить' from form formView.
     * @return a {@link java.lang.String} object.
     */
    @PostMapping("/delete")
    public String delete(Model model, @ModelAttribute Task task) {
        if (taskService.delete(task.getId())) {
            return "redirect:/index";
        }
        model.addAttribute("task", task);
        model.addAttribute("link", "/view");
        model.addAttribute("error", "Ошибка! Задача не удалена.");
        return "error";
    }

    /**
     * <p>formView.</p>
     * Web service. Handler button 'Выполнено' from form formView.
     * @return a {@link java.lang.String} object.
     */
    @PostMapping("/formDone")
    public String formDone(Model model, HttpSession session, @ModelAttribute Task task) {
        User user = (User) session.getAttribute("user");
        task.setUser(userService.findById(user.getId()).orElse(null));
        if (taskService.updateDone(task)) {
            return "redirect:/index";
        }
        model.addAttribute("task", task);
        model.addAttribute("link", "/view");
        model.addAttribute("error", "Ошибка! Не удалось установить статус задачи.");
        return "error";
    }

    /**
     * <p>formEdit.</p>
     * Task edit page web service. Handler button 'Отредактировать' from page formView.
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/formEdit")
    public String formEdit(Model model,
                           @ModelAttribute Task task,
                           @RequestParam(value = "checks", required = false) int[] checks
    ) {
        model.addAttribute("task", task);
        List<Priority> priorities = priorityService.findAll();
        model.addAttribute("priorities", priorities);
        int i = task.getPriority().getId();
        model.addAttribute("priority_id", i);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        List<Boolean> categoriesChecked = new ArrayList<>();
        int index = 0;
        for (Category category: categories) {
            boolean res = false;
            if ((checks != null) && (checks.length > index)) {
                res = category.getId() == checks[index];
                if (res) {
                    index++;
                }
            }
            categoriesChecked.add(res);
        }
        model.addAttribute("categories_checked", categoriesChecked);
        return "edit";
    }

    /**
     * <p>edit.</p>
     * Handler button 'сохранить' edit page web service
     * @return a {@link java.lang.String} object.
     */
    @PostMapping("/edit")
    public String edit(
            Model model,
            HttpSession session,
            @ModelAttribute Task task,
            @RequestParam("priority_id") int priorityId,
            @RequestParam(value = "checks", required = false) int[] checks
    ) {
        User user = (User) session.getAttribute("user");
        task.setUser(userService.findById(user.getId()).orElse(null));
        Optional<Priority> priority = priorityService.findById(priorityId);
        priority.ifPresent(x -> task.setPriority(priority.get()));
        setCategoriesFromChecks(task, checks);
        if (taskService.update(task)) {
            return "redirect:/index";
        }
        model.addAttribute("task", task);
        model.addAttribute("link", "/edit");
        model.addAttribute("error", "Ошибка! Не удалось сохранить изменения.");
        return "error";
    }
}
