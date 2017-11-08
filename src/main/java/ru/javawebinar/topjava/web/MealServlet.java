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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        log.debug("redirect to meals");
//
//        resp.sendRedirect("meals.jsp");
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.setCharacterEncoding("UTF-8");
//
//    }

    private static String INSERT_OR_EDIT = "meals.jsp";
    private static String LIST_USER = "meals.jsp";
    private MealDao dao;

    public MealServlet() {
        super();
        dao = new MealDaoTest();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward="";
//        String action = request.getParameter("action");

//        if (action.equalsIgnoreCase("delete")){
//            int userId = Integer.parseInt(request.getParameter("id"));
//            dao.delete(userId);
//            forward = LIST_USER;
//            request.setAttribute("meals", dao.getList());
//        } else if (action.equalsIgnoreCase("edit")){
//            forward = INSERT_OR_EDIT;
//            int userId = Integer.parseInt(request.getParameter("id"));
//            Meal meal = dao.getByID(userId);
//            request.setAttribute("meal", meal);
//        } else if (action.equalsIgnoreCase("list")){
//            forward = LIST_USER;
//            request.setAttribute("meals", dao.getList());
//        } else {
//            forward = INSERT_OR_EDIT;
//        }
        request.setAttribute("localDateTimeFormat", new SimpleDateFormat("yyyy-MM-dd hh:mm"));
        request.setAttribute("meals", MealsUtil.getListWithExceeded(dao.getList(), 2000));
        forward = LIST_USER;
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Meal meal = new Meal();
//        meal.setDescription(request.getParameter("description"));
//        try {
//            int calories = Integer.parseInt(request.getParameter("calories"));
//            meal.setCalories(calories);
//            LocalDateTime dt = LocalDateTime.of (LocalDate.parse(request.getParameter("date")),
//                    LocalTime.parse(request.getParameter("time")));
//            meal.setDateTime(dt);
//        } catch (Exception e) {
//            log.debug(e.getMessage());
//        }
//        String id = request.getParameter("id");
//        if(id == null || id.isEmpty())
//        {
//            dao.add(meal);
//        }
//        else
//        {
//            meal.setId(Integer.parseInt(id));
//            dao.update(meal);
//        }
//        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
//        request.setAttribute("users", dao.getList());
//        view.forward(request, response);
//    }
}
