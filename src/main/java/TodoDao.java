import model.Task;

import java.util.List;

public interface TodoDao {
    Task getTask(int id);
    // addNewTask
    Task addTask(String title, String content);
    // deleteTask
    Task deleteTask(int id);
    // updateTask
    Task updateTask(int id, String title, String content, Boolean isDone);
    // markTaskAsDone
    Task toggleTask(int id);
    // listTasks
    List<Task> listTasks();
    // listFilteredTasks
    List<Task> listTasks(Integer id, String title, String content, Boolean isDone);
}
