package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println();
        filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)
                .forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, List<UserMeal>> dateMeals = new HashMap<>(); // я не знаю, упорядочен ли meals или нет, так что предполагаем худший сценарий
        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            if (!dateMeals.containsKey(date)) dateMeals.put(date, new ArrayList<>());
            dateMeals.get(date).add(meal);
        }

        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        for (Map.Entry<LocalDate, List<UserMeal>> entry : dateMeals.entrySet()) {
            int currentCalories = 0;
            for (UserMeal meal : entry.getValue()) {
                currentCalories += meal.getCalories();
            }
            for (UserMeal meal : entry.getValue()) {
                if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                    mealsWithExcess.add(fromUserMeal(meal, currentCalories > caloriesPerDay));
                }
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, List<UserMeal>> dateMeals = meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate())); // я не знаю, упорядочен ли meals или нет, так что предполагаем худший сценарий

        return dateMeals.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .flatMap(entry -> {
                    int currentCalories = entry.getValue().stream()
                            .mapToInt(UserMeal::getCalories)
                            .sum();
                    return entry.getValue().stream()
                            .sorted(Comparator.comparing(UserMeal::getDateTime))
                            .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                            .map(meal -> fromUserMeal(meal, currentCalories > caloriesPerDay));
                })
                .collect(Collectors.toList());
    }

    public static UserMealWithExcess fromUserMeal(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
