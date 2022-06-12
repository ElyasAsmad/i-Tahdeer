package com.elyasasmad.istudent;

import com.elyasasmad.istudent.models.LastLoginSession;
import com.elyasasmad.istudent.models.SiteInfoResponse;
import com.elyasasmad.istudent.models.StudentData;
import com.elyasasmad.istudent.models.StudentTask;
import com.elyasasmad.istudent.utils.ItaleemcClient;
import com.elyasasmad.istudent.utils.SessionData;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class DashboardController implements Initializable {

    private final ItaleemcClient italeemcClient = new ItaleemcClient();
    private final StudentData studentData = new StudentData();
    private LastLoginSession session;
    private ArrayList<StudentTask> studentTasks;

    @FXML
    private Label welcomeText;

    @FXML
    private Rectangle studentImageFrame;

    @FXML
    private TableColumn<String, String> nameCol;

    @FXML
    private Text studentName;

    @FXML
    private Text studentMatric;

    @FXML
    private Button logoutButton;

    @FXML
    private VBox container;

    @FXML
    private ScrollPane tasksContainer;

    @FXML
    private void logoutSession() {

        logoutButton.setDisable(true);
        logoutButton.setText("Logging out...");

        new Thread(logoutTask()).start();

    }

    private Task<Void> logoutTask() {
        Task<Void> _task = new Task<>() {
            @Override
            protected Void call() throws InterruptedException {
                String logoutTime = italeemcClient.logout(studentData.getToken());
                System.out.println("Logout time: " + logoutTime);

                Thread.sleep(1000);

                return null;
            }
        };

        _task.setOnSucceeded(event -> {
            navigateToLogin();
        });

        _task.setOnFailed(event -> {
            logoutButton.setDisable(false);
            logoutButton.setText("Re-attempt Logout");
        });

        return _task;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SessionData sessionData = new SessionData();
        this.session = sessionData.getLastSessionData();
        sessionData.close();

        if (this.session == null) navigateToLogin();

        logoutButton.setDisable(true);

        studentData.setToken(session.getToken());

        // Get user data subroutine
        new Thread(getUserInfoTask()).start();

        // Get user tasks subroutine
        new Thread(getUserAssignmentsTask()).start();

    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Stage stage = (Stage) container.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Task<Void> getUserAssignmentsTask() {

       Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                studentTasks = italeemcClient.getUserTasks(session.getToken());
                return null;
            }
       };

        task.setOnFailed(event -> {
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });

        task.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                VBox allItems = new VBox();
                allItems.setSpacing(5.0);
                allItems.setMinWidth(400.0);
                allItems.setAlignment(Pos.CENTER);
                studentTasks.forEach(item -> {
                    System.out.println(item.getCourseName());
                    Date taskDueDate = new Date(item.getTimestamp() * 1000L);
                    Date todayDate = new Date();

                    LocalDate taskDueLocalDate = convertToLocalDate(taskDueDate);
                    LocalDate todayLocalDate = convertToLocalDate(todayDate);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                    long daysBetween = ChronoUnit.DAYS.between(todayLocalDate, taskDueLocalDate);

                    TaskItem taskItem = new TaskItem();
                    taskItem.setTaskName(item.getTaskName());
                    taskItem.setCourseName(item.getCourseName());
                    taskItem.setDueDate(sdf.format(taskDueDate));
                    taskItem.setRemainingDays(daysBetween + " days more");

                    allItems.getChildren().add(taskItem.getControl());
                });
                tasksContainer.setContent(allItems);
            });
        });

       return task;

    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Task<SiteInfoResponse> getUserInfoTask() {

        Task<SiteInfoResponse> task = new Task<>() {
            @Override
            protected SiteInfoResponse call() throws Exception {

                System.out.println("Using logged in session from " + session.getLoggedInTime());

                return italeemcClient.getUserInfo(session.getToken());
            }
        };

        task.setOnFailed(event -> {
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });

        task.setOnSucceeded(event -> {
            System.out.println(task.getValue().getUserPictureUrl());
            SiteInfoResponse response = task.getValue();

            String _pictureUrl = response.getUserPictureUrl();
            String _studentName = response.getFirstname();
            String _studentMatric = response.getLastname();

            ImagePattern pattern = new ImagePattern(new Image(_pictureUrl));
            studentImageFrame.setFill(pattern);

            studentName.setText(_studentName);
            studentMatric.setText(_studentMatric);

            studentData.setStudentName(_studentName);
            studentData.setMatricNumber(_studentMatric);
            studentData.setPictureUrl(_pictureUrl);

            logoutButton.setDisable(false);
        });

        task.setOnRunning(event -> {
            studentName.setText("Loading student name...");
            studentMatric.setText("Loading student matric number...");
        });

        return task;

    }

}