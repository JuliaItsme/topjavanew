package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.dto.MealTo;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;
    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll for user {}", SecurityUtil.getAuthUserId());
        return MealsUtil.getTos(service.getAll(SecurityUtil.getAuthUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get meal {} for user {} ", id, SecurityUtil.getAuthUserId());
        return service.get(id, SecurityUtil.getAuthUserId());
    }

    public Meal create(Meal m) {
        log.info("create meal {} for user {}", m, SecurityUtil.getAuthUserId());
        checkNew(m);
        return service.create(m, SecurityUtil.getAuthUserId());
    }

    public void delete(int id) {
        log.info("delete meal {} for user {}", id, SecurityUtil.getAuthUserId());
        service.delete(id, SecurityUtil.getAuthUserId());
    }

    public void update(Meal m, int id) {
        log.info("update  meal {} for user {}", m, SecurityUtil.getAuthUserId());
        assureIdConsistent(m, id);
        service.update(m, SecurityUtil.getAuthUserId());
    }

    public List<MealTo> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.getAuthUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime,SecurityUtil.getAuthUserId());
        List<Meal> meals = service.getBetween(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(meals, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}