package kr.ryoo.app.taskmanager.Models;

/**
 * Created by ywryo on 2016-12-03.
 */

public class Task {
    private String name;
    private boolean done;
    private long timestamp;

    public Task() {

    }

    public Task(String name, boolean done,long timestamp) {
        this.name = name;
        this.done = done;
        this.timestamp = timestamp;
    }

    public String getTaskName() {
        return name;
    }

    public boolean isDone() {
        return done;
    }
    public long getTimestamp() {
        return timestamp;
    }

}
