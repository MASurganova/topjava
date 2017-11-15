package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    @Autowired
    ProfileRestController restController;

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        return getFilteredWithExceeded(service.getAll(restController.get().getId()),
                LocalTime.MIN, LocalTime.MAX, restController.get().getCaloriesPerDay());
    }
    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, restController.get().getId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, restController.get().getId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, restController.get().getId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, restController.get().getId());
    }

    private List<MealWithExceed> getByDate(LocalDate start, LocalDate end) {
        log.info("getByDate {}", start + " " + end);
        return getFilteredWithExceeded(service.getByDate(restController.get().getId(), start, end),
                LocalTime.MIN, LocalTime.MAX, restController.get().getCaloriesPerDay());
    }

    public List<MealWithExceed> getByDateTime(LocalDate startDate, LocalDate endDate,
                                              LocalTime startTime, LocalTime endTime) {
        log.info("getByDateTime {}", startDate + " " + endDate + " " + startTime + " " + endTime);
        startDate = startDate == null ? LocalDate.MIN : startDate;
        endDate = endDate == null ? LocalDate.MAX: endDate;
        startTime = startTime == null ? LocalTime.MIN: startTime;
        endTime = endTime == null ? LocalTime.MAX: endTime;
        return getFilteredWithExceeded(service.getByDate(restController.get().getId(), startDate, endDate),
                startTime, endTime, restController.get().getCaloriesPerDay());
    }
}