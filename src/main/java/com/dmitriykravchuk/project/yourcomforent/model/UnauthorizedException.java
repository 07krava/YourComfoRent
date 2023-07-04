package com.dmitriykravchuk.project.yourcomforent.model;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}