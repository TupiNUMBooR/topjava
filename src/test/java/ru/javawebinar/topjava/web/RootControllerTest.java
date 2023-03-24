package ru.javawebinar.topjava.web;

import org.assertj.core.matcher.AssertionMatcher;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;

import java.util.List;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.MEAL_TO_MATCHER;
import static ru.javawebinar.topjava.MealTestData.mealsTo;
import static ru.javawebinar.topjava.UserTestData.*;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users", getMatcher(User.class,
                        actual -> USER_MATCHER.assertMatch(actual, admin, guest, user)
                )));
    }

    @Test
    void getMeals() throws Exception {
        perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", getMatcher(MealTo.class,
                        actual -> MEAL_TO_MATCHER.assertMatch(actual, mealsTo)
                )));
    }

    private <T> Matcher<List<T>> getMatcher(Class<T> ignoredClass, Consumer<List<T>> tester) {
        return new AssertionMatcher<>() {
            @Override
            public void assertion(List<T> actual) throws AssertionError {
                tester.accept(actual);
            }
        };
    }
}