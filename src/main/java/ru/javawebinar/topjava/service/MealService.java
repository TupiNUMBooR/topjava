package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private final MealRepository mealRepository;

    public MealService(MealRepository repository) {
        this.mealRepository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return mealRepository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(mealRepository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(mealRepository.get(id, userId), id);
    }

    public List<MealTo> getAll(int userId, int userCaloriesPerDay) {
        List<Meal> meals = mealRepository.getAll(userId);
        return MealsUtil.getTos(meals, userCaloriesPerDay);
    }

    public List<MealTo> getFiltered(int userId, int userCaloriesPerDay, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        List<Meal> meals = mealRepository.getFiltered(userId, startDate, endDate);
        return MealsUtil.getFilteredTos(meals, userCaloriesPerDay, startTime, endTime);
    }

    public Meal update(Meal meal, int userId) {
        return checkNotFoundWithId(mealRepository.save(meal, userId), meal.getId());
    }
}