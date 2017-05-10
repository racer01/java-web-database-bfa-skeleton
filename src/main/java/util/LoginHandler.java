package util;

import model.User;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginHandler {
    private static LoginHandler ourInstance = new LoginHandler();
    public String loginUrl;
    private List<User> userList = new ArrayList<>();

    private LoginHandler() {
        loginUrl = "/login";
        userList.add(new User("Petőfi Sándor", "a", "a"));
    }

    public static LoginHandler getInstance() {
        return ourInstance;
    }

    public void redirectLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUrl);
    }

    public User checkLogin(HttpSession session) {
        Object authAttr = session.getAttribute("auth");
        if (authAttr instanceof String) {
            String currentUser = (String) authAttr;
            for (User user : userList) {
                if (user.getUsername().equals(currentUser)) {
                    return user;
                }
            }
        }
        return null;
    }

    public boolean login(HttpSession session, String username, String password) {
        if (userList.contains(new User(username, password))) {
            session.setAttribute("auth", username);
            return true;
        }
        return false;
    }

    public boolean logout(HttpSession session) {
        session.removeAttribute("auth");
        return false;
    }
}
