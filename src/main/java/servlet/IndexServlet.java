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

@WebServlet("")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginDao loginDao = LoginDaoMySQL.getInstance();
        User user = loginDao.checkLogin(request.getSession());
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }
        request.setAttribute("auth", user);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
