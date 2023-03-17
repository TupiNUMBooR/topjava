package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("meals")
public class JspMealController extends MealController {
    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("update")
    public String getUpdate(Model model, @RequestParam("id") int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get update meal {} for user {}", id, userId);
        model.addAttribute(service.get(id, userId));
        return "/mealForm";
    }

    @GetMapping("create")
    public String getCreate(Model model) {
        log.info("create meal for user {}", SecurityUtil.authUserId());
        model.addAttribute(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "/mealForm";
    }

    // https://stackoverflow.com/questions/40274353/how-to-use-localdatetime-requestparam-in-spring-i-get-failed-to-convert-string
    @GetMapping()
    public String filter(Model model,
                         @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                         @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                         @RequestParam(value = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                         @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        int userId = SecurityUtil.authUserId();
        log.info("filter meals dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        var mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        var mealsTo = MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
        model.addAttribute("meals", mealsTo);
        return "/meals";
    }

    @GetMapping("delete")
    public String delete(@RequestParam("id") int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
        return "redirect:/meals";
    }

    @PostMapping
    public String postUpdate(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                             @RequestParam("description") String description,
                             @RequestParam("calories") int calories) {
        Meal meal = new Meal(id, dateTime, description, calories);
        int userId = SecurityUtil.authUserId();
        if (id != null) {
            log.info("update {} for user {}", meal, userId);
            service.update(meal, userId);
        } else {
            log.info("create {} for user {}", meal, userId);
            service.create(meal, userId);
        }
        return "redirect:/meals";
    }
}
