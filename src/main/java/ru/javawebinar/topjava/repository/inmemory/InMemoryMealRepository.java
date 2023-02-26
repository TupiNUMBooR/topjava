package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Map<Integer, Meal> EMPTY_MAP = Collections.emptyMap();
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> add(meal, 1));
        MealsUtil.meals2.forEach(meal -> add(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            add(meal, userId);
            return meal;
        }
        // handle case: update, but not present in storage
        Map<Integer, Meal> userRepo = repository.get(userId);
        if (userRepo == null) return null;
        return userRepo.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    private void add(Meal meal, int userId) {
        meal.setId(counter.incrementAndGet());
        repository.computeIfAbsent(userId, id -> new ConcurrentHashMap<>())
                .put(meal.getId(), meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return getUserRepo(userId).remove(id) != null;
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

    private Map<Integer, Meal> getUserRepo(int userId) {
        return repository.getOrDefault(userId, EMPTY_MAP);
    }
}

