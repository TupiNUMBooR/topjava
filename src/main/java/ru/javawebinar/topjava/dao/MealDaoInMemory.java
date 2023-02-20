package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.UserServlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealDaoInMemory implements IDao<Meal> {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, Meal> idMeals = new ConcurrentHashMap<>();
    private static final Logger log = getLogger(UserServlet.class);

    public MealDaoInMemory() {
        MealsUtil.createHardcodeMeals().forEach(this::add);
    }

    @Override
    public synchronized List<Meal> getAll() {
        log.trace("getAll");
        return new ArrayList<>(idMeals.values());
    }

    @Override
    public synchronized Meal getById(int id) {
        log.trace("getById " + id);
        return idMeals.getOrDefault(id, null);
    }

    @Override
    public synchronized void add(Meal meal) {
        log.trace("add " + meal.getId());
        int id = counter.getAndIncrement();
        idMeals.put(id, new Meal(id, meal));
    }

    @Override
    public synchronized void update(Meal meal) {
        log.trace("update " + meal.getId());
        idMeals.put(meal.getId(), meal);
    }

    @Override
    public synchronized void delete(int id) {
        log.trace("delete " + id);
        idMeals.remove(id);
    }
}
