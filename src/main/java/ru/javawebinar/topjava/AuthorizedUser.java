package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.User;

import java.util.HashSet;
import java.util.Set;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class AuthorizedUser {

    private static int id = 1;

    private static int caloriesPerDay = DEFAULT_CALORIES_PER_DAY;

    public static int id() {
        return id;
    }

    public static void setId(int userId) { id = userId; }

    public static void setCaloriesPerDay(int userCaloriesPerDay) { caloriesPerDay = userCaloriesPerDay; }

    public static int getCaloriesPerDay() {
        return caloriesPerDay;
    }

}