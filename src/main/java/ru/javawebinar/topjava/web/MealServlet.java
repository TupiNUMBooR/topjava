package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MMM-dd hh:mm");
    private static final String FORWARD_UPDATE_MEAL = "/updateMeal.jsp";
    private static final String FORWARD_MEALS = "/meals.jsp";
    private static final int CALORIES_PER_DAY = 2000;
    private Dao<Meal> dao;

    @Override
    public void init() {
        log.trace("init");
        dao = new InMemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("get");
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
                    resp.sendRedirect("meals");
                    return;
                }
                case "update": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    req.setAttribute("meal", dao.getById(id));
                    forward = FORWARD_UPDATE_MEAL;
                    break;
                }
                case "add":
                    req.setAttribute("meal", new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0));
                    forward = FORWARD_UPDATE_MEAL;
                    break;
                default:
                    forward = FORWARD_MEALS;
                    break;
            }
        }

        if (forward.equals(FORWARD_MEALS)) {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            req.setAttribute("meals", mealsTo);
//            req.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
        }
        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("post");
        req.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        int calories = Integer.parseInt(req.getParameter("calories"));
        String idPar = req.getParameter("id");
        if (idPar == null) {
            dao.add(new Meal(null, dateTime, req.getParameter("description"), calories));
        } else {
            int id = Integer.parseInt(idPar);
            dao.update(new Meal(id, dateTime, req.getParameter("description"), calories));
        }

        resp.sendRedirect("meals");
    }
}
