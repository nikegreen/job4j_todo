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

/**
 * <p>TaskController class. Spring boot controller</p>
 * return allways text "Greetings from Spring Boot!"
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
    public String index(Model model,
                        HttpSession session) {
        String filter = (String) session.getAttribute("filter");
        if ("done".equals(filter)) {
            model.addAttribute("tasks", taskService.findAllDone());
        } else if ("new".equals(filter)) {
            model.addAttribute("tasks", taskService.findAllNew());
        } else {
            model.addAttribute("tasks", taskService.findAll());
            filter = "all";
        }
        session.setAttribute("filter", filter);
        return "index";
    }

    @PostMapping("/formCreateTask")
    public String formCreateTask(Model model) {
        Task task = new Task();
        task.setDescription("description");
        task.setCreated(LocalDateTime.now());
        task.setDone(false);
        model.addAttribute("task", task);
        return "createTask";
    }

    @PostMapping("/addTask")
    public String addTask(Model model, @ModelAttribute Task task) {
        Task task1 = taskService.create(task);
        if (task1 == null) {
            model.addAttribute("task", task);
            return "addTaskError";
        }
        return "redirect:/index";
    }

    @GetMapping("/filterAll")
    public String filterAll(Model model,
                        HttpSession session) {
        model.addAttribute("tasks", taskService.findAll());
        session.setAttribute("filter", "all");
        return "index";
    }

    @GetMapping("/filterDone")
    public String filterDone(Model model,
                            HttpSession session) {
        model.addAttribute("tasks", taskService.findAllDone());
        session.setAttribute("filter", "done");
        return "index";
    }

    @GetMapping("/filterNew")
    public String filterNew(Model model,
                            HttpSession session) {
        model.addAttribute("tasks", taskService.findAllNew());
        session.setAttribute("filter", "new");
        return "index";
    }

    @GetMapping("/formViewTask/{taskId}")
    public String formViewTask(Model model,
                               @PathVariable("taskId") int id) {
        Task task = taskService.findById(id).orElse(null);
        if (task != null) {
            model.addAttribute("task", task);
            return "viewTask";
        }
        return "redirect:/index";
    }

    @PostMapping("/deleteTask")
    public String deleteTask(@ModelAttribute Task task) {
        taskService.delete(task.getId());
        return "redirect:/index";
    }

    @PostMapping("/doneTask")
    public String doneTask(@ModelAttribute Task task) {
        task.setDone(true);
        taskService.update(task);
        return "redirect:/index";
    }

    @PostMapping("/formEditTask")
    public String formEditTask(Model model, @ModelAttribute Task task) {
        model.addAttribute("task", task);
        return "editTask";
    }

    @PostMapping("/editTask")
    public String editTask(@ModelAttribute Task task) {
        taskService.update(task);
        return "redirect:/index";
    }
}
