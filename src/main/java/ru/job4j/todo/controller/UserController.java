package ru.job4j.todo.controller;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;
import ru.job4j.todo.view.UserTimeZone;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * <p>TaskController class. Spring boot controller</p>
 * @author nikez
 * @version $Id: $Id
 */
@ThreadSafe
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    /**
     * <p>Регистрация пользователя.</p>
     * Страница для регистрации пользователя  web service
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/pages/registration")
    public String registration(Model model) {
        List<UserTimeZone> timeZones = userService.findAllTimeZone();
        model.addAttribute("time_zones", timeZones);
        model.addAttribute("zone_id", TimeZone.getDefault().getID());
        return "registration";
    }

    /**
     * <p>Обработка результатов страницы регистрация пользователя.</p>
     * Страница для регистрации пользователя  web service
     * @return a {@link java.lang.String} object.
     */
    @PostMapping("/pages/registration")
    public String registration1(Model model, HttpSession session, @ModelAttribute User user) {
        String link = "/pages/login";
        String error = "Ошибка! Не переданы данные пользователя на сервер.";
        if (user != null) {
            Optional<User> result = userService.create(user);
            if (result.isPresent()) {
                user.setId(result.get().getId());
                user.setPassword("");
                session.setAttribute("user", user);
                return "redirect:/pages/login";
            }
            error = "Ошибка! Такой пользователь уже есть или попробуйте повторить позже.";
            user.setPassword("");
        }
        model.addAttribute("link", link);
        model.addAttribute("error", error);
        model.addAttribute("user", user);
        return "error";
    }

    /**
     * <p>user login.</p>
     * Страница входа пользоватнля web service
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/pages/login")
    public String login() {
        return "login";
    }

    /**
     * <p>Обработка результатов страницы входа пользоватнля.</p>
     * Страница для обработки результатов входа пользоватнля web service
     * @return a {@link java.lang.String} object.
     */
    @PostMapping("/pages/login")
    public String login1(Model model, HttpSession session, @ModelAttribute User user) {
        String link = "/pages/login";
        String error = "Ошибка! Не переданы данные пользователя на сервер.";
        if (user != null) {
            Optional<User> result = userService.findByLoginAndPassword(
                    user.getLogin(), user.getPassword());
            if (result.isPresent()) {
                user.setId(result.get().getId());
                user.setName(result.get().getName());
                user.setPassword("");
                session.setAttribute("user", user);
                return "redirect:/index";
            }
            error = "Ошибка! Нет такого пользователя с таким паролем.";
            user.setPassword("");
        }
        model.addAttribute("link", link);
        model.addAttribute("error", error);
        model.addAttribute("user", user);
        return "error";
    }

    /**
     * <p>Обработка выхода пользователя с сайта.</p>
     * Страница для выхода пользоватнля с web service
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/pages/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/pages/login";
    }
}
