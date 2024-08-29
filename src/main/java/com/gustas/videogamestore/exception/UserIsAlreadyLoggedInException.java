package com.gustas.videogamestore.exception;

public class UserIsAlreadyLoggedInException extends RuntimeException {

    public UserIsAlreadyLoggedInException(String message) {
        super(message);
    }

}