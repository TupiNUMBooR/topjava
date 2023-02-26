package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::add);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUserId(userId);
            add(meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return getUserRepo(userId)
                .computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    private void add(Meal meal) {
        meal.setId(counter.incrementAndGet());
        getUserRepo(meal.getUserId()).put(meal.getId(), meal);
    }

    private Map<Integer, Meal> getUserRepo(int userId) {
        return repository.computeIfAbsent(userId, id -> new ConcurrentHashMap<>());
    }

    @Override
    public boolean delete(int id, int userId) {
        AtomicBoolean removed = new AtomicBoolean();
        getUserRepo(userId).compute(id, (id2, meal) -> {
            removed.set(meal != null);
            return null;
        });
        return removed.get();
    }

    @Override
    public Meal get(int id, int userId) {
        return getUserRepo(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        return getFiltered(userId, meal -> DateTimeUtil.isBetweenInclusiveAndInclusive(meal.getDate(), startDate, endDate));
    }

    private List<Meal> getFiltered(int userId, Predicate<Meal> filter) {
        return getUserRepo(userId).values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

