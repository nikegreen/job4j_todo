package ru.job4j.todo.controller;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
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
    public String formCreate(Model model) {
        Task task = new Task();
        task.setDescription("description");
        task.setCreated(LocalDateTime.now());
        task.setDone(false);
        model.addAttribute("task", task);
        return "create";
    }

    /**
     * <p>add.</p>
     * Web service. Handler button 'Сохранить' from formCreate page.
     * @return a {@link java.lang.String} object.
     */
    @PostMapping("/add")
    public String add(Model model, @ModelAttribute Task task) {
        Optional<Task> task1 = taskService.create(task);
        if (task1.isPresent()) {
            return "redirect:/index";
        }
        model.addAttribute("task", task);
        model.addAttribute("link", "/index");
        model.addAttribute("error", "Ошибка! Не удалось создать задачу.");
        return "error";
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
    public String done(Model model,
                            HttpSession session) {
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
    public String new1(Model model,
                            HttpSession session) {
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
    public String formView(Model model,
                               @PathVariable("Id") int id) {
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
    public String formDone(Model model, @ModelAttribute Task task) {
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
    public String formEdit(Model model, @ModelAttribute Task task) {
        model.addAttribute("task", task);
        return "edit";
    }

    /**
     * <p>edit.</p>
     * Handler button 'сохранить' edit page web service
     * @return a {@link java.lang.String} object.
     */
    @PostMapping("/edit")
    public String edit(Model model, @ModelAttribute Task task) {
        if (taskService.update(task)) {
            return "redirect:/index";
        }
        model.addAttribute("task", task);
        model.addAttribute("link", "/edit");
        model.addAttribute("error", "Ошибка! Не удалось сохранить изменения.");
        return "error";
    }
}
