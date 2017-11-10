package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsUtil {

    private static final Logger log = getLogger(MealsUtil.class);

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static List<MealWithExceed> getListWithExceeded(List<Meal> meals, int caloriesPerDay ) {
        List<MealWithExceed> filteredWithExceeded = getFilteredWithExceeded(meals, LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
        log.debug("get list with execed:" + filteredWithExceeded);
        return filteredWithExceeded;
    }

    public static List<MealWithExceed> getFilteredWithExceeded(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(Collectors.groupingBy(Meal::getDate)).values().stream().
                flatMap(mealsByDay -> {
                    boolean exceed = mealsByDay.stream().mapToInt(Meal::getCalories).sum() > caloriesPerDay;
                    return mealsByDay.stream().filter(m -> TimeUtil.isBetween(m.getTime(), startTime, endTime)).
                            map(m -> createWithExceed(m, exceed));
                }).collect(Collectors.toList());
    }

    public static MealWithExceed createWithExceed(Meal meal, boolean exceeded) {
        return new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }
}