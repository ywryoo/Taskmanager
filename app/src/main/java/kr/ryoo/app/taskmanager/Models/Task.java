package kr.ryoo.app.taskmanager.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ywryo on 2016-12-03.
 */
@IgnoreExtraProperties
public class Task {
    private String name;
    private boolean done;
    private long timestamp;
    private String key;

    public Task() {

    }

    public Task(String name, boolean done,long timestamp) {
        this.name = name;
        this.done = done;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public boolean isKey(String key) {
        return key.equals(this.key);
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean getDone() {
        return done;
    }
    public long getTimestamp() {
        return timestamp;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("done", done);
        result.put("timestamp", timestamp);

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Task && ((Task) obj).getKey().equals(key);
    }

}
