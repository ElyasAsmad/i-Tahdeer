package com.elyasasmad.istudent.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteInfoResponse {

    @JsonProperty
    private String firstname;

    @JsonProperty
    private String lastname;

    @JsonProperty
    private String userpictureurl;

    @JsonProperty
    private String message;

    @JsonProperty
    private String errorcode;

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUserPictureUrl() {
        return userpictureurl;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorcode;
    }
}
