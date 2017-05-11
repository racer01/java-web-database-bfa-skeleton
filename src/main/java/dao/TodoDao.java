package dao;

import model.Task;

import java.util.List;

public interface TodoDao {
    Task getTask(int id, int userid);
    // addNewTask
    Task addTask(String newTitle, String newContent, int userid);
    // deleteTask
    Task deleteTask(int id, int userid);
    // updateTask
    Task updateTask(int id, String newTitle, String newContent, Integer newStatus, int newUserid);
    // toggle status
    Task toggleTask(int id, int userid);
    // listTasks
    List<Task> listTasks(int userid);
    // listFilteredTasks
    List<Task> listTasks(Integer filterId, String filterTitle, String filterContent, Integer filterStatus, int filterUserId);
}
