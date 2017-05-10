import model.Task;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class TodoDaoSession implements TodoDao {
    private List<Task> tasks;
    private HttpSession session;
    private int counter;

    public TodoDaoSession(HttpSession session) {
        //List<Task> tasks
        this.session = session;
        if (this.session.getAttribute("tasks") == null) {
            this.tasks = new ArrayList<>();
        } else {
            this.tasks = (List<Task>)this.session.getAttribute("tasks");
        }
        counter = this.tasks.size();
    }

    @Override
    public Task getTask(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    @Override
    public Task addTask(String title, String content) {
        Task newTask = new Task(counter, title, content);
        tasks.add(newTask);
        counter += 1;
        session.setAttribute("tasks", tasks);
        return newTask;
    }

    @Override
    public Task deleteTask(int id) {
        Task removedTask = getTask(id);
        if (removedTask != null) {
            tasks.remove(removedTask);
        }
        session.setAttribute("tasks", tasks);
        return removedTask;
    }

    @Override
    public Task updateTask(int id, String title, String content, Boolean done) {
        Task updatedTask = getTask(id);
        if (title != null) {
            updatedTask.setTitle(title);
        }
        if (content != null) {
            updatedTask.setContent(content);
        }
        if (done != null) {
            updatedTask.setDone(done);
        }
        session.setAttribute("tasks", tasks);
        return updatedTask;
    }

    @Override
    public Task toggleTask(int id) {
        Task task = getTask(id);
        task.setDone(!task.isDone());
        session.setAttribute("tasks", tasks);
        return task;
    }

    @Override
    public List<Task> listTasks() {
        return listTasks(null, null, null, null);
    }

    @Override
    public List<Task> listTasks(Integer id, String title, String content, Boolean isDone) {
        List<Task> filteredList = new ArrayList<>();
        for (Task task : tasks) {
            if ((id == null || id.equals(task.getId()))
                && (title == null || title.equals(task.getTitle()))
                && (content == null || content.equals(task.getContent()))
                && (isDone == null || isDone.equals(task.isDone()))) {
                filteredList.add(task);
            }
        }
        return filteredList;
    }
}
