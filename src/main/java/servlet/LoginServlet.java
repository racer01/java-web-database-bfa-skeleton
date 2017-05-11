package servlet;

import dao.LoginDao;
import dao.LoginDaoMySQL;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginDao loginDao = LoginDaoMySQL.getInstance();
        Map<String, String[]> parameterMap = request.getParameterMap();

        if (!parameterMap.containsKey("method")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        String method = parameterMap.get("method")[0];

        if (parameterMap.containsKey("user") && parameterMap.containsKey("pass")) {
            String username = request.getParameter("user");
            String password = request.getParameter("pass");

            if (method.equals("login")) {
                User currentUser = loginDao.login(request.getSession(), username, password);
                if (currentUser != null) {
                    response.sendRedirect("/");
                    return;
                } else {
                    response.sendRedirect("login?loginerror");
                    return;
                }
            } else if (method.equals("register")) {
                User newUser = loginDao.registerUser(username, password);
                if (newUser != null) {
                    response.sendRedirect("login?regsuccess");
                    return;
                } else {
                    response.sendRedirect("login?regerror");
                    return;
                }
            }
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginDao loginDao = LoginDaoMySQL.getInstance();
        loginDao.logout(request.getSession());
        if (request.getParameterMap().containsKey("loginerror")) {
            request.setAttribute("errortype", "loginerror");
            request.setAttribute("errormsg", "Wrong username or password");
        } else if (request.getParameterMap().containsKey("regerror")) {
            request.setAttribute("errortype", "regerror");
            request.setAttribute("errormsg", "Username taken. Try another username!");

        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
