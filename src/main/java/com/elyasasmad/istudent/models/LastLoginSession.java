package com.elyasasmad.istudent.models;

public class LastLoginSession {

    private String matricNumber, token, loggedInTime;
    private boolean isLoggedOut;

    public LastLoginSession(String matricNumber, String token, String loggedInTime, int isLoggedOut) {
        this.token = token;
        this.loggedInTime = loggedInTime;
        this.isLoggedOut = isLoggedOut == 1;
    }

    public String getToken() {
        return token;
    }

    public String getLoggedInTime() {
        return loggedInTime;
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public boolean isLoggedOut() { return isLoggedOut; }
}
