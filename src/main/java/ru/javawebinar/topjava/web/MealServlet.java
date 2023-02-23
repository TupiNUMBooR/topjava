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
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String FORWARD_UPDATE_MEAL = "/updateMeal.jsp";
    private static final String FORWARD_MEALS = "/meals.jsp";
    private static final String REDIRECT_MEALS = "meals";
    private static final int CALORIES_PER_DAY = 2000;
    private Dao<Meal> dao;

    @Override
    public void init() {
        log.trace("init");
        dao = new InMemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        log.trace("get?action={}", action);
        if ("delete".equalsIgnoreCase(action)) {
            log.trace("delete");
            int id = Integer.parseInt(req.getParameter("id"));
            dao.delete(id);
            resp.sendRedirect(REDIRECT_MEALS);
        } else if ("update".equalsIgnoreCase(action)) {
            log.trace("update");
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("meal", dao.getById(id));
            req.getRequestDispatcher(FORWARD_UPDATE_MEAL).forward(req, resp);
        } else if ("add".equalsIgnoreCase(action)) {
            log.trace("add");
            req.setAttribute("meal", new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0));
            req.getRequestDispatcher(FORWARD_UPDATE_MEAL).forward(req, resp);
        } else {
            log.trace("get meals");
            List<MealTo> mealsTo = MealsUtil.toMealsTo(dao.getAll(), CALORIES_PER_DAY);
            req.setAttribute("meals", mealsTo);
//            req.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
            req.getRequestDispatcher(FORWARD_MEALS).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.trace("post");
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
