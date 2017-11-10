package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class MealsUtil {

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static List<MealWithExceed> getListWithExceeded(List<Meal> meals, int caloriesPerDay ) {
        return getFilteredWithExceeded(meals, LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
    }

    public static List<MealWithExceed> getFilteredWithExceeded(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(Collectors.groupingBy(Meal::getDate)).values().stream().flatMap(mealsByDay -> {
            boolean exceed = mealsByDay.stream().mapToInt(Meal::getCalories).sum() > caloriesPerDay;
            return meals.stream().filter(m -> TimeUtil.isBetween(m.getTime(), startTime, endTime)).map(m -> createWithExceed(m, exceed));
        }).collect(Collectors.toList());
    }

    public static MealWithExceed createWithExceed(Meal meal, boolean exceeded) {
        return new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }
}