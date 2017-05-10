package servlet;

import util.LoginHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginHandler loginHandler = LoginHandler.getInstance();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.containsKey("user") && parameterMap.containsKey("pass")) {
            String username = request.getParameter("user");
            String password = request.getParameter("pass");
            if (loginHandler.login(request.getSession(), username, password)) {
                response.sendRedirect("/");
                return;
            }
        }
        response.sendRedirect("/login?error");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameterMap().containsKey("status")) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write("");
            out.flush();
            return;
        }
        LoginHandler.getInstance().logout(request.getSession());
        if (request.getParameterMap().containsKey("error")) {
            request.setAttribute("errormsg", "Wrong username or password");
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
