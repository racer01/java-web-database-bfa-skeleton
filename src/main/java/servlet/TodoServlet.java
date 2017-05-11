package servlet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.LoginDao;
import dao.LoginDaoMySQL;
import dao.TodoDao;
import dao.TodoDaoMySQL;
import model.Task;
import model.User;
import util.Convert;
import util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


@WebServlet("/todo/*")
public class TodoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginDao loginDao = LoginDaoMySQL.getInstance();
        User user = loginDao.checkLogin(request.getSession());
        if (user == null) {
            ServletUtil.sendStatus(response, HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String[] URIsplit = request.getRequestURI().split("/");
        String lastURI = URIsplit[URIsplit.length - 1];
        if (!lastURI.equals("todo")) {
            ServletUtil.sendStatus(response, HttpServletResponse.SC_CONFLICT);
            return;
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.containsKey("title")
            || !parameterMap.containsKey("content")) {
            ServletUtil.sendStatus(response, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        if (title == null
            || content == null) {
            ServletUtil.sendStatus(response, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        TodoDao dao = TodoDaoMySQL.getInstance();

        Task newTask = dao.addTask(title, content, user.getId());
        if (newTask != null) {
            //ServletUtil.sendStatus(response, HttpServletResponse.SC_CREATED);
            Type taskType = new TypeToken<Task>() {
            }.getType();
            Gson gson = new Gson();

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().print(gson.toJson(newTask, taskType));
            return;
        }
        ServletUtil.sendStatus(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginDao loginDao = LoginDaoMySQL.getInstance();
        User user = loginDao.checkLogin(request.getSession());
        if (user == null) {
            ServletUtil.sendStatus(response, HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        TodoDao dao = TodoDaoMySQL.getInstance();

        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        Gson gson = new Gson();

        String[] URIsplit = request.getRequestURI().split("/");
        String lastURI = URIsplit[URIsplit.length - 1];
        List<Task> tasks;
        if (lastURI.equals("todo")) {
            String strid = request.getParameter("id");
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String strstatus = request.getParameter("status");

            Integer id = strid != null ? Integer.valueOf(strid) : null;
            Integer status = strstatus != null ? Integer.valueOf(strstatus) : null;

            tasks = dao.listTasks(id, title, content, status, user.getId());
        } else {
            int id = Integer.valueOf(lastURI);
            tasks = dao.listTasks(id, null, null, null, user.getId());
            if (tasks.size() == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
        String json = gson.toJson(tasks, listType);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginDao loginDao = LoginDaoMySQL.getInstance();
        User user = loginDao.checkLogin(request.getSession());
        if (user == null) {
            ServletUtil.sendStatus(response, HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        TodoDao dao = TodoDaoMySQL.getInstance();
        String[] URIsplit = request.getRequestURI().split("/");
        String lastURI = URIsplit[URIsplit.length - 1];

        int statuscode = HttpServletResponse.SC_NOT_FOUND;
        if (lastURI == null) {
            statuscode = HttpServletResponse.SC_BAD_REQUEST;
        } else if (lastURI.equals("todo")) {
            statuscode = HttpServletResponse.SC_FORBIDDEN;
        } else {
            Integer id = Integer.valueOf(lastURI);
            int userid = user.getId();
            String[] paramKeys = {"title", "content", "status"};
            String[] paramVals = Convert.toParamValues(request.getInputStream(), paramKeys);
            if (paramVals != null && id != null) {
                String title = paramVals[0];
                String content = paramVals[1];
                String strstatus = paramVals[2];

                Integer status = null;
                if (strstatus != null) {
                    if (strstatus.equals("toggle")) {
                        dao.toggleTask(id, user.getId());
                    } else {
                        status = Integer.valueOf(strstatus);
                        dao.updateTask(id, title, content, status, userid);
                    }
                }
                statuscode = HttpServletResponse.SC_OK;
            }
        }
        ServletUtil.sendStatus(response, statuscode);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginDao loginDao = LoginDaoMySQL.getInstance();
        User user = loginDao.checkLogin(request.getSession());
        if (user == null) {
            ServletUtil.sendStatus(response, HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        TodoDao dao = TodoDaoMySQL.getInstance();

        String[] URIsplit = request.getRequestURI().split("/");
        String lastURI = URIsplit[URIsplit.length - 1];

        int statuscode = HttpServletResponse.SC_NOT_FOUND;
        if (lastURI == null) {
            statuscode = HttpServletResponse.SC_BAD_REQUEST;
        } else if (lastURI.equals("todo")) {
            statuscode = HttpServletResponse.SC_FORBIDDEN;
        } else {
            int id = Integer.valueOf(lastURI);
            Task deletedTask = dao.deleteTask(id, user.getId());
            if (deletedTask != null) {
                statuscode = HttpServletResponse.SC_OK;
            }
        }
        ServletUtil.sendStatus(response, statuscode);
    }
}
