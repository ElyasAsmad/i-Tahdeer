package com.elyasasmad.istudent.utils;

import com.elyasasmad.istudent.constants.StaticURL;
import com.elyasasmad.istudent.models.LoginResponse;
import com.elyasasmad.istudent.models.SiteInfoResponse;
import com.elyasasmad.istudent.models.StudentTask;
import com.elyasasmad.istudent.utils.throwable.LoginException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ItaleemcClient {

    public ItaleemcClient() {}

    public String login(String username, String password) throws IOException, LoginException {

        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("service", "moodle_mobile_app")
                .build();

        Request request = new Request.Builder()
                .url(StaticURL.AUTH_URL)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        ObjectMapper mapper = new ObjectMapper();
        LoginResponse loginResponse = mapper.readValue(response.body().string(), LoginResponse.class);

        if (loginResponse.getError() != null) throw new LoginException(loginResponse.getError());

        return loginResponse.getToken();

    }

    public String logout(String token) {

        SessionData sessionData = new SessionData();
        return sessionData.logoutCurrentSession(token);

    }

    public ArrayList<StudentTask> getUserTasks(String token) throws IOException, LoginException {

        OkHttpClient client = new OkHttpClient();
        ArrayList<StudentTask> courseTasks = new ArrayList<>();

        FormBody formBody = new FormBody.Builder()
                .add("wstoken", token)
                .add("wsfunction", "core_calendar_get_action_events_by_timesort")
                .add("limitnum", String.valueOf(10))
                .add("timesortfrom", getUnixTime())
                .build();

        Request request = new Request.Builder()
                .url(StaticURL.USER_TASKS_URL)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();

        String rawResponse = response.body().string();

        if (!response.isSuccessful()) throw new LoginException(response.message());

        JSONArray tasks = new JSONObject(rawResponse).getJSONArray("events");

        for (Object obj: tasks) {
            courseTasks.add(new StudentTask(obj.toString()));
        }

        return courseTasks;

    }

    private String getUnixTime() {
        long unixTime = System.currentTimeMillis() / 1000L;
        return String.valueOf(unixTime);
    }

    public SiteInfoResponse getUserInfo(String token) throws IOException, LoginException {

        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("wstoken", token)
                .build();

        Request request = new Request.Builder()
                .url(StaticURL.USER_INFO_URL)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        ObjectMapper mapper = new ObjectMapper();
        SiteInfoResponse siteInfoResponse = mapper.readValue(response.body().string(), SiteInfoResponse.class);

        if (siteInfoResponse.getErrorCode() != null) throw new LoginException(siteInfoResponse.getMessage());

        return siteInfoResponse;

    }

}
