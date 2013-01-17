package com.iCo6.command.exceptions;

public class InvalidUsage extends Exception {
    public InvalidUsage(String message) {
        super("<rose>Неправильное использование: " + message);
    }
}
