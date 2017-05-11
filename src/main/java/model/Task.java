package model;

public class Task {
    private int id;
    private String title;
    private String content;
    private Status status;
    private int userid;

    public Task(int id, String title, String content, int userid) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = Status.NEW;
        this.userid = userid;
    }

    public Task(int id, String title, String content, int statusValue, int userid) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = getStatusFromValue(statusValue);
        this.userid = userid;
    }


    public Task(int id, String title, String content, Status status, int userid) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.userid = userid;
    }

    private Status getStatusFromValue(int status) {
        switch (status) {
            case 0:
                return Status.NEW;
            case 1:
                return Status.IN_PROGRESS;
            case 2:
                return Status.DONE;
        }
        return null;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(int statusValue) {
        this.status = getStatusFromValue(statusValue);
    }

    public int getStatusValue() {
        switch (status) {
            case NEW:
                return 0;
            case IN_PROGRESS:
                return 1;
            case DONE:
                return 2;
        }
        return -1;
    }

    public Status toggleStatus() {
        switch (status) {
            case NEW:
                status = Status.IN_PROGRESS;
                break;
            case IN_PROGRESS:
                status = Status.DONE;
                break;
            case DONE:
                status = Status.NEW;
                break;
        }
        return status;
    }

    public int getUserid() {
        return userid;
    }

    @Override
    public String toString() {
        return String.format("%d %s %s %s %d", id, title, content, status, userid);
    }

    public enum Status {
        NEW, IN_PROGRESS, DONE
    }
}
