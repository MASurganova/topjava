package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoTest;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealDao dao;
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");


    public MealServlet() {
        super();
        dao = new MealDaoTest();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action == null ? "list" : action) {
            case "delete": {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.delete(id);
                log.debug("delete meal id = " + id);
                request.setAttribute("meals", MealsUtil.getListWithExceeded(dao.getList(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("meals.jsp").forward(request, response);
            } break;
            case "edit":
            case "insert": {
                String id = request.getParameter("id");
                Meal meal = id == null ?
                        new Meal(LocalDateTime.now(), "", 1000) :
                        dao.getByID(Integer.parseInt(id));
                log.debug("forward to edit page, meal - " + meal);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
            }
            case "all":
            default: {
                log.debug("show meals list");
                request.setAttribute("meals",
                        MealsUtil.getListWithExceeded(dao.getList(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal();
        meal.setDescription(request.getParameter("description"));
        try {
            int calories = Integer.parseInt(request.getParameter("calories"));
            meal.setCalories(calories);
            LocalDateTime dt = LocalDateTime.of (LocalDate.parse(request.getParameter("date"), df),
                    LocalTime.parse(request.getParameter("time"), tf) );
            meal.setDateTime(dt);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        String id = request.getParameter("id");
        if(id == null || id.isEmpty())
        {
            dao.add(meal);
            log.debug("add meal: " + meal);
        }
        else
        {
            meal.setId(Integer.parseInt(id));
            dao.update(meal);
            log.debug("edit meal, new meal: " + meal);
        }
        request.setAttribute("meals", MealsUtil.getListWithExceeded(dao.getList(), 2000));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
