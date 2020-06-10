package com.phoenixhosman.phoenixdbmanagers;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * The type ObjectGradeResponse
 */
@Keep
public class ObjectGradeResponse {
    @SerializedName("success") private Boolean success;
    @SerializedName("message") private String message;
    @SerializedName("data") private String data;

    /**
     * Instantiates a new ObjectGradeResponce
     *
     * @param success   Boolean Success indicater
     * @param message   String The success message
     * @param data      String The Grade name
     */
    ObjectGradeResponse(Boolean success, String message, String data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    ObjectGradeResponse() {

    }

    /**
     * Gets the success flag
     *
     * @return the success flag
     */
    Boolean getSuccess() {
        return success;
    }

    /**
     * Sets the success flag
     *
     * @param success - Boolean - the success flag
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * Get the message
     *
     * @return the status message
     */
    String getMessage() {
        return message;
    }

    /**
     * Set the status message
     *
     * @param message - String - the status message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the data
     *
     * @return the data value
     */
    String getData() {
        return data;
    }

    /**
     * Set the data value
     *
     * @param data - String - the data value
     */
    public void setData(String data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return data;
    }
}
