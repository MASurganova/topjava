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

    private static String INSERT_OR_EDIT = "meal.jsp";
    private static String LIST_USER = "meals.jsp";
    private MealDao dao;
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");


    public MealServlet() {
        super();
        dao = new MealDaoTest();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id);
            log.debug("delete meal id = " + id);
            forward = LIST_USER;
            request.setAttribute("meals", MealsUtil.getListWithExceeded(dao.getList(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = dao.getByID(id);
            log.debug("forward to edit page, meal - " + meal);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("list")){
            forward = LIST_USER;
            log.debug("show meals list");
            request.setAttribute("meals", MealsUtil.getListWithExceeded(dao.getList(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
        } else {
            log.debug("forward to add page");
            forward = INSERT_OR_EDIT;
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
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
        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
        request.setAttribute("meals", MealsUtil.getListWithExceeded(dao.getList(), 2000));
        view.forward(request, response);
    }
}
