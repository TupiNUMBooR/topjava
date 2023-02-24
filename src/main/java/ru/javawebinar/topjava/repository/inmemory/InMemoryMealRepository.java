package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::add);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (userId != meal.userId) return null;
        if (meal.isNew()) {
            add(meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.computeIfPresent(id, (id2, meal) -> userId == meal.userId ? null : meal) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return userId == meal.userId ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.userId == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private void add(Meal meal) {
        meal.setId(counter.incrementAndGet());
        repository.put(meal.getId(), meal);
    }
}

