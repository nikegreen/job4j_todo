package ru.job4j.todo.filter;

import org.springframework.stereotype.Component;
import ru.job4j.todo.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (uri.endsWith("index")
                || uri.endsWith("all")
                || uri.endsWith("done")
                || uri.endsWith("new")
                || uri.endsWith("login")
                || uri.endsWith("registration")
                || uri.endsWith("success")
                || uri.endsWith("error")
                || uri.endsWith("bootstrap/css/bootstrap.min.css")
                || uri.endsWith("bootstrap/js/bootstrap.bundle.min.js")
                || uri.endsWith("login.css")) {
            chain.doFilter(req, res);
            return;
        }
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getName() == null || "Гость".equals(user.getName())) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        chain.doFilter(req, res);
    }
}
