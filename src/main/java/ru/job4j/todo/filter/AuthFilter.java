package ru.job4j.todo.filter;

import org.springframework.stereotype.Component;
import ru.job4j.todo.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class AuthFilter implements Filter {
    private static final List<String> PAGES = List.of(
            "index",
            "pages/all",
            "pages/done",
            "pages/new",
            "pages/login",
            "pages/registration",
            "pages/error",
            "bootstrap/css/bootstrap.min.css",
            "bootstrap/js/bootstrap.bundle.min.js",
            "css/login.css"
    );

    private boolean granted(String url) {
        for (String page: PAGES) {
            if (url.endsWith(page)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (granted(uri)) {
            chain.doFilter(req, res);
            return;
        }
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getName() == null || "Гость".equals(user.getName())) {
            res.sendRedirect(req.getContextPath() + "/pages/login");
            return;
        }
        chain.doFilter(req, res);
    }
}
