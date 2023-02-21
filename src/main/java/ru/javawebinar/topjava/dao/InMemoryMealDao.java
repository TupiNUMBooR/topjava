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

public class InMemoryMealDao implements Dao<Meal> {
    private static final Logger log = getLogger(UserServlet.class);

    private final AtomicInteger counter = new AtomicInteger(0);

    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();

    public InMemoryMealDao() {
        MealsUtil.createHardcodeMeals().forEach(this::add);
    }

    @Override
    public List<Meal> getAll() {
        log.trace("getAll");
        return new ArrayList<>(mealsMap.values());
    }

    @Override
    public Meal get(int id) {
        log.trace("getById {}", id);
        return mealsMap.get(id);
    }

    @Override
    public Meal add(Meal meal) {
        log.trace("add");
        int id = counter.getAndIncrement();
        return mealsMap.put(id, new Meal(id, meal.getDateTime(), meal.getDescription(), meal.getCalories()));
    }

    @Override
    public Meal update(Meal meal) {
        log.trace("update {}", meal.getId());
        if (!mealsMap.containsKey(meal.getId())) throw new IllegalArgumentException("No meal with id " + meal.getId());
        return mealsMap.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        log.trace("delete {}", id);
        mealsMap.remove(id);
    }
}
