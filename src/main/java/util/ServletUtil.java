package util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletUtil {
    public static void sendStatus(HttpServletResponse response, int status) {
        try {
            response.setStatus(status);
            response.getWriter().print("{}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
