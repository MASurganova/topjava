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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExceeded;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExceeded;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealWithExceed> getAll() {
        int userId = AuthorizedUser.id();
        log.info("getAll for user {}", userId);
        return getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay());
    }
    public Meal get(int id) {
        int userId = AuthorizedUser.id();
        log.info("get meal {} fo user {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        int userId = AuthorizedUser.id();
        log.info("create {} for user {}", meal, userId);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void delete(int id) throws NotFoundException{
        int userId = AuthorizedUser.id();
        log.info("delete {} for user {}", id, userId);
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) throws NotFoundException{
        int userId = AuthorizedUser.id();
        log.info("update {} for user", meal, userId);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

    public List<MealWithExceed> getByDateTime(LocalDate startDate, LocalDate endDate,
                                              LocalTime startTime, LocalTime endTime) {
        int userId = AuthorizedUser.id();
        log.info("getByDateTime  dates({} - {}) time({} - {}) for user {}",
                startDate, endDate, startTime, endTime, userId);
        startDate = startDate == null ? LocalDate.MIN : startDate;
        endDate = endDate == null ? LocalDate.MAX: endDate;
        startTime = startTime == null ? LocalTime.MIN: startTime;
        endTime = endTime == null ? LocalTime.MAX: endTime;
        return getFilteredWithExceeded(service.getByDate(userId, startDate, endDate),
                startTime, endTime, AuthorizedUser.getCaloriesPerDay());
    }
}