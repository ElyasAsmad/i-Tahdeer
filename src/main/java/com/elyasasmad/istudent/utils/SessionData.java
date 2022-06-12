package com.elyasasmad.istudent.utils;

import com.elyasasmad.istudent.constants.DatabaseConstants;
import com.elyasasmad.istudent.models.LastLoginSession;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessionData {

    Connection connection = null;

    public SessionData() {

        try {

            connection = DriverManager.getConnection("jdbc:sqlite:itahdeer.db");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // 30 seconds timeout

            int rows = statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Session (
                        sessionID INTEGER PRIMARY KEY AUTOINCREMENT,
                        matricNumber TEXT,
                        token TEXT,
                        loginTime TEXT,
                        isLoggedOut INTEGER,
                        logoutTime TEXT
                    )
                    """);

            statement.close();

            System.out.println("Rows updated: " + rows);

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

    }

    public void addSession(String username, String token) {

        try {

            String sql = "INSERT INTO Session (matricNumber, token, loginTime, isLoggedOut, logoutTime) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, username);
            statement.setObject(2, token);
            statement.setObject(3, getLocalTime());
            statement.setObject(4, 0);
            statement.setObject(5, DatabaseConstants.ACTIVE_SESSION);
            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public String logoutCurrentSession(String token) {

        try {

            String logoutTime = getLocalTime();

            PreparedStatement sessionStatement = connection.prepareStatement("SELECT sessionID FROM Session WHERE token = ? ORDER BY sessionID DESC LIMIT 1");
            sessionStatement.setObject(1, token);
            String sessionID = sessionStatement.executeQuery().getString("sessionID");

            String sql = "UPDATE Session SET isLoggedOut = ?, logoutTime = ? WHERE sessionID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, 1);
            statement.setObject(2, logoutTime);
            statement.setObject(3, sessionID);
            statement.executeUpdate();
            statement.close();

            return logoutTime;

        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public LastLoginSession getLastSessionData() {

        try {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Session ORDER BY sessionID DESC LIMIT 1");

            if (rs.isClosed()) { return null; }

            LastLoginSession lastLoginSession = new LastLoginSession(rs.getString("matricNumber"), rs.getString("token"), rs.getString("loginTime"), rs.getInt("isLoggedOut"));

            statement.close();

            return lastLoginSession;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getLocalTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return dateTimeFormatter.format(dateTime);
    }

    public void close() {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
