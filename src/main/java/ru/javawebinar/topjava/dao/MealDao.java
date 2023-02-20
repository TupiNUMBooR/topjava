package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.UserServlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class MealDao implements IDao<Meal> {
    private int counter = 0;
    private final Map<Integer, Meal> idMeals = new HashMap<>();
    private static final Logger log = getLogger(UserServlet.class);

    public MealDao() {
        MealsUtil.createHardcodeMeals().forEach(this::add);
    }

    @Override
    public List<Meal> getAll() {
        log.trace("getAll");
        return new ArrayList<>(idMeals.values());
    }

    @Override
    public Meal getById(int id) {
        log.trace("getById " + id);
        return idMeals.getOrDefault(id, null);
    }

    @Override
    public void add(Meal meal) {
        log.trace("add " + meal.getId());
        idMeals.put(counter, new Meal(counter, meal));
        counter++;
    }

    @Override
    public void update(Meal meal) {
        log.trace("update " + meal.getId());
        idMeals.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        log.trace("delete " + id);
        idMeals.remove(id);
    }
}
