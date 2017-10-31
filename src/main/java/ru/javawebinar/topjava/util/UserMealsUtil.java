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
        int summ = 0;
        List<UserMeal> mealPerDay = new ArrayList<>();
        for(UserMeal meal : sortedMealList) {
            LocalDate date = meal.getDateTime().toLocalDate();
            if(prevDate.equals(date)) {
                mealPerDay.add(meal);
                summ += meal.getCalories();
            } else {
                boolean exceed = summ > caloriesPerDay;
                for (UserMeal mealExceed : mealPerDay)
                    if(TimeUtil.isBetween(mealExceed.getDateTime().toLocalTime(), startTime, endTime)) {
                        list.add(new UserMealWithExceed(
                                mealExceed.getDateTime(), mealExceed.getDescription(),
                                mealExceed.getCalories(), exceed));
                        System.out.println("result = " + mealExceed + " " + exceed);
                    }
                mealPerDay = new ArrayList<>();
                mealPerDay.add(meal);
                summ = meal.getCalories();
                prevDate = date;
                System.out.println();
            }
        }
        boolean exceed = summ > caloriesPerDay;
        for (UserMeal mealExceed : mealPerDay)
            if(TimeUtil.isBetween(mealExceed.getDateTime().toLocalTime(), startTime, endTime)) {
                list.add(new UserMealWithExceed(
                        mealExceed.getDateTime(), mealExceed.getDescription(),
                        mealExceed.getCalories(), exceed));
                System.out.println("result = " + mealExceed + " " + exceed);
            }
        return list;
    }

}
