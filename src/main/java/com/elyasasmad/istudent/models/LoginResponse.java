package com.elyasasmad.istudent.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

    @JsonProperty
    private String token;
    @JsonProperty
    private String privatetoken;
    @JsonProperty
    private String debuginfo;
    @JsonProperty
    private String error;
    @JsonProperty
    private String errorcode;
    @JsonProperty
    private String reproductionlink;
    @JsonProperty
    private String stacktrace;

    public String getToken() {
        return token;
    }

    public String getPrivateToken() {
        return privatetoken;
    }

    public String getError() { return error; }
}