package com.elyasasmad.istudent.models;

import org.json.JSONObject;

public class StudentTask {
    private final int taskID;
    private final String taskName;
    private final long timestamp;
    private final int courseID;
    private final String courseName;
    private final String shortName;

    public StudentTask(String rawData) {
        JSONObject taskObj = new JSONObject(rawData);
        JSONObject courseObj = new JSONObject(taskObj.getJSONObject("course").toString());

        this.taskID = taskObj.getInt("id");
        this.taskName = taskObj.getString("name");
        this.timestamp = taskObj.getLong("timestart");

        this.courseID = courseObj.getInt("id");
        this.courseName = courseObj.getString("fullname");
        this.shortName = courseObj.getString("shortname");
    }

    public int getTaskID() {
        return taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getShortName() {
        return shortName;
    }
}
