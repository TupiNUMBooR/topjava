package ru.javawebinar.topjava.util;

import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.Month;

import static java.time.LocalDateTime.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MealsUtilTest {

    @Test
    void createTo() {
        var meal = new Meal(15, of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        assertMeal(meal, MealsUtil.createTo(meal, true), true);
        assertMeal(meal, MealsUtil.createTo(meal, false), false);
    }

    private void assertMeal(Meal meal, MealTo mealTo, boolean excess) {
        assertEquals(meal.getId(), mealTo.getId());
        assertEquals(meal.getDateTime(), mealTo.getDateTime());
        assertEquals(meal.getDescription(), mealTo.getDescription());
        assertEquals(meal.getCalories(), mealTo.getCalories());
        assertEquals(excess, mealTo.isExcess());
    }
}