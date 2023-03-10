package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {
    @Test
    public void getWithUser() {
        Meal actual = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, adminMeal1);
        UserTestData.USER_MATCHER.assertMatch(actual.getUser(), UserTestData.admin);
    }

    @Test
    public void getWithUserNotExistUser() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(ADMIN_MEAL_ID, NOT_FOUND));
    }

    @Test
    public void getWithUserNotExistMeal() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(NOT_FOUND, ADMIN_ID));
    }
}
