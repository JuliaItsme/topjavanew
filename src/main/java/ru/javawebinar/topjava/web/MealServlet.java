package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealDao mealDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        mealDao = new MealDaoImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")), request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
        if (Objects.equals(request.getParameter("id"), "")) {
            log.info("Create {}", meal);
            mealDao.save(meal);
            response.sendRedirect("meals");
        } else {
            int id = getId(request);
            meal.setId(id);
            log.info("Update {}", meal);
            mealDao.update(meal, id);
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                mealDao.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ? new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0) : mealDao.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", MealsUtil.getMealTo(mealDao.getAll(), MealsUtil.CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private Integer getId(HttpServletRequest request) {
        String id = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(id);
    }
}
