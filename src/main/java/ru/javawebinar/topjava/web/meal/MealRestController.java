package ru.javawebinar.topjava.web.meal;

import ru.javawebinar.topjava.service.MealService;

public class MealRestController extends MealController {
    public MealRestController(MealService service) {
        super(service);
    }
}