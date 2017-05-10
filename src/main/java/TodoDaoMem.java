import model.Task;

import java.util.ArrayList;
import java.util.List;

public class TodoDaoMem implements TodoDao {

    // public static final TodoDao INSTANCE = new TodoDaoMem();
    private List<Task> tasks;
    private int counter;

    private TodoDaoMem() {
        tasks = new ArrayList<>();
        counter = 0;
        addTask("title1", "content1");  //0
        addTask("title2", "content2");  //1
        addTask("title3", "content3");  //2
        updateTask(0, null, null, true);
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
        return newTask;
    }

    @Override
    public Task deleteTask(int id) {
        Task removedTask = getTask(id);
        if (removedTask != null) {
            tasks.remove(removedTask);
        }
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
        return updatedTask;
    }

    @Override
    public Task toggleTask(int id) {
        Task task = getTask(id);
        task.setDone(!task.isDone());
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
