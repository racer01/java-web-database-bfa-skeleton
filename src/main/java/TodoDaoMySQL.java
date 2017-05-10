import model.Task;

import java.util.List;

public class TodoDaoMySQL implements TodoDao {
    private static TodoDaoMySQL ourInstance = new TodoDaoMySQL();

    public static TodoDaoMySQL getInstance() {
        return ourInstance;
    }

    private TodoDaoMySQL() {
    }

    @Override
    public Task getTask(int id) {
        return null;
    }

    @Override
    public Task addTask(String title, String content) {
        return null;
    }

    @Override
    public Task deleteTask(int id) {
        return null;
    }

    @Override
    public Task updateTask(int id, String title, String content, Boolean isDone) {
        return null;
    }

    @Override
    public Task toggleTask(int id) {
        return null;
    }

    @Override
    public List<Task> listTasks() {
        return null;
    }

    @Override
    public List<Task> listTasks(Integer id, String title, String content, Boolean isDone) {
        return null;
    }
}
