package dao;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class TodoDaoMem implements TodoDao {

    // public static final dao.TodoDao INSTANCE = new dao.TodoDaoMem();
    private List<Task> tasks;
    private int counter;

    private TodoDaoMem() {
        tasks = new ArrayList<>();
        counter = 0;
        addTask("title1", "content1", 0);  //0
        addTask("title2", "content2", 0);  //1
        addTask("title3", "content3", 0);  //2
        updateTask(0, null, null, 0, 0);
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
    public Task addTask(String title, String content, int userid) {
        Task newTask = new Task(counter, title, content, userid);
        tasks.add(newTask);
        counter += 1;
        return newTask;
    }

    @Override
    public Task deleteTask(int id, int userid) {
        Task removedTask = getTask(id, userid);
        if (removedTask != null) {
            tasks.remove(removedTask);
        }
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
        return updatedTask;
    }

    @Override
    public Task toggleTask(int id, int userid) {
        Task task = getTask(id, userid);
        task.toggleStatus();
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
