import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Task;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
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
    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void sendStatus(HttpServletResponse response, int status) {
        try {
            response.setStatus(status);
            response.getWriter().print("{}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getParamFromStream(ServletInputStream inputStream, String[] paramKeys) {
        String parameterString = convertStreamToString(inputStream); //1 string, json
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Gson gson = new Gson();
        Map<String, String> jsonMap = gson.fromJson(parameterString, mapType);

        if (jsonMap == null) {
            return null;
        }
        String[] paramValues = new String[paramKeys.length];
        for (int i = 0; i < paramKeys.length; i++) {
            String paramKey = paramKeys[i];
            if (jsonMap.containsKey(paramKey)) {
                paramValues[i] = jsonMap.get(paramKey);
            }
        }
        return paramValues;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] URIsplit = request.getRequestURI().split("/");
        String lastURI = URIsplit[URIsplit.length - 1];
        if (!lastURI.equals("todo")) {
            sendStatus(response, HttpServletResponse.SC_CONFLICT);
            return;
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.containsKey("title")
            || !parameterMap.containsKey("content")) {
            sendStatus(response, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        if (title == null
            || content == null) {
            sendStatus(response, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        TodoDao dao = new TodoDaoSession(request.getSession());
        dao.addTask(title, content);
        sendStatus(response, HttpServletResponse.SC_CREATED);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TodoDao dao = new TodoDaoSession(request.getSession());
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
            String strdone = request.getParameter("done");

            Integer id = strid != null ? Integer.valueOf(strid) : null;
            Boolean done = strdone != null ? Boolean.valueOf(strdone) : null;

            tasks = dao.listTasks(id, title, content, done);
        } else {
            int id = Integer.valueOf(lastURI);
            tasks = dao.listTasks(id, null, null, null);
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
        TodoDao dao = new TodoDaoSession(request.getSession());


        String[] URIsplit = request.getRequestURI().split("/");
        String lastURI = URIsplit[URIsplit.length - 1];

        int statuscode = HttpServletResponse.SC_NOT_FOUND;
        if (lastURI == null) {
            statuscode = HttpServletResponse.SC_BAD_REQUEST;
        } else if (lastURI.equals("todo")) {
            statuscode = HttpServletResponse.SC_FORBIDDEN;
        } else {
            Integer id = Integer.valueOf(lastURI);
            String[] paramKeys = {"title", "content", "done"};
            String[] paramVals = getParamFromStream(request.getInputStream(), paramKeys);
            if (paramVals != null && id != null) {
                String title = paramVals[0];
                String content = paramVals[1];
                String strdone = paramVals[2];

                Boolean done = null;
                if (strdone != null) {
                    if (strdone.equals("toggle")) {
                        dao.toggleTask(id);
                    } else {
                        done = Boolean.valueOf(strdone);
                    }
                }
                dao.updateTask(id, title, content, done);
                statuscode = HttpServletResponse.SC_OK;
            }
        }
        sendStatus(response, statuscode);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TodoDao dao = new TodoDaoSession(request.getSession());

        String[] URIsplit = request.getRequestURI().split("/");
        String lastURI = URIsplit[URIsplit.length - 1];

        int statuscode = HttpServletResponse.SC_NOT_FOUND;
        if (lastURI == null) {
            statuscode = HttpServletResponse.SC_BAD_REQUEST;
        } else if (lastURI.equals("todo")) {
            statuscode = HttpServletResponse.SC_FORBIDDEN;
        } else {
            int id = Integer.valueOf(lastURI);
            Task deletedTask = dao.deleteTask(id);
            if (deletedTask != null) {
                statuscode = HttpServletResponse.SC_OK;
            }
        }
        sendStatus(response, statuscode);
    }
}
