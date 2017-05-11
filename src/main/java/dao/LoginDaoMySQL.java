package dao;

import model.Task;
import model.User;
import util.ConnectionUtil;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDaoMySQL implements LoginDao {
    private static LoginDaoMySQL ourInstance = new LoginDaoMySQL();
    private final Connection conn;
    private final String loginURI = "login";
    private final String table = "users";


    private LoginDaoMySQL() {
        conn = ConnectionUtil.getConnection(ConnectionUtil.DatabaseName.rcrTodo);
    }

    public static LoginDaoMySQL getInstance() {
        return ourInstance;
    }

    @Override
    public User checkLogin(HttpSession session) {
        String loginSql = "SELECT `userid`,`username`,`password` FROM " + table + " WHERE username=? AND password=?";

        Object authAttr = session.getAttribute("auth");
        if (authAttr instanceof User) {
            User currentUser = (User) authAttr;
            try {
                PreparedStatement loginStatement = conn.prepareStatement(loginSql);
                loginStatement.setString(1, currentUser.getUsername());
                loginStatement.setString(2, currentUser.getPassword());
                ResultSet userSet = loginStatement.executeQuery();
                if (userSet.next()) {
                    return currentUser;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public User login(HttpSession session, String username, String password) {
        String loginSql = "SELECT `userid`,`username`,`password` FROM " + table + " WHERE username=? AND password=?";

        try {
            PreparedStatement loginStatement = conn.prepareStatement(loginSql);
            loginStatement.setString(1, username);
            loginStatement.setString(2, password);
            ResultSet userSet = loginStatement.executeQuery();
            if (userSet.next()) {
                int userid = userSet.getInt("userid");
                String user = userSet.getString("username");
                String pass = userSet.getString("password");
                User currentUser = new User(userid, user, pass);
                session.setAttribute("auth", currentUser);
                return currentUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean logout(HttpSession session) {
        session.removeAttribute("auth");
        return true;
    }

    @Override
    public String getLoginURI() throws IOException {
        return loginURI;
    }

    @Override
    public User registerUser(String newUsername, String newPassword) {
        String registerSql = "INSERT INTO " + table + " (username, password)" +
            "VALUES (?, ?);";
        try {
            PreparedStatement registerStatement = conn.prepareStatement(registerSql, PreparedStatement.RETURN_GENERATED_KEYS);

            registerStatement.setString(1, newUsername);
            registerStatement.setString(2, newPassword);

            int taskN = registerStatement.executeUpdate();
            ResultSet generatedSet = registerStatement.getGeneratedKeys();
            if (taskN == 1 && generatedSet.next()) {
                int id = generatedSet.getInt(1);
                return new User(id, newUsername, newPassword);
            }
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return null;
    }

}
