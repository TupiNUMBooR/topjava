package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.IDao;
import ru.javawebinar.topjava.dao.MealDaoInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

//@WebServlet(name = "MealServlet", value = "/MealServlet") к этому я так понял мы еще придем?
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd hh:mm:ss");
    private static final String FORWARD_UPDATE_MEAL = "/update-meal.jsp";
    private static final String FORWARD_MEALS = "/meals.jsp";
    private static final int caloriesPerDay = 2000;
    private IDao<Meal> dao;

    @Override
    public void init() {
        log.trace("init");
        dao = new MealDaoInMemory();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("get");
        req.setCharacterEncoding("UTF-8");
        String forward;
        String action = req.getParameter("action");

        if (action == null) {
            forward = FORWARD_MEALS;
        } else {
            action = action.toLowerCase();
            switch (action) {
                case "delete": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    dao.delete(id);
                    forward = FORWARD_MEALS;
                    break;
                }
                case "update": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    req.setAttribute("meal", dao.getById(id));
                    forward = FORWARD_UPDATE_MEAL;
                    break;
                }
                case "add":
                    req.setAttribute("meal", new Meal(LocalDateTime.now(), "", 0));
                    forward = FORWARD_UPDATE_MEAL;
                    break;
                default:
                    forward = FORWARD_MEALS;
                    break;
            }
        }

        if (forward.equals(FORWARD_MEALS)) {
            List<MealTo> mealsTo = MealsUtil.toMealsTo(dao.getAll(), caloriesPerDay);
            req.setAttribute("meals", mealsTo);
        } else if (forward.equals(FORWARD_UPDATE_MEAL)) {
            // IDEA предлагает убрать лишний if, но если сверху код поменяется - может появиться неочевидный баг, так ведь?
            req.setAttribute("action", action);
        }
        req.setAttribute("dateTimeFormatter", dateTimeFormatter);
        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("post");
        req.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        int calories = Integer.parseInt(req.getParameter("calories"));
        switch (req.getParameter("action").toLowerCase()) {
            case "add":
                dao.add(new Meal(0, dateTime, req.getParameter("description"), calories));
                break;
            case "update":
                int id = Integer.parseInt(req.getParameter("id"));
                dao.update(new Meal(id, dateTime, req.getParameter("description"), calories));
                break;
        }

        resp.sendRedirect("meals");
    }
}
