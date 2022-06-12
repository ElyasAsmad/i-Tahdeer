package com.elyasasmad.istudent.constants;

public class StaticURL {

    private StaticURL() { } // Prevents instantiation

    public static final String AUTH_URL = "https://italeemc.iium.edu.my/login/token.php";

    public static final String USER_INFO_URL = "https://italeemc.iium.edu.my/webservice/rest/server.php?moodlewsrestformat=json&wsfunction=core_webservice_get_site_info";

    public static final String USER_TASKS_URL = "https://italeemc.iium.edu.my/webservice/rest/server.php?moodlewsrestformat=json&wsfunction=core_calendar_get_action_events_by_timesort";

}
