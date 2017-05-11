package dao;

import model.Task;
import model.Task.Status;
import org.apache.commons.lang3.StringUtils;
import util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TodoDaoMySQL implements TodoDao {
    private static TodoDaoMySQL ourInstance = new TodoDaoMySQL();
    private final Connection conn;
    private final String table = "tasks";

    private TodoDaoMySQL() {
        conn = ConnectionUtil.getConnection(ConnectionUtil.DatabaseName.rcrTodo);
    }

    public static TodoDaoMySQL getInstance() {
        return ourInstance;
    }

    @Override
    public Task getTask(int id, int userid) {
        String getTaskSql = "SELECT `taskid`,`title`,`content`,`isDone`,`userid`" +
            " FROM " + table + " WHERE taskid = ? AND userid = ?;";
        try {
            PreparedStatement getTaskStatement = conn.prepareStatement(getTaskSql);

            getTaskStatement.setInt(1, id);
            getTaskStatement.setInt(2, userid);

            ResultSet taskSet = getTaskStatement.executeQuery();
            if (taskSet.next()) {
                int taskId = taskSet.getInt("taskid");
                String taskTitle = taskSet.getString("title");
                String taskContent = taskSet.getString("content");
                int taskStatus = taskSet.getInt("isDone");
                int taskUserid = taskSet.getInt("userid");
                Task task = new Task(taskId, taskTitle, taskContent, taskStatus, taskUserid);
                return task;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Task addTask(String newTitle, String newContent, int userid) {
        String addTaskSql = "INSERT INTO " + table + " (title, content, userid)" +
            "VALUES (?, ?, ?);";
        try {
            PreparedStatement addTaskStatement = conn.prepareStatement(addTaskSql, PreparedStatement.RETURN_GENERATED_KEYS);

            addTaskStatement.setString(1, newTitle);
            addTaskStatement.setString(2, newContent);
            addTaskStatement.setInt(3, userid);

            int taskN = addTaskStatement.executeUpdate();
            ResultSet generatedSet = addTaskStatement.getGeneratedKeys();
            if (taskN == 1 && generatedSet.next()) {
                int id = generatedSet.getInt(1);
                return new Task(id, newTitle, newContent, userid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Task deleteTask(int id, int userid) {
        String deleteTaskSql = "DELETE FROM " + table + " WHERE `taskid` = ? AND `userid` = ?";
        Task deletedTask = getTask(id, userid);
        try {
            PreparedStatement deleteTaskStatement = conn.prepareStatement(deleteTaskSql);

            deleteTaskStatement.setInt(1, id);
            deleteTaskStatement.setInt(2, userid);

            int taskN = deleteTaskStatement.executeUpdate();
            if (taskN == 1) {
                return deletedTask;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Task updateTask(int id, String newTitle, String newContent, Integer newStatus, int newUserid) {
        String updateTaskSql = "UPDATE " + table + " SET ";

        List<String> newKeyList = new ArrayList<>();
        newKeyList.add("`userid` = ?");
        if (newTitle != null) {
            newKeyList.add("`title` = ?");
        }
        if (newContent != null) {
            newKeyList.add("`content` = ?");
        }
        if (newStatus != null) {
            newKeyList.add("`isDone` = ?");
        }
        String updates = StringUtils.join(newKeyList, ", ");
        updateTaskSql += updates;
        updateTaskSql += " WHERE taskid = ?;";

        try {
            PreparedStatement updateTaskStatement = conn.prepareStatement(updateTaskSql);

            updateTaskStatement.setInt(1, newUserid);
            int counter = 2;
            if (newTitle != null) {
                updateTaskStatement.setString(counter, newTitle);
                counter += 1;
            }
            if (newContent != null) {
                updateTaskStatement.setString(counter, newContent);
                counter += 1;
            }
            if (newStatus != null) {
                updateTaskStatement.setInt(counter, newStatus);
                counter += 1;
            }
            updateTaskStatement.setInt(counter, id);

            int taskN = updateTaskStatement.executeUpdate();
            if (taskN == 1) {
                return getTask(id, newUserid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Task toggleTask(int id, int userid) {
        Task toggledTask = getTask(id, userid);
        toggledTask.toggleStatus();
        return updateTask(id, null, null, toggledTask.getStatusValue(), userid);
    }

    @Override
    public List<Task> listTasks(int userid) {
        return listTasks(null, null, null, null, userid);
    }

    @Override
    public List<Task> listTasks(Integer filterId, String filterTitle, String filterContent, Integer filterStatus, int filterUserId) {
        String listTasksSql = "SELECT `taskid`,`title`,`content`,`isDone`,`userid`" +
            " FROM " + table + " WHERE `userid` = ? ";

        List<String> conditionKeyList = new ArrayList<>();
        if (filterId != null) {
            conditionKeyList.add("`taskid` = ?");
        }
        if (filterTitle != null) {
            conditionKeyList.add("`title` = ?");
        }
        if (filterContent != null) {
            conditionKeyList.add("`content` = ?");
        }
        if (filterStatus != null) {
            conditionKeyList.add("`isDone` = ?");
        }

        if (conditionKeyList.size() > 0) {
            String condition = " AND " + StringUtils.join(conditionKeyList, " AND ");
            listTasksSql += condition;
        }
        listTasksSql += ";";

        try {
            PreparedStatement listTasksStatement = conn.prepareStatement(listTasksSql);

            listTasksStatement.setInt(1, filterUserId);
            int counter = 2;
            if (filterId != null) {
                listTasksStatement.setInt(counter, filterId);
                counter += 1;
            }
            if (filterTitle != null) {
                listTasksStatement.setString(counter, filterTitle);
                counter += 1;
            }
            if (filterContent != null) {
                listTasksStatement.setString(counter, filterContent);
                counter += 1;
            }
            if (filterStatus != null) {
                listTasksStatement.setInt(counter, filterStatus);
                counter += 1;
            }

            ResultSet taskListSet = listTasksStatement.executeQuery();
            List<Task> tasks = new ArrayList<>();
            while (taskListSet.next()) {
                int id = taskListSet.getInt("taskid");
                String title = taskListSet.getString("title");
                String content = taskListSet.getString("content");
                int status = taskListSet.getInt("isDone");
                int userid = taskListSet.getInt("userid");
                Task task = new Task(id, title, content, status, userid);
                tasks.add(task);
            }

            return tasks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
