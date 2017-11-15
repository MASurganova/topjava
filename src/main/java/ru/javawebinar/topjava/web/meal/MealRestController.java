package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;
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

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        return getWithExceeded(service.getAll(AuthorizedUser.id()), AuthorizedUser.getCaloriesPerDay());
    }
    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, AuthorizedUser.id());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, AuthorizedUser.id());
    }

    public void delete(int id) throws NotFoundException{
        log.info("delete {}", id);
        service.delete(id, AuthorizedUser.id());
    }

    public void update(Meal meal, int id) throws NotFoundException{
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, AuthorizedUser.id());
    }

    public List<MealWithExceed> getByDateTime(LocalDate startDate, LocalDate endDate,
                                              LocalTime startTime, LocalTime endTime) {
        log.info("getByDateTime {}", startDate + " " + endDate + " " + startTime + " " + endTime);
        startDate = startDate == null ? LocalDate.MIN : startDate;
        endDate = endDate == null ? LocalDate.MAX: endDate;
        startTime = startTime == null ? LocalTime.MIN: startTime;
        endTime = endTime == null ? LocalTime.MAX: endTime;
        return getFilteredWithExceeded(service.getByDate(AuthorizedUser.id(), startDate, endDate),
                startTime, endTime, AuthorizedUser.getCaloriesPerDay());
    }
}