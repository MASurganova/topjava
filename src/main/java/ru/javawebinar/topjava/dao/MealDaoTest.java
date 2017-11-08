package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MealDaoTest implements MealDao {
    private List<Meal> meals;
    private int count = 0;

    public MealDaoTest() {
        meals = new ArrayList<>(Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500, count++),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000, count++),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500, count++),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000, count++),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500, count++),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510, count++)
        ));
    }

    @Override
    public void add(Meal meal) {
        meal.setId(count++);
        meals.add(meal);
    }

    @Override
    public void delete(int id) {
        List<Meal> copy = new ArrayList<>(meals);
        for(Meal meal : copy)
            if (meal.getId() == id) meals.remove(meal);
    }

    @Override
    public void update(Meal meal) {
        meals.remove(meals.stream().
                filter(m -> m.getId() == meal.getId()).collect(Collectors.toList()).get(0));
        meals.add(meal);
    }

    @Override
    public List<Meal> getList() {
        return meals;
    }

    @Override
    public Meal getByID(int id) {
        return meals.stream().filter(m -> m.getId() == id).collect(Collectors.toList()).get(0);
    }
}
