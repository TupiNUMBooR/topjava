package ru.javawebinar.topjava.service;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.getUpdated;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class, profiles = Profiles.DATAJPA)
public class UserServiceDatajpaTest extends UserServiceTest {
    @Test
    public void getWithUser() {
        User actual = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(actual, user);
        MEAL_MATCHER.assertMatch(Hibernate.unproxy(actual.getMeals(), List.class), meals);
    }

    @Test
    public void updateUserNotRemoveMeals() {
        User updated = getUpdated();
        service.update(updated);
        User actual = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(actual, getUpdated());
        MEAL_MATCHER.assertMatch(Hibernate.unproxy(actual.getMeals(), List.class), meals);
    }
}
