package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Controller
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealRestController mealController;

    @GetMapping(value = "/meals", params = "action=update")
    public String getUpdate(Model model, @RequestParam("id") int id) {
        model.addAttribute(mealController.get(id));
        return "mealForm";
    }

    @GetMapping(value = "/meals", params = "action=create")
    public String getUpdate(Model model) {
        model.addAttribute(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    // https://stackoverflow.com/questions/40274353/how-to-use-localdatetime-requestparam-in-spring-i-get-failed-to-convert-string
    @GetMapping(value = "/meals", params = "action=filter")
    public String filter(Model model,
                         @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                         @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                         @RequestParam(value = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                         @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        model.addAttribute("meals",
                mealController.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping(value = "/meals")
    public String getAll(Model model) {
        model.addAttribute("meals", mealController.getAll());
        return "meals";
    }

    @GetMapping(value = "/meals", params = "action=delete")
    public String delete(@RequestParam("id") int id) {
        mealController.delete(id);
        return "redirect:meals";
    }

    @PostMapping(value = "/meals")
    public String postUpdate(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                             @RequestParam("description") String description,
                             @RequestParam("calories") int calories) {
        Meal meal = new Meal(id, dateTime, description, calories);
        if (id != null) mealController.update(meal, id);
        else mealController.create(meal);
        return "redirect:meals";
    }
}
