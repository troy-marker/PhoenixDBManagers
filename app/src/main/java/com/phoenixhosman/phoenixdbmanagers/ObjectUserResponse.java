package com.phoenixhosman.phoenixdbmanagers;

import org.json.JSONArray;

/**
 * Thje ObjectUserResponse type definition
 */
public class ObjectUserResponse {
   private String status;
   private String message;
   private ObjectUser[] data;

    public ObjectUserResponse(String status, String message, ObjectUser[] data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ObjectUserResponse() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ObjectUser[] getData() {
        return data;
    }

    public void setData(ObjectUser[] data) {
        this.data = data;
    }
}
