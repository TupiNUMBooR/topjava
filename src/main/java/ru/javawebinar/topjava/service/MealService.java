package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public MealService(@Autowired MealRepository repository, @Autowired UserRepository userRepository) {
        this.mealRepository = repository;
        this.userRepository = userRepository;
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

    public List<Meal> getAll(int userId) {
        return mealRepository.getAll(userId);
    }

    public List<MealTo> getAllTo(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        List<Meal> meals = mealRepository.getAll(userId);
        if (meals.size() == 0) return new ArrayList<>();
        User user = checkNotFoundWithId(userRepository.get(userId), userId);
        List<MealTo> mealsTo = MealsUtil.getFilteredTos(meals, user.getCaloriesPerDay(), startDate, endDate, startTime, endTime);
        return mealsTo;
    }

    public void update(Meal meal, int userId) {
        checkNotFoundWithId(mealRepository.save(meal, userId), meal.getId());
    }
}