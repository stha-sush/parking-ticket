package com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class VehicleAlreadyExistsException extends RuntimeException {

    public VehicleAlreadyExistsException(String plateNumber) {
        super("Vehicle already exists for plate: " + plateNumber);
    }
}
