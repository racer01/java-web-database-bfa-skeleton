package dao;

import model.User;

import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface LoginDao {
    // check login; return the user if logged in, else null
    User checkLogin(HttpSession session);

    User login(HttpSession session, String username, String password);

    boolean logout(HttpSession session);

    String getLoginURI() throws IOException;

    User registerUser(String username, String password);

}
