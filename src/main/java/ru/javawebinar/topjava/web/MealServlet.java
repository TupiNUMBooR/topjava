package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet(name = "MealServlet", value = "/MealServlet")
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd hh:mm:ss");
    private final int caloriesPerDay = 2000;
    private List<Meal> meals;

    @Override
    public void init() throws ServletException {
        log.info("init");
        meals = MealsUtil.createHardcodeMeals();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("get");
        List<MealTo> mealsTo = MealsUtil.toMealsTo(meals, caloriesPerDay);
        request.setAttribute("meals", mealsTo);
        request.setAttribute("dateTimeFormatter", dateTimeFormatter);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
