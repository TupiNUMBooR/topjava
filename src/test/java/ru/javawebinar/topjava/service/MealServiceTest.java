package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-repo-jdbc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Meal newMeal = getNew();
        newMeal.setId(created.getId());
        assertMatch(created, newMeal);
        assertMatch(service.get(created.getId(), USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(meal1.getDateTime(), "Еда", 300), USER_ID));
    }

    @Test
    public void delete() {
        service.delete(meal1.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(meal1.getId(), USER_ID));
    }

    @Test
    public void deletedNotFoundNoMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deletedNotFoundNoUser() {
        assertThrows(NotFoundException.class, () -> service.delete(meal1.getId(), UserTestData.NOT_FOUND));
    }

    @Test
    public void deletedNotFoundForeignUser() {
        assertThrows(NotFoundException.class, () -> service.delete(meal1.getId(), ADMIN_ID));
    }

    @Test
    public void get() {
        Meal meal = service.get(meal1.getId(), USER_ID);
        assertMatch(meal, meal1);
    }

    @Test
    public void getNotFoundNoMeal() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotFoundNoUser() {
        assertThrows(NotFoundException.class, () -> service.get(meal1.getId(), UserTestData.NOT_FOUND));
    }

    @Test
    public void getNotFoundForeignUser() {
        assertThrows(NotFoundException.class, () -> service.get(meal1.getId(), ADMIN_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> filtered = service.getBetweenInclusive(DATE1, DATE1, USER_ID);
        assertMatch(filtered, meal3, meal2, meal1);
    }

    @Test
    public void getBetweenInclusiveNoLimits() {
        List<Meal> filtered = service.getBetweenInclusive(null, null, USER_ID);
        assertMatch(filtered, meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void getBetweenInclusiveLeftLimit() {
        List<Meal> filtered = service.getBetweenInclusive(DATE2, null, USER_ID);
        assertMatch(filtered, meal7, meal6, meal5, meal4);
    }

    @Test
    public void getBetweenInclusiveRightLimit() {
        List<Meal> filtered = service.getBetweenInclusive(null, DATE1, USER_ID);
        assertMatch(filtered, meal3, meal2, meal1);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), getUpdated());
    }

    @Test
    public void updateNotFoundNoMeal() {
        Meal updated = getUpdated();
        updated.setId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.update(updated, USER_ID));
    }

    @Test
    public void updateNotFoundNoUser() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), UserTestData.NOT_FOUND));
    }

    @Test
    public void updateNotFoundForeignUser() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), ADMIN_ID));
    }
}