package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @Test
    public void getWithMeals() {
        User actual = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(actual, user);
        MEAL_MATCHER.assertMatch(actual.getMeals(), meals);
    }

    @Test
    public void getWithMealsCheckRoles() {
        User actual = service.getWithMeals(USER_ID);
        assertEquals(actual.getRoles(), user.getRoles());
    }

    @Test
    public void getWithEmptyMeals() {
        User actual = service.getWithMeals(GUEST_ID);
        USER_MATCHER.assertMatch(actual, guest);
        MEAL_MATCHER.assertMatch(actual.getMeals());
    }

    @Test
    public void getWithMealsNoUser() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }

    @Test
    public void updateUserNotRemoveMeals() {
        User updated = getUpdated();
        service.update(updated);
        User actual = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(actual, getUpdated());
        MEAL_MATCHER.assertMatch(actual.getMeals(), meals);
    }
}
