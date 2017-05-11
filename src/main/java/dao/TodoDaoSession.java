package dao;

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
        Object tasksObj = this.session.getAttribute("tasks");
        if (tasksObj == null) {
            this.tasks = new ArrayList<>();
        } else {
            if (tasksObj instanceof List) {
                this.tasks = (List<Task>) this.session.getAttribute("tasks");
            }
        }
        counter = this.tasks.size();
    }

    @Override
    public Task getTask(int id, int userid) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    @Override
    public Task addTask(String newTitle, String newContent, int userid) {
        Task newTask = new Task(counter, newTitle, newContent, userid);
        tasks.add(newTask);
        counter += 1;
        session.setAttribute("tasks", tasks);
        return newTask;
    }

    @Override
    public Task deleteTask(int id, int userid) {
        Task removedTask = getTask(id, userid);
        if (removedTask != null) {
            tasks.remove(removedTask);
        }
        session.setAttribute("tasks", tasks);
        return removedTask;
    }

    @Override
    public Task updateTask(int id, String newTitle, String newContent, Integer newStatus, int newUserid) {
        Task updatedTask = getTask(id, newUserid);
        if (newTitle != null) {
            updatedTask.setTitle(newTitle);
        }
        if (newContent != null) {
            updatedTask.setContent(newContent);
        }
        if (newStatus != null) {
            updatedTask.setStatus(newStatus);
        }
        session.setAttribute("tasks", tasks);
        return updatedTask;
    }

    @Override
    public Task toggleTask(int id, int userid) {
        Task task = getTask(id, userid);
        task.toggleStatus();
        session.setAttribute("tasks", tasks);
        return task;
    }

    @Override
    public List<Task> listTasks(int userid) {
        return listTasks(null, null, null, null, userid);
    }

    @Override
    public List<Task> listTasks(Integer filterId, String filterTitle, String filterContent, Integer filterStatus, int filterUserId) {
        List<Task> filteredList = new ArrayList<>();
        for (Task task : tasks) {
            if ((filterId == null || filterId.equals(task.getId()))
                && (filterTitle == null || filterTitle.equals(task.getTitle()))
                && (filterContent == null || filterContent.equals(task.getContent()))
                && (filterStatus == null || filterStatus.equals(task.getStatusValue()))) {
                filteredList.add(task);
            }
        }
        return filteredList;
    }
}
