package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> list = new ArrayList<>();
        List<UserMeal> sortedMealList = new ArrayList<>(mealList);
        sortedMealList.sort(Comparator.comparing(UserMeal::getDateTime));
        LocalDate prevDate = sortedMealList.get(0).getDateTime().toLocalDate();
        List<UserMeal> mealPerDay = new ArrayList<>();
        for(UserMeal meal : sortedMealList) {
            LocalDate date = meal.getDateTime().toLocalDate();
            if(prevDate.equals(date)) {
                mealPerDay.add(meal);
            } else {
                boolean exceed =
                        mealPerDay.stream().map(UserMeal::getCalories).mapToInt(Integer::valueOf).sum() >
                                caloriesPerDay;
                mealPerDay.stream().
                        filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime)).
                        forEach(m -> list.add(new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(), exceed)));
                mealPerDay = new ArrayList<>();
                mealPerDay.add(meal);
                prevDate = date;
            }
        }
        boolean exceed = mealPerDay.stream().map(UserMeal::getCalories).mapToInt(Integer::valueOf).sum() >
                caloriesPerDay;
        mealPerDay.stream().
                filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime)).
                forEach(m -> list.add(new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(), exceed)));
        list.forEach(System.out :: println);
        return list;
    }

}
