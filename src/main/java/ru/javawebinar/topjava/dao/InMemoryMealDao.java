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
    private static final Logger log = getLogger(InMemoryMealDao.class);

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
    public Meal getById(int id) {
        log.trace("getById {}", id);
        return mealsMap.get(id);
    }

    @Override
    public Meal add(Meal meal) {
        log.trace("add");
        Meal newMeal = new Meal(counter.getAndIncrement(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        mealsMap.put(newMeal.getId(), newMeal);
        return newMeal;
    }

    @Override
    public Meal update(Meal meal) {
        log.trace("update {}", meal.getId());
        return mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public void delete(int id) {
        log.trace("delete {}", id);
        mealsMap.remove(id);
    }
}
