package ru.javawebinar.topjava.web.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

@Component
public class MealDateTimeValidator implements Validator {

    @Autowired
    MealRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Meal.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var meal = (Meal) target;
        if (meal.getDateTime() == null) {
            return;
        }

        var otherMeal = repository.getByDateTime(meal.getDateTime(), SecurityUtil.authUserId());
        if (otherMeal != null && !otherMeal.getId().equals(meal.getId())) {
            errors.rejectValue("dateTime", "meal.dateTimeExists",
                    new Object[]{meal.getDateTime()}, "");
        }
    }
}
