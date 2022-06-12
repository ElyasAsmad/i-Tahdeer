package com.elyasasmad.istudent;

import com.elyasasmad.istudent.models.LastLoginSession;
import com.elyasasmad.istudent.utils.ItaleemcClient;
import com.elyasasmad.istudent.utils.SessionData;
import com.elyasasmad.istudent.utils.throwable.LoginException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField matricNumber;

    @FXML
    private PasswordField password;

    @FXML
    private Text errorText;

    @FXML
    private Button submitButton;

    @FXML
    private VBox container;

    @FXML
    private void onClickSubmit(ActionEvent event) {

        if (matricNumber.getText().equals("") || password.getText().equals("")) {
            errorText.setText("Please complete all fields!");
            return;
        }

        // Clears error
        errorText.setText("");
        submitButton.setText("Authenticating...");
        submitButton.setDisable(true);

        Task<Void> loginTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Will throw LoginException if unsuccessful
                    ItaleemcClient italeemcClient = new ItaleemcClient();
                    String token = italeemcClient.login(matricNumber.getText(), password.getText());

                    // Save session into database
                    SessionData sessionData = new SessionData();
                    sessionData.addSession(matricNumber.getText(), token);
                    sessionData.close();

                    Platform.runLater(() -> {
                        // Any UI change should be handled inside Platform.runLater method because any UI change
                        // is managed by another thread
                        errorText.setText("");
                        submitButton.setText("Login successful!");
                        navigateToDashboard();
                    });
                }
                catch (LoginException e) {
                    handleError(e);
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                    handleError(e);
                }
                return null;
            }
        };

        // Start login thread
        new Thread(loginTask).start();

    }

    private void handleError(Exception e) {
        Platform.runLater(() -> {
            errorText.setText(e.getMessage());
            submitButton.setDisable(false);
            submitButton.setText("Re-attempt Login");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        new Thread(getSessionTokenTask()).start();

    }

    private Task<LastLoginSession> getSessionTokenTask() {

        Task<LastLoginSession> task = new Task<>() {
            @Override
            protected LastLoginSession call() {

                SessionData sessionData = new SessionData();
                LastLoginSession session = sessionData.getLastSessionData();
                sessionData.close();

                if (session == null) {
                    System.out.println("NULL");
                    return null;
                }

                return session;
            }
        };

        task.setOnSucceeded(event -> {

            LastLoginSession lastLoginSession = task.getValue();

            if (lastLoginSession != null) {
                if (!lastLoginSession.isLoggedOut()) {
                    navigateToDashboard();
                }
            }

        });

        return task;

    }

    private void navigateToDashboard() {
        try {
            // This try-catch block handles page transition
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage stage = (Stage) container.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setTitle("Dashboard | IIUM i-Tahdeer");
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
