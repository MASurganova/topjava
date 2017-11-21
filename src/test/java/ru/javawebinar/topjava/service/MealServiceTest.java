package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
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
    private int firstId;
    private int countUserMeals;

    @Before
    public void setUp() throws Exception {
        List<Meal> userMeals = service.getAll(USER_ID);
        firstId = userMeals.stream().sorted(Comparator.comparing(Meal::getId))
                .findFirst().get().getId();
        countUserMeals = userMeals.size();
    }

    @Test
    public void get() throws Exception {
        Meal meal = service.get(firstId, USER_ID);
        assertMatch(meal, MealTestData.USER_MEALS.get(0));
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        Meal meal = service.get(firstId + countUserMeals + 10, USER_ID);
    }

    @Test
    public void getOwerUser() throws Exception {
        Meal meal = service.get(firstId + countUserMeals, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getAlienMeal() throws Exception {
        Meal meal = service.get(firstId + countUserMeals, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAlienMeal() throws Exception {
        service.delete(firstId + countUserMeals, USER_ID);
    }

    @Test
    public void delete() throws Exception {
        service.delete(firstId + countUserMeals, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEALS.get(1));
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        LocalDateTime start = LocalDateTime.of(2015, 5, 31, 8, 0);
        LocalDateTime end = LocalDateTime.of(2015, 5, 31, 15, 0);
        List<Meal> actual = service.getBetweenDateTimes(start, end, USER_ID);
        List<Meal> expected = USER_MEALS.stream().filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(),
                start, end)).sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        assertMatch(actual, expected);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID),
                USER_MEALS.stream().sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList()));
    }

    @Test
    public void update() throws Exception {
        Meal updated = new Meal(USER_MEALS.get(0));
        updated.setDescription("UpdatedDescription");
        updated.setCalories(330);
        updated.setId(firstId);
        service.update(updated, USER_ID);
        assertMatch(service.get(firstId, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateAlienMeal() throws Exception {
        Meal updated = new Meal(USER_MEALS.get(0));
        updated.setDescription("UpdatedDescription");
        updated.setId(firstId);
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.now(), "NewMeal",   1555);
        Meal created = service.create(newMeal, ADMIN_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(ADMIN_ID), newMeal, ADMIN_MEALS.get(1), ADMIN_MEALS.get(0));
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateTimeCreate() throws Exception {
        service.create(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0),
                "Duplicate", 1000),USER_ID);
    }

}