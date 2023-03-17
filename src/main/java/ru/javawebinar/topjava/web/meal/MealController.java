package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.service.MealService;

public class MealController {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final MealService service;

    public MealController(MealService service) {
        this.service = service;
    }
}
