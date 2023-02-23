package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    private static final Predicate<Meal> TRUE_FILTER = meal -> true;

    public static List<Meal> createHardcodeMeals() {
        return Arrays.asList(
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
    }

    public static List<MealTo> toMealsToFiltered(List<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        //для примера функция
        return toMealsTo(meals, caloriesPerDay, timeFilter(startTime, endTime));
    }

    public static List<MealTo> toMealsTo(List<Meal> meals, int caloriesPerDay) {
        return toMealsTo(meals, caloriesPerDay, TRUE_FILTER);
    }

    private static List<MealTo> toMealsTo(List<Meal> meals, int caloriesPerDay, Predicate<Meal> postFilter) {
        // сначала хотел реализовать паттерн стратегия через List<Function<Stream<Meal>, Stream<Meal>>> postProcessors
        // но по моему был бы перебор сложности и абстрактности
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(postFilter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static Predicate<Meal> timeFilter(LocalTime startTime, LocalTime endTime) {
        return meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime);
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
