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
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::add);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.userId = userId;

        if (meal.isNew()) {
            add(meal);
            return meal;
        }
        // handle case: update, but not present in storage
        Meal newMeal = repository.computeIfPresent(meal.getId(), (id, oldMeal) -> oldMeal.getUserId() == userId ? meal : oldMeal);
        return meal == newMeal ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        AtomicBoolean removed = new AtomicBoolean();
        repository.compute(id, (id2, meal) -> {
            if (meal != null && userId == meal.userId) {
                removed.set(true);
                return null;
            } else {
                removed.set(false);
                return meal;
            }
        });

        return removed.get();
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return meal != null && meal.userId == userId ? meal : null;
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
        return repository.values().stream()
                .filter(meal -> meal.userId == userId)
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private void add(Meal meal) {
        meal.setId(counter.incrementAndGet());
        repository.put(meal.getId(), meal);
    }
}

