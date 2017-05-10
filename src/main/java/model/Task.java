package model;

public class Task {
    private int id;
    private String title;
    private String content;
    private boolean done;

    public Task(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.done = false;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
