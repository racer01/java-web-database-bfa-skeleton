package servlet;

import dao.DatabaseHiDao;
import dao.HiDao;
import util.ConnectionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/hi")
public class HiServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String lang = req.getParameter("lang");

        try (Connection conn = ConnectionUtil.getConnection(ConnectionUtil.DatabaseName.BFA)) {
            HiDao dao = new DatabaseHiDao(conn);
            String msg = dao.getMsg(lang);
            req.setAttribute("msg", String.format(msg, name));
        } catch (Exception e) {
            throw new ServletException(e);
        }

        req.getRequestDispatcher("name.jsp").forward(req, resp);
    }
}
