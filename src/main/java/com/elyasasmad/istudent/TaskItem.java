package com.elyasasmad.istudent;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TaskItem {

    private String taskName;
    private String courseName;
    private String dueDate;
    private String remainingDays;

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setRemainingDays(String remainingDays) {
        this.remainingDays = remainingDays;
    }

    public TaskItem() {}

    public TaskItem(String taskName, String courseName, String dueDate, String remainingDays) {
        this.taskName = taskName;
        this.courseName = courseName;
        this.dueDate = dueDate;
        this.remainingDays = remainingDays;
    }

    private VBox generateControl() {
        VBox container = new VBox();
        container.setPrefHeight(80.0);
        container.setPrefWidth(420.0);
        container.setSpacing(5.0);
        container.setId("container");
        container.getStylesheets().add(getClass().getResource("styles/task-item.css").toExternalForm());

        Label taskText = new Label();
//        Text taskText = new Text();
        taskText.setText(taskName);
        taskText.setTextOverrun(OverrunStyle.WORD_ELLIPSIS);
        taskText.setId("task-title");

//        Text courseText = new Text();
        Label courseText = new Label();
        courseText.setText(courseName);
        courseText.setId("course-name");

        HBox mainDueContainer = new HBox();
        HBox dueDateContainer = new HBox();
        HBox.setHgrow(dueDateContainer, Priority.ALWAYS);

        Text dueDateText = new Text();
        dueDateText.setText(dueDate);

        Text remainingDaysText = new Text();
        remainingDaysText.setText(remainingDays);

        dueDateContainer.getChildren().add(dueDateText);

        mainDueContainer.getChildren().addAll(dueDateContainer, remainingDaysText);

        container.getChildren().addAll(taskText, courseText, mainDueContainer);

        return container;
    }

    public VBox getControl() {
        return generateControl();
    }

}
