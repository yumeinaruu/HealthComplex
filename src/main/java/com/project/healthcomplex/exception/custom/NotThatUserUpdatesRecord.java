package com.project.healthcomplex.exception.custom;

public class NotThatUserUpdatesRecord extends RuntimeException {
    String message;

    public NotThatUserUpdatesRecord(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Problem with records. Error occurred: " + message;
    }
}
