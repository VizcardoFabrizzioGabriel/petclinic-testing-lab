package com.tecsup.petclinic.exceptions;

public class TypeNotFoundException extends RuntimeException {

    public TypeNotFoundException(String message) {
        super(message);
    }
}
