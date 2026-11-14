package com.tecsup.petclinic.exceptions;

public class SpecialtyNotFoundException extends RuntimeException {

    public SpecialtyNotFoundException(String message) {
        super(message);
    }
}
