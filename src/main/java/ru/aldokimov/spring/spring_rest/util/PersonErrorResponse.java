package ru.aldokimov.spring.spring_rest.util;

public class PersonErrorResponse {
    private String message;
    private long timesTamp;

    public PersonErrorResponse(String message, long timesTamp) {
        this.message = message;
        this.timesTamp = timesTamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimesTamp() {
        return timesTamp;
    }

    public void setTimesTamp(long timesTamp) {
        this.timesTamp = timesTamp;
    }
}
